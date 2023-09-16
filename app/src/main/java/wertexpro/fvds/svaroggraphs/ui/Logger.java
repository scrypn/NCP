package wertexpro.fvds.svaroggraphs.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static final int LEVEL_OFF = 0;
    public static final int LEVEL_ALL = 1;
    public static final int LEVEL_WARN = 2;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_FATAL = 4;
    private static SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private static final Logger instance = new Logger();
    private int level = 0;

    public Logger() {
    }

    public static void setLevel(int level) {
        instance.level = level;
    }

    public static void info(String msg) {
        log("INFO", msg);
    }

    public static void warn(String msg) {
        log("WARN", msg);
    }

    public static void error(String msg) {
        log("ERROR", msg);
    }

    public static void fatal(String msg) {
        log("FATAL", msg);
    }

    private static void log(String type, String msg) {
        if (instance.level != 0) {
            int level = 1;
            if (type.compareTo("WARN") == 0) {
                level = 2;
            }

            if (type.compareTo("ERROR") == 0) {
                level = 3;
            }

            if (type.compareTo("FATAL") == 0) {
                level = 4;
            }

            if (instance.level <= level) {
                StackTraceElement calls = Thread.currentThread().getStackTrace()[3];
                String cn = calls.getClassName();
                String ln = String.valueOf(calls.getLineNumber());
                System.out.println(SDF.format(new Date()) + " " + type + " " + cn + ":" + ln + " - " + msg);
            }
        }
    }
}
