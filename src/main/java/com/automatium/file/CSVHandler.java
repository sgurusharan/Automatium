package com.automatium.file;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sgurusharan on 9/22/17.
 */
public class CSVHandler extends FileHandler {

    Iterator<String[]> rowIterator;
    CSVReader csvReader;

    public CSVHandler(String filePath) throws IOException {
        csvReader = new CSVReader(new FileReader(filePath));
        rowIterator = csvReader.iterator();
    }

    @Override
    protected List<String> getNextRow() {

        if (!rowIterator.hasNext()) {
            return null;
        }

        String[] cellArray = rowIterator.next();

        return Arrays.asList(cellArray);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        csvReader.close();
    }
}
