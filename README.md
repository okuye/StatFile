# TeamsStats

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

# Assumptions and Limitations
This program assumes at least maven 2 is installed <br>
I could not get hold of a dataset that had a TEAM_SIDE element (question 3), making the output look like :<br>
<TEAM_NAME><SUM_OF_STATISTIC_VALUES>

This program is useful for reading simple xml . Complex xml files can be best dealt with using scala.xml.XML along with rdd's .<br>
Thoughtwork's API com.thoughtworks.xstream API can also be used for complex XML.<br>

The output of **_Question3_** will be written to the **_outPutDataRePartitioned_** directoy .<br>
It will be in the format part-00000-xxx-xxx-xxx-xxx-xxx-xxx.xml with the limitation of not being well formed.

The exception handling could be made a lot more elegant.

### Note
I could not get hold of xml data so I converted the following into xml.<br>
**_Source:_**
2018 College Football Statistics
Sun Belt

2018 Sun Belt Receiving Statistics (CSV) :<br>
**_https://sports-statistics.com/database/college-football-stats-csv/2018/sun-belt/receiving.csv_** <br>
2018 Sun Belt Defense Statistics (CSV) :
<br>**_https://sports-statistics.com/database/college-football-stats-csv/2018/sun-belt/defense.csv_** <br>
2018 Sun Belt Kickoff-return Statistics (CSV) :
<br>**_https://sports-statistics.com/database/college-football-stats-csv/2018/sun-belt/kickoff-return.csv_**

# Structure

The **_xml-data_** directory is where the three xml files this program uses.<br>
The following files exist within the root directory of the program : <br>
<ul>
<li>jar **_"StatFile-xx.0-SNAPSHOT.jar"_** - The is an executable jar of the program</li>
<li>**_run_statfile.sh_** - is used to run the program</li>
<li>**_build_statfile.sh_** - is used to build the program</li>
</ul>


# Building the program
Simply run the execute the **_build_statfile.sh_**  file i.e. <b>./build_statfile.sh</b><br>
The build file does the following : <br>
<ul>
<li>compiles java classes <br>
mvn compile</li>
<li>creates a jar in target folder<br>
mavn package</li>
<li>creates a jar in target folder and adds to your local .m2 repository<br>
mvn install</li>
</ul>

# Running the program
**_run_statfile.sh_** - This is used to start the pipe line and requires two parameters **_statistic_** such as  :
<ul>
<li>defence</li>
<li>kickoff-return</li>
<li>recieving</li>
</ul>

and the **_path_** to the location of the file being processed.<br>
i.e. <b>./run_statfile.sh defence  /temp/defence.xml</b> 




 
