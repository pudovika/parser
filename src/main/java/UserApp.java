import data.AgeCategory;
import data.Participant;
import data.ParticipantsGroup;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserApp extends Application {

    private File outputFolder;

    private List<Participant> participants;

    private StatisticService statisticService = StatisticService.getStatisticService();

    // App has 2 flow depending on args ui and command line

    public static void main(String[] args) {
        if(args.length == 0){
            Application.launch(args);
        } else {
            CommandLineFlow commandLineFlow = new CommandLineFlow();
            boolean successfulInit = commandLineFlow.init();
            if(!successfulInit) {
                System.out.print("Error during command line flow initialization");
                System.exit(1);
            }
            commandLineFlow.processFiles(args[0]);
            System.exit(0);
        }

    }

    //quite ugly UI implementation TODO RFC

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Kudo Participants");

        FileChooser fileChooser = new FileChooser();

        Button openMultipleButton = new Button("Open files...");

        Button selectDirectoryButton = new Button("Select directory...");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select output directory");

        GridPane inputGridPane = createInputMenu(openMultipleButton, selectDirectoryButton);


        Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);


        RangeSlider categoryFirst = getRangeSlider(150, 230, 190, 220);
        TextArea categoryFirstTextArea = getTextArea();

        RangeSlider categorySecond = getRangeSlider(160, 240, 220, 240);
        TextArea categorySecondTextArea = getTextArea();

        RangeSlider categoryThird = getRangeSlider(170, 250, 240, 250);
        TextArea categoryThirdTextArea = getTextArea();

        RangeSlider categoryFourth = getRangeSlider(180, 260, 250, 260);
        TextArea categoryFourthTextArea = getTextArea();

        RangeSlider categoryFifth = getRangeSlider(190, 270, 260, 270);
        TextArea categoryFifthTextArea = getTextArea();

        Label absoluteLabel = new Label("Absolute (more then prev slider max)");
        TextArea categorySixthTextArea = getTextArea();


        final GridPane categoriesGridPane = new GridPane();
        categoriesGridPane.setHgap(8);
        categoriesGridPane.setVgap(2);

        GridPane.setConstraints(categoryFirst, 0, 0);
        GridPane.setConstraints(categoryFirstTextArea, 0, 1);
        GridPane.setConstraints(categorySecond, 1, 0);
        GridPane.setConstraints(categorySecondTextArea, 1, 1);
        GridPane.setConstraints(categoryThird, 2, 0);
        GridPane.setConstraints(categoryThirdTextArea, 2, 1);
        GridPane.setConstraints(categoryFourth, 3, 0);
        GridPane.setConstraints(categoryFourthTextArea, 3, 1);
        GridPane.setConstraints(categoryFifth, 4, 0);
        GridPane.setConstraints(categoryFifthTextArea, 4, 1);
        GridPane.setConstraints(absoluteLabel, 5, 0);
        GridPane.setConstraints(categorySixthTextArea, 5, 1);

        categoriesGridPane.getChildren().add(categoryFirst);
        categoriesGridPane.getChildren().add(categoryFirstTextArea);
        categoriesGridPane.getChildren().add(categorySecond);
        categoriesGridPane.getChildren().add(categorySecondTextArea);
        categoriesGridPane.getChildren().add(categoryThird);
        categoriesGridPane.getChildren().add(categoryThirdTextArea);
        categoriesGridPane.getChildren().add(categoryFourth);
        categoriesGridPane.getChildren().add(categoryFourthTextArea);
        categoriesGridPane.getChildren().add(categoryFifth);
        categoriesGridPane.getChildren().add(categoryFifthTextArea);
        categoriesGridPane.getChildren().add(absoluteLabel);
        categoriesGridPane.getChildren().add(categorySixthTextArea);

        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        rootGroup.getChildren().add(categoriesGridPane);

        stage.setScene(new Scene(rootGroup));

        Parser parser = new Parser();

        CategoryService categoryService = new CategoryService();

        FileWriter fileWriter = new FileWriter();

        Label totalReadCount = new Label("No info");
        GridPane.setConstraints(totalReadCount, 3, 0);
        inputGridPane.getChildren().add(totalReadCount);

        Label totalWroteCount = new Label("No info");
        GridPane.setConstraints(totalWroteCount, 3, 1);
        inputGridPane.getChildren().add(totalWroteCount);

        selectDirectoryButton.setOnAction(event -> {
            outputFolder = chooser.showDialog(stage);
            statisticService.clearOutputTotalWroteCount();
            List<ParticipantsGroup> participantsGroups = categoryService.processParticipants(participants);
            participantsGroups.forEach(participantsGroup -> {
                try {
                    String path = outputFolder.getPath() + "\\";
                    fileWriter.writeToFile(path, participantsGroup);
                    fileWriter.writeStatisticToFile(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            totalWroteCount.setText(Integer.toString(statisticService.getOutputTotalWroteCount()));

        });

        openMultipleButton.setOnAction(e -> {
            List<File> list =
                    fileChooser.showOpenMultipleDialog(stage);
            if (list != null) {
                statisticService.clearInputTotalReadCount();
                participants = parser.parseDocxFiles(list);
                if (selectDirectoryButton.isDisabled()) {
                    selectDirectoryButton.setDisable(false);
                }

                totalReadCount.setText(Integer.toString(statisticService.getInputTotalReadCount()));

                List<Participant> adults = categoryService.filterByAgeAndGender(participants, AgeCategory.ADULTS, ParticipantsGroup.GENDER_M);
                filterByRangeAndShow(categoryFirst, categoryFirstTextArea, adults);
                filterByRangeAndShow(categorySecond, categorySecondTextArea, adults);
                filterByRangeAndShow(categoryThird, categoryThirdTextArea, adults);
                filterByRangeAndShow(categoryFourth, categoryFourthTextArea, adults);
                filterByRangeAndShow(categoryFifth, categoryFifthTextArea, adults);
                adults.stream()
                        .filter(participant -> participant.getRatio() >= categoryFifth.getHighValue())
                        .forEach(participant -> categorySixthTextArea.appendText(participant.toPrettyEnString() + "\n"));
            }
        });
        stage.show();
    }

    private void filterByRangeAndShow(RangeSlider categoryFirst, TextArea categoryFirstTextArea, List<Participant> participants) {
        participants.stream()
                .filter(participant -> participant.getRatio() >= categoryFirst.getLowValue() && participant.getRatio() < categoryFirst.getHighValue())
                .forEach(participant -> categoryFirstTextArea.appendText(participant.toPrettyEnString() + "\n"));
    }

    private GridPane createInputMenu(Button openMultipleButton, Button selectDirectoryButton){

        Label introLabel = new Label("Set categories sliders and select files, after files selected process will start. Will be showed only adults category (age 18+)");
        Label folderDescriptionLabel = new Label("Select output folder to store categorized files. Button will be enabled after files choosing");

        Label totalRead = new Label("Participants read count: ");
        Label totalWrote = new Label("Participants wrote count: ");
        GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openMultipleButton, 0, 0);
        GridPane.setConstraints(introLabel, 1, 0);
        GridPane.setConstraints(totalRead, 2, 0);
        selectDirectoryButton.setDisable(true);
        GridPane.setConstraints(selectDirectoryButton, 0, 1);
        GridPane.setConstraints(folderDescriptionLabel, 1, 1);
        GridPane.setConstraints(totalWrote, 2, 1);

        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().add(openMultipleButton);
        inputGridPane.getChildren().add(introLabel);
        inputGridPane.getChildren().add(totalRead);
        inputGridPane.getChildren().add(selectDirectoryButton);
        inputGridPane.getChildren().add(folderDescriptionLabel);
        inputGridPane.getChildren().add(totalWrote);
        return inputGridPane;
    }

    private TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(200,300);
        return textArea;
    }

    private RangeSlider getRangeSlider(int min, int max, int lowValue, int highValue) {
        final RangeSlider rangeSlider = new RangeSlider(min, max, lowValue, highValue);
        rangeSlider.adjustLowValue(lowValue);
        rangeSlider.setShowTickMarks(true);
        rangeSlider.setShowTickLabels(true);
        rangeSlider.setMajorTickUnit(10);
        rangeSlider.setMinorTickCount(0);
        rangeSlider.setBlockIncrement(10);
        rangeSlider.setSnapToTicks(true);
        return rangeSlider;
    }



}