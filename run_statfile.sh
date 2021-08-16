#!/bin/bash

if [ $# -ne 2 ]
then
  echo "script requires at least 2 arguments"
  echo "A statistic parameter i.e. defence, kickoff-return or recieving \n and a path to the data file"

  exit 1
fi
echo $1
echo $2
pwd
java -jar target/StatFile-1.0-SNAPSHOT-jar-with-dependencies.jar $1 $2