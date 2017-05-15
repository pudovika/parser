import java.io.IOException;
import java.util.List;

import data.Participant;
import data.ParticipantsGroup;

public class CommandLineFlow {

    private Parser parser;

    private FileReader fileReader;

    private SimpleLogger simpleLogger;

    private CategoryService categoryService;

    private FileWriter fileWriter;

    public boolean init(){
        try {
            parser = new Parser();
            fileReader = new FileReader();
            simpleLogger = SimpleLogger.getSimpleLogger();
            categoryService = new CategoryService("M", "F");
            fileWriter = new FileWriter();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void processFiles(String folderPath) {
        try {
            List<Participant> participants = parser.parseDocxFiles(fileReader.readFilesFromFolder(folderPath));
            List<ParticipantsGroup> participantsGroups = categoryService.processParticipants(participants);

            participantsGroups.forEach(participantsGroup -> {
                try {
                    fileWriter.writeToFile(folderPath + "\\", participantsGroup);
                } catch (IOException e) {
                    simpleLogger.logException(e);
                }
            });
        } catch (Exception e) {
            simpleLogger.logException(e);
        }

    }

}
