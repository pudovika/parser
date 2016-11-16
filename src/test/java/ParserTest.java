import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import data.Participant;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ParserTest {

    private FileReader fileReader;
    private Parser parser;
    private String resourcesFolderPath;

    @Before
    public void setUp() throws Exception {
        fileReader = new FileReader();
        parser = new Parser();
        ClassLoader classLoader = getClass().getClassLoader();
        resourcesFolderPath = new File(classLoader.getResource("participants.docx").getFile()).getParent();
    }

    @Test
    public void testParseFile() throws Exception {
        List<Participant> participants = parser.parseDocxFiles(fileReader.readFilesFromFolder(resourcesFolderPath));
        assertEquals(32, participants.size());
    }

    @Test
    public void testParsedContent() throws Exception {
        List<Participant> participants = parser.parseDocxFiles(fileReader.readFilesFromFolder(resourcesFolderPath));
        assertTrue(participants.size() > 0);
        Participant participant = participants.get(0);
        assertEquals("John", participant.getName());
        assertEquals("Down", participant.getLastName());
        assertEquals(new Integer(181), participant.getHeight());
        assertEquals(new Integer(83), participant.getWeight());

    }
}
