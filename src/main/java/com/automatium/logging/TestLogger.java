package com.automatium.logging;

import com.automatium.config.TestConfiguration;

import java.util.Date;
import java.util.Stack;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public class TestLogger {
    private static TestLogger singletonInstance;

    public static final String DEFAULTLOGTAG = "";

    private LogLevel logLevel;
    private Stack<String> logTags = new Stack<>();

    private TestLogger() {
        logLevel = LogLevel.fromString(TestConfiguration.getSingletonInstance().get("logLevel"));
        if (logLevel == null) {
            // This is probably an IDE run - use the lowest log level (INFO)
            logLevel = LogLevel.INFO;
        }
        logTags.push(DEFAULTLOGTAG);
    }

    public static void printErrorAndExit(String error) {
        System.err.println(error);
        System.exit(1);
    }
    public static TestLogger getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new TestLogger();
        }
        return singletonInstance;
    }

    public String getLogTag() {
        return logTags.peek();
    }

    public void setLogTag(String logTag) {
        this.logTags.push(logTag);
    }

    public void resetLogTag() {
        if (this.logTags.size() > 1) {
            this.logTags.pop();
        }
    }

    private void log(LogLevel logLevel, String tag, String msg) {
        if (shouldLog(logLevel)) {
            Date currentDate = new Date();
            String toLog = String.format("%s: %s: <%s> %s", currentDate.toString(), logLevel.toString(), tag, msg);
            System.out.println(toLog);
        }
    }

    public void info(String tag, String msg) {
        log(LogLevel.INFO, tag, msg);
    }

    public void info(String msg) {
        info(getLogTag(), msg);
    }

    public void debug(String tag, String msg) {
        log(LogLevel.DEBUG, tag, msg);
    }

    public void debug(String msg) {
        debug(getLogTag(), msg);
    }

    public void warn(String tag, String msg) {
        log(LogLevel.WARN, tag, msg);
    }

    public void warn(String msg) {
        warn(getLogTag(), msg);
    }

    public void error(String tag, String msg) {
        log(LogLevel.ERROR, tag, msg);
    }

    public void error(String msg) {
        error(getLogTag(), msg);
    }

    private boolean shouldLogInfo() {
        return logLevel == LogLevel.INFO;
    }

    private boolean shouldLogDebug() {
        return (shouldLogInfo() || logLevel == LogLevel.DEBUG);
    }

    private boolean shouldLogWarn() {
        return (shouldLogDebug() || logLevel == LogLevel.WARN);
    }

    private boolean shouldLogError() {
        return (shouldLogWarn() || logLevel == LogLevel.ERROR);
    }

    private boolean shouldLog(LogLevel logLevel) {
        switch (logLevel) {
            case INFO: return  shouldLogInfo();
            case DEBUG: return shouldLogDebug();
            case WARN: return shouldLogWarn();
            case ERROR: return shouldLogError();
            default: return false;
        }
    }
}
