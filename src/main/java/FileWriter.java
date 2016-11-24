import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import data.AgeCategory;
import data.Category;
import data.Participant;
import data.ParticipantsGroup;

public class FileWriter {

    /**
     * Writes participants to files
     *
     * @param path              Folder path
     * @param participantsGroup Group of participants
     * @throws IOException Error during writing to disk
     */
    public void writeToFile(String path, ParticipantsGroup participantsGroup) throws IOException {

        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun runWriter = paragraph.createRun();
            runWriter.setBold(true);
            AgeCategory ageCategory = participantsGroup.getAgeCategory();
            runWriter.setText("Age category: " + ageCategory);
            addParticipantTables(doc, participantsGroup.getCategoryMap(), ageCategory.getRatioOffset());
            String fileName = path +
                    ageCategory +
                    "_" +
                    ageCategory.getStartAge() +
                    "_" +
                    ageCategory.getEndAge() +
                    ".docx";
            File file = new File(fileName);
            System.out.println("Wrote to file: " + file.getAbsolutePath());
            try (OutputStream out = new FileOutputStream(file)) {
                doc.write(out);
            }
        }
    }

    private void addParticipantTables(XWPFDocument doc, Map<Category, List<Participant>> categoryMap, int ratioOffset) {

        for (Map.Entry<Category, List<Participant>> entry : categoryMap.entrySet()) {

            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun runWriter = paragraph.createRun();
            runWriter.setText("Category: " + entry.getKey() +
                    " Ratio: " + (entry.getKey().getStartRatio() - ratioOffset)
                    + " - " + (entry.getKey().getEndRatio() - ratioOffset));

            XWPFTable table = doc.createTable(1, 9);
            XWPFTableRow header = table.getRow(0);
            header.getCell(0).setText("Last Name");
            header.getCell(1).setText("First Name");
            header.getCell(2).setText("Height");
            header.getCell(3).setText("Weight");
            header.getCell(4).setText("Ratio");
            header.getCell(5).setText("BirthDate");
            header.getCell(6).setText("Age");
            header.getCell(7).setText("City");
            header.getCell(8).setText("Full name with City");
            entry.getValue().forEach(participant -> {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(participant.getLastName());
                row.getCell(1).setText(participant.getName());
                row.getCell(2).setText(String.valueOf(participant.getHeight()));
                row.getCell(3).setText(String.valueOf(participant.getWeight()));
                row.getCell(4).setText(String.valueOf(participant.getRatio()));
                row.getCell(5).setText(participant.getBirthDate().toString());
                row.getCell(6).setText(String.valueOf(participant.getAge()));
                row.getCell(7).setText(String.valueOf(participant.getCity()));
                row.getCell(8).setText(participant.getLastName()
                        + " " + participant.getName()
                        + " " + participant.getCity());

            });
        }
    }
}
