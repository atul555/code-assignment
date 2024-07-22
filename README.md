# Coding Assignment
## Objective
Write a program that reads a file and finds matches against a predefined set of words. 
There can be up to 10K entries in the list of predefined words. The output of the program should look something like this:

<b>Predefined word          Match count</b>\
FirstName                3500\
LastName                 2700\
Zipcode                  1601

## Instructions for running
### Prerequisites
* Java 11 and up
* maven
* git

### Steps to testrun the application
* Compile using `mvn package`
* Update input file `predefined.txt` to update the words being counted.
* Update input file `input.txt` to update input text to be parsed to look for frequency of words.
* Run `mvn clean compile exec:java` again, and examine `result.txt` for output.
* Run test suite using `mvn test`

## Implementation
### Requirement details:
* Input file is a plain text (ascii) file, every record separated by a new line.
* For this exercise, assume English words only
* The file size can be up to 20 MB
* The predefined words are defined in a text file, every word separated by a newline.
* Use a sample file of your choice for the set of predefined keywords for the exercise.
* Assume that the predefined words file doesn’t contain duplicates
* Maximum length of the word can be upto 256
* Matches should be case-insensitive
* The match should be word to word match, no substring matches.

### Other details
Time alloted : 2 hours

Tech stack : TDD with Java, Junit

### Assumptions
It is a simple word match and context should not matter, ie if first name string is matched as a part of an address, iit will be counted as a match.

Back of the Envelope  calculations:
Text file limit is 10MB. Each line is average 100 character. So worst case, there are 100K lines to ingest.

This is not a real time system, and given the calculation of the scope above, it can be tackled by a single app running on a single thread. 
#### Scalability concern
The application can be scaled horizontally and input file can be broken down into 10MB files and can be parallely processed by a number of instances.
The result of each instance can be aggregated by a master job after completion of processing of each chunk.

### Approach
#### Data Structure
Since we are expecting around 10K words in the predefined file, which means that for the worst case, we end up with 10K keys for our counter data structure.
The most logical data structure we can use to store the frequency information is a HashMap. A single instance of JVM can easily manage 10K keys in a HashMap in the memory.
For scaling it up, we can employ sharding technique by splitting prereq files into multiple files of 10K word size, processed by different instances.

### Testing
JUnit was used for testing. 

