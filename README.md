Participants parser. 

Read list of participants from file, split to age and ratio(height + weight) categories and writes them to output files.

Application has 2 flows.
First UI flow just run without any args.

Second command line flow, run with folder path argument, that contains files to process. Output files will write to the same folder.
Example java -jar parser-all-1.0-SNAPSHOT.jar D:\education\kudo\src\test\resources

For building used Gradle. Run fatJar task. It will put all libraries to jar and this jar can be run anywhere where existing JRE 1.8.

Detailed flow description.
Parse from *.docx files using apache poi library, after parse maps to Participant entities.
CategoryService process participants, splits it to List<ParticipantsGroup> with AgeCategory and Category (ratio).
After that creates output .docx files and writes ParticipantsGroup to files also using apache poi library.
