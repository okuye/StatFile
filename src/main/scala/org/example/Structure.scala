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

//    val byTeam = Window.partitionBy(col("Team"))
//    df.withColumn("avg", avg('salary) over byTeam).show

    val ranked = df.withColumn("rank", rank().over(Window.partitionBy("Team").orderBy(col("Rec").desc)))
      .filter(col("rank") <= 5)
      .drop("rank")
//      .limit(5)

//    df.groupBy("Team").agg(col("Population"),
//      avg("Population")).show(5)


    println(s"The total count is ${ranked.count()}")
            ranked.printSchema()
            ranked.show(false)

//    val grouped = ranked.groupBy("Team").agg(collect_list(struct("A", "rank")).alias("tmp"))
//    val grouped = ranked.groupBy("Team")
//    println(s"The total count is ${grouped.count()}")
//    grouped.printSchema()
//    grouped.show(false)

//        println(s"The total count is ${df.count()}")
//        df.printSchema()
//        df.show(false)

  }

}
