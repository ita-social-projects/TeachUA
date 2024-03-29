package com.softserve.teachua.service;

import com.softserve.teachua.dto.database_transfer.ExcelParsingMistake;
import com.softserve.teachua.dto.database_transfer.ExcelParsingResponse;
import com.softserve.teachua.constants.excel.ExcelColumn;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface contains all needed methods to manage exel parser.
 */
public interface ExcelParserService {
    /**
     * The method parses Excel file for initial db.
     *
     * @return new {@code ExcelParsingResponse}.
     */
    ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException;

    /**
     * The method gets sheet from Excel file.
     *
     * @return new {@code Sheet}.
     */
    Sheet getSheetFromExcelFile(MultipartFile multipartFile);

    /**
     * The method checks that row is empty.
     */
    boolean isRowEmpty(Row row);

    /**
     * The method checks that column is empty.
     */
    boolean isColumnEmpty(Sheet sheet, int columnIndex);

    /**
     * The method gets not empty cells from sheet.
     */
    List<List<Cell>> excelToList(MultipartFile multipartFile);

    /**
     * The method defines indexes based on enum for first row of sheet.
     */
    HashMap<ExcelColumn, Integer> getColumnIndexes(List<Cell> row, ExcelColumn[] excelColumns);

    List<ExcelParsingMistake> validateColumnsPresent(List<Cell> row, ExcelColumn[] excelColumns,
                                                     HashMap<ExcelColumn, Integer> indexes);
}
