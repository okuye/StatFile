package org.example
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, rank}

object Structure {

  private lazy val spark: SparkSession = {
    SparkSession.builder().
      master("local[2]").
      appName("StatFile").
      config("spark.ui.enabled", false).
      getOrCreate()
  }

  def getData(path:String) = {
    val df = spark.read
      .format("com.databricks.spark.xml")
      .option("mode", "FAILFAST")
      .option("nullValue", "")
      .option("rootTag", "Teams")
      .option("rowTag", "row")
      .option("ignoreSurroundingSpaces","true")
      .load(path)

    val ranked = df.na.drop().withColumn("rank", rank().over(Window.partitionBy("Team").orderBy(col("Rec").desc)))
      .filter(col("rank") <= 5)

    println(s"The total count is ${ranked.count()}")
            ranked.printSchema()
            ranked.show(false)

    val sumDf = ranked.groupBy("Team").sum("Rec").withColumnRenamed("sum(Rec)","sum_of_statistic_values")

    sumDf.show(false)

  }

}
