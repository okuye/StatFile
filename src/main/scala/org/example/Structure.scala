package org.example

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, rank}
import org.apache.spark.sql.{SparkSession, functions}

import scala.Console.println

object Structure {

  private lazy val spark: SparkSession = {
    SparkSession.builder().
      master("local[2]").
      appName("StatFile").
      config("spark.ui.enabled", false).
      getOrCreate()
  }


  def getData(stat: String, path: String) = {

    val searchCol = stat.toLowerCase match {
      case "defence" => "Tot"
      case "kickoff-return" => "Yds"
      case "recieving" => "Rec"
    }

    val df = spark.read
      .format("com.databricks.spark.xml")
      .option("mode", "FAILFAST")
      .option("nullValue", "")
      .option("rootTag", "Teams")
      .option("rowTag", "row")
      .option("ignoreSurroundingSpaces", "true")
      .load(path)

    df.printSchema()

    //Drop all cells with nulls
    val ranked = df.na.drop().withColumn("rank", rank().over(Window.partitionBy("Team").orderBy(col(searchCol).desc)))
      .filter(col("rank") <= 5)

    println(s"The total count is ${ranked.count()}")

    val result = ranked
      .withColumnRenamed("rank","position_in_ranking")
      .withColumn("firstname", functions.split(col("Player"), " ").getItem(0))
      .withColumn("lastname", functions.split(col("Player"), " ").getItem(1))
      .select(col("position_in_ranking"),col("firstname"),col("lastname"),col(searchCol))

    result.show(false)

    val sumDf = ranked.groupBy("Team").sum(searchCol).withColumnRenamed(s"sum(${searchCol})", "SUM_OF_STATISTIC_VALUES")
      .withColumnRenamed("Team", "TEAM_NAME")

    sumDf.show(false)

    //Save the data to a temp directory
    sumDf.write.format("com.databricks.spark.xml").option("rootTag", "TEAMS").option("rowTag", "TEAM_SIDE").mode(
      "overwrite").save("outPutData")

    //Save the data as a single file in the outPutDataRePartitioned directory i.e. part-00000-8835d608-30fa-4364-b46a-a2c53da4bedf-c000.xml
    val data = spark.read.text("outPutData").repartition(1)
    data.write.format("text").mode(
      "overwrite").save("outPutDataRePartitioned")

    data.show(false)

  }
}