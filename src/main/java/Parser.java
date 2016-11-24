import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import data.Participant;

public class Parser {

    private static SimpleLogger simpleLogger = SimpleLogger.getSimpleLogger();

    /**
     * Parses participant .docx files to entity
     * @param files list of files to parse
     * @return list of participant data
     */
    public List<Participant> parseDocxFiles(List<File> files) {
        return files.stream()
                .map(Parser::parseDocxFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static List<Participant> parseDocxFile(File file) {
        List<Participant> participants = new ArrayList<>();
        final String city = file.getName().split("_")[0];
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFTable> tables = document.getTables();
            if (tables.size() == 1) {
                participants = tables.get(0).getRows()
                        .stream()
                        .skip(1)
                        .map(Parser::mapToParticipant)
                        .collect(Collectors.toList());
            }

            fis.close();
        } catch (Exception e) {
            simpleLogger.logException(e);
        }
        return participants.stream()
                .filter(participant -> !participant.getLastName().isEmpty())
                .filter(participant -> !participant.getName().isEmpty())
                .filter(participant -> participant.getHeight() != null)
                .filter(participant -> participant.getWeight() != null)
                .map(setParticipantCityFunction(city))
                .collect(Collectors.toList());
    }

    private static Function<Participant, Participant> setParticipantCityFunction(String city) {
        return participant -> {
            participant.setCity(city);
            return participant;};
    }

    /**
     * Maps poi XWPFTableRow to Participant entity
     * @param participantTableRow table row with raw data from file
     * @return Participant
     */
    private static Participant mapToParticipant(XWPFTableRow participantTableRow) {
        Participant participant = new Participant();
        if (participantTableRow.getTableICells().size() > 1) {

            participant.setLastName(participantTableRow.getCell(1).getText());
            participant.setName(participantTableRow.getCell(2).getText());

            try {
                participant.setWeight(Double.valueOf(participantTableRow.getCell(6).getText().replace(",",".")));
                participant.setHeight(Integer.valueOf(participantTableRow.getCell(7).getText()));
                participant.setRatio(participant.getHeight() + participant.getWeight());
            } catch (NumberFormatException nfe) {
                simpleLogger.logException(nfe);
            }
            String birthDate = participantTableRow.getCell(3).getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");

            try {
                LocalDate date = sdf.parse(birthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                participant.setBirthDate(date);
                LocalDate now = LocalDate.now();
                participant.setAge(date.until(now).getYears());
            } catch (ParseException e) {
                simpleLogger.logException(e);
            }
            participant.setGender(participantTableRow.getCell(10).getText());
            printParticipant(participant);
        }
        return participant;
    }

    private static void printParticipant(Participant participant) {
        System.out.println("Parsed First Name: " + participant.getName());
        System.out.println("Parsed Last Name: " + participant.getLastName());
        System.out.println("Parsed Height: " + participant.getHeight());
        System.out.println("Parsed Weight: " + participant.getWeight());
        System.out.println("Parsed Gender: " + participant.getGender());
        System.out.println("Parsed Birth Date: " + participant.getBirthDate());
    }
}
