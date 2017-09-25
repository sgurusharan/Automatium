package com.automatium.exception;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class UnknownActionFileFormatException extends AutomatiumException {
    public UnknownActionFileFormatException(String filePath) {
        super("Automatium cannot process the file type of '" + filePath + "'");
    }
}
