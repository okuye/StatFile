package org.example


import org.example.Structure.getData

import java.io.{FileNotFoundException, IOException}

object StatExecutor {

  def main(args: Array[String]): Unit = {
    if ((args(0).length == 0) || (args(1).length == 0)) {
      System.out.println("The arguments that represent the statistic and path of the data file is required : ")
      System.out.println("java -jar StatFile-x.xx-SNAPSHOT-jar-with-dependencies.jar  $stat $path ")
      System.exit(0)
    }

    try {

      if (!args(0).contains("defence") && !args(0).contains("kickoff-return") && !args(0).contains("recieving")) {
        println("The statistic you have provided can not be computed \nThis program computes the following stats :\ndefence\nkickoff-return\nrecieving")
        System.exit(0)
      }

      getData(args(0), args(1))

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
