import data.AgeCategory;
import data.Category;
import data.Participant;
import data.ParticipantsGroup;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class FileWriter {


    private StatisticService statisticService = StatisticService.getStatisticService();

    public void writeStatisticToFile(String path) throws IOException {
        String fileName = path +
                "statistic.txt";
        Path pathFile = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(pathFile)) {
            writer.write(statisticService.generateStatisticReport());
        }
    }

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
                    "_" +
                    participantsGroup.getGender() +
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

            XWPFTable table = doc.createTable(1, 10);
            XWPFTableRow header = table.getRow(0);
            header.getCell(0).setText("Last Name");
            header.getCell(1).setText("First Name");
            header.getCell(2).setText("Height");
            header.getCell(3).setText("Weight");
            header.getCell(4).setText("Ratio");
            header.getCell(5).setText("BirthDate");
            header.getCell(6).setText("Age");
            header.getCell(7).setText("City");
            header.getCell(8).setText("Gender");
            header.getCell(9).setText("Full name with City");

            List<Participant> participantList = entry.getValue();
            statisticService.addOutputTotalWroteCount(participantList.size());
            participantList.forEach(participant -> {
                statisticService.addOutputCityCount(participant.getCity());
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(participant.getLastName());
                row.getCell(1).setText(participant.getName());
                row.getCell(2).setText(String.valueOf(participant.getHeight()));
                row.getCell(3).setText(String.valueOf(participant.getWeight()));
                row.getCell(4).setText(String.valueOf(participant.getRatio()));
                row.getCell(5).setText(participant.getBirthDate().toString());
                row.getCell(6).setText(String.valueOf(participant.getAge()));
                row.getCell(7).setText(String.valueOf(participant.getCity()));
                row.getCell(8).setText(participant.getGender());
                row.getCell(9).setText(participant.getLastName()
                        + " " + participant.getName()
                        + " " + participant.getCity());

            });
        }
    }
}
