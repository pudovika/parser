import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import data.AgeCategory;
import data.Participant;
import data.ParticipantsGroup;

import static junit.framework.TestCase.assertEquals;

public class FileWriterTest {

    private static final String PARTICIPANTS_DOCX = "Kharkiv_participants.docx";
    private String folderPath;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        fileReader = new FileReader();
        fileWriter = new FileWriter();
        categoryService = new CategoryService();
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileResource = classLoader.getResource(PARTICIPANTS_DOCX);
        if (fileResource != null){
            folderPath = new File(fileResource.getFile()).getParent();
        }

    }

    @Test
    public void testWriteFilesToFolder() throws Exception {
        List<File> files = fileReader.readFilesFromFolder(folderPath);
        Parser parser = new Parser();
        List<Participant> participants = parser.parseDocxFiles(files);
        List<ParticipantsGroup> participantsGroups = categoryService.processParticipants(participants);

        participantsGroups.forEach(participantsGroup -> {
            try {
                fileWriter.writeToFile(folderPath + "\\", participantsGroup);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        List<File> addedFiles = fileReader.readFilesFromFolder(folderPath);
        assertEquals(addedFiles.size() - files.size(), participantsGroups.size());
    }

    @After
    public void tearDown() throws Exception {
        Arrays.stream(new File(folderPath)
                .listFiles((f, p) -> !p.startsWith(PARTICIPANTS_DOCX)))
                .forEach(File::delete);
    }
}
