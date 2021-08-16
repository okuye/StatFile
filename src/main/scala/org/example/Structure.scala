package org.example

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import org.apache.spark.sql.functions.{col, rank}

import java.io.File
import scala.Console.println

object Structure {

  lazy val spark: SparkSession = {
    SparkSession.builder().
      master("local[2]").
      appName("StatFile").
      config("spark.ui.enabled", false).
      getOrCreate()
  }

  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

  def saveXml(path: String, extension: String, xmlFileName: String) = {
    val files = getListOfFiles(new File(path), List(extension))
    var filePath = ""
    for (a <- files) {
      filePath = a.getPath
    }
    println(filePath)
    val currentXml = filePath.replace(".txt", ".xml")
    mv(filePath, currentXml)
  }

  import util.Try

  def mv(oldName: String, newName: String) =
    Try(new File(oldName).renameTo(new File(newName))).getOrElse(false)

  def saveData(stat: String) = {

    //Save the data as a single file in the outPutDataRePartitioned directory i.e. part-00000-8835d608-30fa-4364-b46a-a2c53da4bedf-c000.xml
    val data = spark.read.text("outPutData").repartition(1)

    data.printSchema()

    data.write.format("text").mode(
      "overwrite").save("outPutDataRePartitioned")

    data.show(false)

    saveXml("outPutDataRePartitioned", ".txt", stat)
  }

  def processData(sumDf:DataFrame,stat:String) = {

    //Save the data to a temp directory
    sumDf.write.format("com.databricks.spark.xml").option("rootTag", "TEAMS").option("rowTag", "TEAM_SIDE").mode(
      "overwrite").save("outPutData")

    //Save the data as a single file in the outPutDataRePartitioned directory i.e. part-00000-8835d608-30fa-4364-b46a-a2c53da4bedf-c000.xml
    val data = spark.read.text("outPutData").repartition(1)

    data.printSchema()

    data.write.format("text").mode(
      "overwrite").save("outPutDataRePartitioned")

    data.show(false)

    saveXml("outPutDataRePartitioned", ".txt", stat)
  }

  def getSumarryRankedData(ranked:DataFrame, stat: String, path: String):DataFrame = {
    val searchCol = stat.toLowerCase match {
      case "defence" => "Tot"
      case "kickoff-return" => "Yds"
      case "recieving" => "Rec"
    }
    val result = ranked
      .withColumnRenamed("rank", "position_in_ranking")
      .withColumn("firstname", functions.split(col("Player"), " ").getItem(0))
      .withColumn("lastname", functions.split(col("Player"), " ").getItem(1))
      .select(col("position_in_ranking"), col("firstname"), col("lastname"), col(searchCol))

    result.show(false)

    val sumDf = ranked.groupBy("Team").sum(searchCol).withColumnRenamed(s"sum(${searchCol})", "SUM_OF_STATISTIC_VALUES")
      .withColumnRenamed("Team", "TEAM_NAME")

    sumDf.show(false)

    //Save the data to a temp directory
    sumDf.write.format("com.databricks.spark.xml").option("rootTag", "TEAMS").option("rowTag", "TEAM_SIDE").mode(
      "overwrite").save("outPutData")

    sumDf
  }

  def getRankedData(stat: String, path: String):DataFrame = {

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

    ranked
  }
}