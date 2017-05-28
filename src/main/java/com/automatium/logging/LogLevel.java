package com.automatium.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    NONE;

    private static Map<LogLevel, String> stringRepresentation = new HashMap<>();

    static {
        stringRepresentation.put(DEBUG, "DEBUG");
        stringRepresentation.put(INFO, "INFO");
        stringRepresentation.put(WARN, "WARN");
        stringRepresentation.put(ERROR, "ERROR");
        stringRepresentation.put(NONE, "NONE");
    }

    @Override
    public String toString() {
        return stringRepresentation.get(this);
    }

    public static LogLevel fromString(String logLevelAsString) {
        Iterator<Map.Entry<LogLevel, String>> logLevelIterator = stringRepresentation.entrySet().iterator();
        while (logLevelIterator.hasNext()) {
            Map.Entry<LogLevel, String> entry = logLevelIterator.next();
            if (entry.getValue().equalsIgnoreCase(logLevelAsString)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
