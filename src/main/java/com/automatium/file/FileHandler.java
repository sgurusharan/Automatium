package com.automatium.file;

import com.automatium.exception.UnknownActionFileFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgurusharan on 9/22/17.
 */
public abstract class FileHandler {

    public static class ActionEntry {
        String action;
        List<String> arguments;

        public ActionEntry(String action, List<String> arguments) {
            this.action = action;
            this.arguments = arguments;
        }

        public String getAction() {
            return action;
        }

        public List<String> getArguments() {
            return arguments;
        }
    }

    public static FileHandler getFileHandlerForFile(String filePath) {
        try {

            if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
                return new ExcelHandler(filePath);
            }

            if (filePath.endsWith(".csv")) {
                return new CSVHandler(filePath);
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new UnknownActionFileFormatException(filePath);
    }

    public ActionEntry getNextAction() {
        List<String> nextRow = getNextRow();
        if (nextRow == null) {
            return null;
        }

        // Make it mutable
        nextRow = new ArrayList<>(nextRow);

        String action = nextRow.remove(0);

        return new ActionEntry(action, nextRow);
    }

    protected abstract List<String> getNextRow();

}
