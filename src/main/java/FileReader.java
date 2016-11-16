import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private SimpleLogger simpleLogger = SimpleLogger.getSimpleLogger();

    /**
     * Reads participants *.docx files from folder
     * @param folderPath path to folder with files
     * @return List of files
     */
    public List<File> readFilesFromFolder(String folderPath) {
        List<File> files = new ArrayList<>();
        Path dir = FileSystems.getDefault().getPath(folderPath);
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(dir, "*.docx")) {
            for (Path entry : stream) {
                files.add(entry.toFile());
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            simpleLogger.logException(e);
        }
        return files;
    }
}
