import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pavlo_Udovika on 27-Mar-17.
 */
public class StatisticService {

    private static StatisticService statisticService;

    private int inputTotalReadCount;

    private int outputTotalWroteCount;

    private Map<String,Integer> inputCityCounts;

    private Map<String,Integer> outputCityCounts;

    public static StatisticService getStatisticService() {
        if(statisticService == null) {
            statisticService = new StatisticService();
            statisticService.setInputCityCounts(new HashMap<>());
            statisticService.setOutputCityCounts(new HashMap<>());
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

    private void setInputCityCounts(Map<String, Integer> inputCityCounts) {
        this.inputCityCounts = inputCityCounts;
    }

    public void addInputCityCount(String city){
        inputCityCounts.merge(city,1,(integer, integer2) -> integer+1);
    }

    private void setOutputCityCounts(Map<String, Integer> outputCityCounts) {
        this.outputCityCounts = outputCityCounts;
    }

    public void addOutputCityCount(String city){
        outputCityCounts.merge(city,1,(integer, integer2) -> integer+1);
    }

    public String generateStatisticReport(){
        StringBuilder result = new StringBuilder();
        result.append("Input statistic:" + System.lineSeparator());
        inputCityCounts.forEach((k ,v) -> result.append(k + " " + v + System.lineSeparator()));
        result.append("Output statistic:" + System.lineSeparator());
        outputCityCounts.forEach((k ,v) -> result.append(k + " " + v + System.lineSeparator()));
        return result.toString();
    }
}
