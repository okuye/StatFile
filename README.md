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

### Note 
I could not get hold of xml data so I converted the following into xml.<br>
**_Source:_** 
2018 College Football Statistics
Sun Belt

2018 Sun Belt Receiving Statistics (CSV)<br>
2018 Sun Belt Defense Statistics (CSV)<br>
2018 Sun Belt Kickoff-return Statistics (CSV)

**_https://sports-statistics.com/sports-data/college-football-stats-datasets-csv-database/_**

# Structure 

The xml-data is where the xml files this program uses.<br>
The executable jar "StatFile-xx.0-SNAPSHOT.jar" exists in the root directory of where this program is extracted or downloaded to.<br>

**_run_statfile.sh_** : This is used to start the pipe line and requires two parameters **_statistic_** like : 
<ul>
<li>defence</li>
<li>kickoff-return</li>
<li>recieving</li>
</ul>

and the **_path_** to the location of the file being processed.
# Requirements

To run the pipeline simply run the script named **_run_statfile.sh_** 



