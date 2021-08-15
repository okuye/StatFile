package org.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, rank, split}

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

    val ranked = df.na.drop().withColumn("rank", rank().over(Window.partitionBy("Team").orderBy(col(searchCol).desc)))
      .filter(col("rank") <= 5)

    println(s"The total count is ${ranked.count()}")

    val result = ranked
      .withColumnRenamed("rank","position_in_ranking")
      .withColumn("firstname", split(col("Player"), " ").getItem(0))
      .withColumn("lastname", split(col("Player"), " ").getItem(1))
      .select(col("position_in_ranking"),col("firstname"),col("lastname"),col(searchCol))

    result.show(false)

    val sumDf = ranked.groupBy("Team").sum(searchCol).withColumnRenamed(s"sum(${searchCol})", "sum_of_statistic_values")

    sumDf.show(false)

  }
}
