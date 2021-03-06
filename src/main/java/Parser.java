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

    private static final int LAST_NAME_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int BIRTHDATE_INDEX = 3;
    private static final int WEIGHT_INDEX = 5;
    private static final int HEIGHT_INDEX = 6;
    private static final int GENDER_INDEX = 8;
    private SimpleLogger simpleLogger = SimpleLogger.getSimpleLogger();

    private StatisticService statisticService = StatisticService.getStatisticService();

    /**
     * Parses participant .docx files to entity
     * @param files list of files to parse
     * @return list of participant data
     */
    public List<Participant> parseDocxFiles(List<File> files) {
        return files.stream()
                .map(this::parseDocxFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Participant> parseDocxFile(File file) {
        List<Participant> participants = new ArrayList<>();
        String city = file.getName().split("_")[0];
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFTable> tables = document.getTables();
            if (tables.size() == 1) {
                participants = tables.get(0).getRows()
                        .stream()
                        .skip(1)
                        .map(this::mapToParticipant)
                        .collect(Collectors.toList());
            }

            fis.close();
        } catch (Exception e) {
            simpleLogger.logException(e);
        }

        List<Participant> participantReadList = participants.stream()
                .filter(participant -> !participant.getLastName().isEmpty())
                .filter(participant -> !participant.getName().isEmpty())
                .collect(Collectors.toList());
        statisticService.addInputTotalReadCount(participantReadList.size());
        return participantReadList.stream()
                .map(setParticipantCityFunction(city))
                .filter(participant -> participant.getHeight() != null)
                .filter(participant -> participant.getWeight() != null)
                .collect(Collectors.toList());
    }

    private Function<Participant, Participant> setParticipantCityFunction(String city) {
        return participant -> {
            participant.setCity(city);
            statisticService.addInputCityCount(city);
            return participant;};
    }

    /**
     * Maps poi XWPFTableRow to Participant entity
     * @param participantTableRow table row with raw data from file
     * @return Participant
     */
    private Participant mapToParticipant(XWPFTableRow participantTableRow) {
        Participant participant = new Participant();
        if (participantTableRow.getTableICells().size() > 1) {

            participant.setLastName(participantTableRow.getCell(LAST_NAME_INDEX).getText());
            participant.setName(participantTableRow.getCell(NAME_INDEX).getText());

            try {
                participant.setWeight(Double.valueOf(participantTableRow.getCell(WEIGHT_INDEX).getText().replace(",",".")));
                participant.setHeight(Double.valueOf(participantTableRow.getCell(HEIGHT_INDEX).getText().replace(",",".")));
                participant.setRatio(participant.getHeight() + participant.getWeight());
            } catch (NumberFormatException nfe) {
                simpleLogger.logException(nfe);
            }
            String birthDate = participantTableRow.getCell(BIRTHDATE_INDEX).getText();
            participant.setBirthDate(parseDate(birthDate));
            participant.setAge(participant.getBirthDate().until(LocalDate.now()).getYears());
            participant.setGender(participantTableRow.getCell(GENDER_INDEX).getText());
            printParticipant(participant);
        }
        return participant;
    }

    private LocalDate parseDate(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        try {
            return getLocalDate(birthDate, sdf);
        } catch (ParseException e) {
            simpleLogger.logException(e);
        }
        try {
            sdf = new SimpleDateFormat("yyyy");
            return getLocalDate(birthDate, sdf);
        } catch (ParseException e) {
            simpleLogger.logException(e);
        }
        //set default date
        return LocalDate.of(1970, 1,1);
    }

    private LocalDate getLocalDate(String birthDate, SimpleDateFormat sdf) throws ParseException {
        LocalDate date = sdf.parse(birthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return  date;
    }

    private void printParticipant(Participant participant) {
        System.out.println("Parsed First Name: " + participant.getName());
        System.out.println("Parsed Last Name: " + participant.getLastName());
        System.out.println("Parsed Height: " + participant.getHeight());
        System.out.println("Parsed Weight: " + participant.getWeight());
        System.out.println("Parsed Gender: " + participant.getGender());
        System.out.println("Parsed Birth Date: " + participant.getBirthDate());
    }
}
