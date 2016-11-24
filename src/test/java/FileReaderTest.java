import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class FileReaderTest {

    private String folderPath;
    private FileReader fileReader;

    @Before
    public void setUp() throws Exception {
        fileReader = new FileReader();
        ClassLoader classLoader = getClass().getClassLoader();
        folderPath = new File(classLoader.getResource("Kharkiv_participants.docx").getFile()).getParent();
    }

    @Test
    public void testReadFilesFromFolder() throws Exception {
        List<File> files = fileReader.readFilesFromFolder(folderPath);
        assertTrue(files.size() == 1);
    }

}
