package org.example


import org.example.Structure.{getRankedData, getSumarryRankedData, processData, saveData}

import java.io.{FileNotFoundException, IOException}

object StatExecutor {

  def main(args: Array[String]): Unit = {
    if (args(0).isEmpty || args(1).isEmpty) {
      System.out.println("The arguments that represent the statistic and path of the data file is required : ")
      System.out.println("java -jar StatFile-x.xx-SNAPSHOT-jar-with-dependencies.jar  $stat $path ")
      System.exit(0)
    }

    try {

      if (!args(0).equals("defence") && !args(0).equals("kickoff-return") && !args(0).equals("recieving")) {
        println("The statistic you have provided can not be computed\nThis program computes the following stats :\ndefence\nkickoff-return\nrecieving")
        System.exit(0)
      }

      val ranked = getRankedData(args(0), args(1))
      val summary = getSumarryRankedData(ranked, args(0), args(1))

      processData(summary, args(0))

      saveData(args(0))

    } catch {
      case ex: FileNotFoundException => {
        println("Missing file exception")
      }
      case ex: IOException => {
        println("IO Exception")
      }
    } finally {
      println("Exiting finally...")
    }
  }

}
