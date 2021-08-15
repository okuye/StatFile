# Task 
Write a program that:
1. Can be executed with command line: java -cp ... xxx.jar. It should accept two parameters:
   a. type of statistic to check
   b. path to a file to process

2. When executed, program will read the XML from path 1b and will write to output top 5 players with statistic 1a (from any team; desc). Players should be written with format:
   <POSITION_IN_RANKING>. <FIRST NAME> <LAST NAME> - <STATISTIC_VALUE>
3. Will also sum the value for that statistic for each team. Team should be written with format
   <TEAM_SIDE>; <TEAM_NAME> - <SUM_OF_STATISTIC_VALUES>
4. Please provide a README file (in github format), explaining:
   a. how to build the application - this should be a single command
   b. how to run the application - this should be a single command

# Assumptions 

# Source 
2018 College Football Statistics
Sun Belt
2018 Sun Belt Receiving Statistics (CSV)
2018 Sun Belt Defense Statistics (CSV)
2018 Sun Belt Kickoff-return Statistics (CSV)


https://sports-statistics.com/sports-data/college-football-stats-datasets-csv-database/

# Data Exploration

This is used to load xml data into a MySQL database , but it can be configured or modified to cater for any database.

This application also loads the xml downloaded into spark sql tables. 

There should be a test against a small set of xml data .

Another application is responsible for downloading the data and all jobs are rab using a bash script .

The script used to run the application will download the required files , extract them before loading data into a 

MySQL database and spark sql tables.

# MySQL 

I used the distribution  5.7.32, for Linux (x86_64)

# Structure

The script directory also holds database directory containing the database schema used . 

The script directory hosts the executable jar file for this application named 

DataExploration-x.x-SNAPSHOT-jar-with-dependencies.jar  and the other that extracts the xml files is named
 
ExtractData-x.x-SNAPSHOT-jar-with-dependencies.jar.


#####  run_exploration.sh : Used to start the pipe line

#####  schema.sql : Contains the table definitions

#####  application.conf : Contains all parameters needed to run the pipeline


# Requirements

Any MySQL database , at least as high or higher than 5.1 with a user that has all necessary privileges to create a 

database and tables.

Edit the configuration file named (application.conf) setting all required values .

#### Note : MySQL Configuration
The properties mysql, appProp as well as those that start with shell in them have to be updated to suit.
i.e. 

##### Application Configuration
#####
The properties appProp is used by the application with the following properties :

start and end tage for the various files being ingested like belows shows that for the Users.xml file.

There are other properties such as input path , which is the xml data and the extension type of the files being ingested.
 

appProp {
  usersInputStart {
     value = "<row"
     value = ${?VALUE}
  }
  usersInputEnd {
     value = "/>"
     value = ${?VALUE}
  }
  .......
  .......
  .......
  }    
  
 
#These are used to connect to a mysql database from the bash script  

#####  shellsqluser = sa
#####  shellsqlpassword = sa
#####  shellsqldatabase = explorerDB      

#These are used to determine where to .7z file we be downloaded to and where the .xml files will be extracted to.

#####  shellinputpath = /home/xzy
#####  shelloutputPath = /home/abc        
#####                                     
 

#  Running The Application

In the application.conf set the appProp.inputPath.value property to represent the datasets path. 
In the application.conf set the appProp.outputPath.value property to represent the xml files path. 


To run the pipeline simply run the script named run_exploration.sh with the application.conf updated to suit.

# Assumptions and known limitations
I commented out sections that were downloading the files as I assumed they would be made available

I could not get passing a password to mysql to work using a variable in the bash , as can be seen in line 61 of the 
run_exploration.sh script : 

### mysql -u ${shellsqluser} --password="sa" ${shellsqldatabase} < schema.sql > /dev/null 2>/dev/null 

===================================

To build this project use

    mvn install

To run this project

    mvn exec:java

