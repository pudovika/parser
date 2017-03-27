/**
 * Created by Pavlo_Udovika on 27-Mar-17.
 */
public class StatisticService {

    private static StatisticService statisticService;

    private int inputTotalReadCount;

    private int outputTotalWroteCount;

    public static StatisticService getStatisticService() {
        if(statisticService == null) {
            statisticService = new StatisticService();
        }
        return statisticService;
    }

    public void clearInputTotalReadCount(){
        inputTotalReadCount = 0;
    }

    public void addInputTotalReadCount(int count) {
        inputTotalReadCount = inputTotalReadCount + count;
    }

    public void clearOutputTotalWroteCount(){
        outputTotalWroteCount = 0;
    }

    public void addOutputTotalWroteCount(int count) {
        outputTotalWroteCount = outputTotalWroteCount + count;
    }

    public int getInputTotalReadCount() {
        return inputTotalReadCount;
    }

    public int getOutputTotalWroteCount() {
        return outputTotalWroteCount;
    }
}
