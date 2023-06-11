package com.softserve.commons.service;

import com.softserve.commons.constant.excel.ExcelColumn;
import com.softserve.commons.dto.ExcelParsingMistake;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    private ExcelUtil() {
    }

    /**
     * The method gets not empty cells from sheet.
     */
    public static List<List<Cell>> excelSheetToList(Sheet sheet) {
        List<List<Cell>> allCells = new ArrayList<>();

        for (Row row : sheet) {
            if (isRowEmpty(row)) {
                continue;
            }
            List<Cell> allRowCells = new ArrayList<>();
            for (Cell currentCell : row) {
                if (!isColumnEmpty(sheet, currentCell.getColumnIndex())) {
                    allRowCells.add(currentCell);
                }
            }
            allCells.add(allRowCells);
        }
        return allCells;
    }

    /**
     * The method checks that row is empty.
     */
    public static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        if (row != null) {
            for (Cell cell : row) {
                if (DATA_FORMATTER.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    /**
     * The method checks that column is empty.
     */
    public static boolean isColumnEmpty(Sheet sheet, int columnIndex) {
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method defines indexes based on enum for first row of sheet.
     */
    public static HashMap<ExcelColumn, Integer> getColumnIndexes(List<Cell> headerRow, ExcelColumn[] excelColumns) {
        HashMap<ExcelColumn, Integer> indexes = new HashMap<>();
        for (Cell cell : headerRow) {
            String lowerCaseCell = DATA_FORMATTER.formatCellValue(cell).toLowerCase();
            for (ExcelColumn column : excelColumns) {
                if (lowerCaseCell.contains(column.getKeyWord())) {
                    indexes.put(column, cell.getColumnIndex());
                }
            }
        }
        return indexes;
    }

    public static List<ExcelParsingMistake> validateColumnsPresent(List<Cell> headerRow, ExcelColumn[] excelColumns,
                                                                   HashMap<ExcelColumn, Integer> indexes) {
        return Arrays.stream(excelColumns)
                .filter(column -> !indexes.containsKey(column))
                .map(column -> new ExcelParsingMistake(column.getMissingMessage(), headerRow.toString(), 1))
                .toList();
    }
}
