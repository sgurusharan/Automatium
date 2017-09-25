package com.automatium.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sgurusharan on 9/22/17.
 */
public class ExcelHandler extends FileHandler {

    Iterator<Row> rowIterator;

    public ExcelHandler(String filePath) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        Workbook workbook;

        if (filePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(excelFile);
        }
        else {
            workbook = new HSSFWorkbook(excelFile);
        }

        rowIterator = workbook.getSheetAt(0).rowIterator();
    }

    @Override
    protected List<String> getNextRow() {

        if (!rowIterator.hasNext()) {
            return null;
        }

        List<String> colsList = new ArrayList<>();

        Row nextRow = rowIterator.next();
        Iterator<Cell> cellIterator = nextRow.cellIterator();

        while (cellIterator.hasNext()) {
            Cell nextCell = cellIterator.next();
            colsList.add(nextCell.getStringCellValue());
        }

        return colsList;
    }
}
