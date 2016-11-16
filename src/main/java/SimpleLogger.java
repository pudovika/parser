public class SimpleLogger {

    private static SimpleLogger simpleLogger;

    public void logException(Exception e){
        System.out.println("Error: " + e.getMessage());
    }

    public static SimpleLogger getSimpleLogger(){
        if (simpleLogger == null) {
            simpleLogger = new SimpleLogger();
        }
        return simpleLogger;
    }
}
