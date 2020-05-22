package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class LogHelper {
    private static final LinkedHashMap<String, String> myLog = new LinkedHashMap<>();

    public static void debug(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " DEBUG: " + data;
        myLog.put("DEBUG", logData);
    }

    public static void info(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " INFO: " + data;
        myLog.put("INFO", logData);
    }

    public static void warning(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " WARNING: " + data;
        myLog.put("WARNING", logData);
    }

    public static void error(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " ERROR: " + data;
        myLog.put("ERROR", logData);
    }

    public static void critical(String data) {
        String logData = getTime("yyyy-MM-dd HH:mm") + " CRITICAL: " + data;
        myLog.put("CRITICAL", logData);
    }

    public static void writeAllLog() {
        String file_name = "./log/" + getTime("yyyy_MM_dd_HH_mm") + " Log.txt";
    }

    private static String getTime(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
