package com.softserve.teachua.service;

import static com.softserve.teachua.TestConstants.EMPTY_STRING;
import static com.softserve.teachua.TestConstants.MOCK_MULTIPART_FILE;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestUtils.fillSheet;
import static com.softserve.teachua.TestUtils.getEmptyRow;
import static com.softserve.teachua.TestUtils.getEmptySheet;
import static com.softserve.teachua.TestUtils.getIndexes;
import static com.softserve.teachua.TestUtils.getListOfEmptyCells;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.service.impl.ExcelParserServiceImpl;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.constants.excel.CertificateExcelColumn;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ExcelParserServiceTest {
    @Mock
    private DataFormatter dataFormatter;
    @Spy
    @InjectMocks
    private ExcelParserServiceImpl excelParserService;
    private final DataFormatter cellFormatter = new DataFormatter();

    @Test
    void givenUnprocessedFile_whenGetSheetFromExcelFile_thenThrowResponseStatusException() {
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                excelParserService.getSheetFromExcelFile(MOCK_MULTIPART_FILE));

        assertThat(thrown.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void givenEmptyRow_whenIsRowEmpty_thenReturnTrue() {
        Row row = getEmptyRow(5);
        when(dataFormatter.formatCellValue(any(Cell.class))).thenReturn(EMPTY_STRING);

        assertTrue(excelParserService.isRowEmpty(row));
    }

    @Test
    void givenNotEmptyRow_whenIsRowEmpty_thenReturnFalse() {
        Row row = getEmptyRow(5);
        row.getCell(0).setCellValue(NOT_EMPTY_STRING);
        when(dataFormatter.formatCellValue(any(Cell.class))).thenReturn(NOT_EMPTY_STRING);

        assertFalse(excelParserService.isRowEmpty(row));
    }

    @Test
    void givenEmptyColumnSheet_whenIsColumnEmpty_thenReturnTrue() {
        Sheet sheet = getEmptySheet(5, 5);
        assertTrue(excelParserService.isColumnEmpty(sheet, 0));
    }

    @Test
    void givenNotEmptyColumnSheet_whenIsColumnEmpty_thenReturnFalse() {
        Sheet sheet = getEmptySheet(1, 5);
        sheet.getRow(0).getCell(0).setCellValue(NOT_EMPTY_STRING);
        assertFalse(excelParserService.isColumnEmpty(sheet, 0));
    }

    @Test
    @DisplayName("parseExcel should return correct CertificateExcel after parsing valid row of sheet")
    void excelToList() {
        Sheet sheet = fillSheet(getEmptySheet(1, 10));
        doReturn(sheet)
                .when(excelParserService).getSheetFromExcelFile(MOCK_MULTIPART_FILE);
        when(dataFormatter.formatCellValue(any(Cell.class))).thenReturn(NOT_EMPTY_STRING);

        List<List<Cell>> rows = excelParserService.excelToList(MOCK_MULTIPART_FILE);

        assertEquals(1, rows.size());
        assertEquals(10, rows.get(0).size());
    }

    @Test
    void givenExcelColumns_whenGetColumnIndexes_thenReturnCorrectMap() {
        ExcelColumn[] excelColumns = getExcelColumns();
        HashMap<ExcelColumn, Integer> expected = getIndexes(excelColumns);
        List<Cell> headerRow = getHeaderRow(expected);

        when(dataFormatter.formatCellValue(ArgumentMatchers.any(Cell.class)))
                .then(invocation -> cellFormatter.formatCellValue((Cell) invocation.getArguments()[0]));

        HashMap<ExcelColumn, Integer> actual =
                excelParserService.getColumnIndexes(headerRow, CertificateExcelColumn.values());
        assertEquals(expected, actual);
    }

    @Test
    void givenMapWithAllColumns_whenValidateColumnsPresent_thenReturnEmptyList() {
        ExcelColumn[] excelColumns = getExcelColumns();
        HashMap<ExcelColumn, Integer> indexes = getIndexes(excelColumns);
        List<Cell> headerRow = getHeaderRow(indexes);

        List<ExcelParsingMistake> parsingMistakes =
                excelParserService.validateColumnsPresent(headerRow, excelColumns, indexes);

        assertTrue(parsingMistakes.isEmpty());
    }

    @Test
    void givenMapWithoutColumns_whenValidateColumnsPresent_thenReturnExcelParsingMistakes() {
        ExcelColumn[] excelColumns = getExcelColumns();
        HashMap<ExcelColumn, Integer> emptyIndexes = new HashMap<>();
        List<Cell> headerRow = getHeaderRow(emptyIndexes);

        List<ExcelParsingMistake> parsingMistakes =
                excelParserService.validateColumnsPresent(headerRow, excelColumns, emptyIndexes);

        assertEquals(excelColumns.length, parsingMistakes.size());
    }

    private ExcelColumn[] getExcelColumns() {
        return CertificateExcelColumn.values();
    }

    private List<Cell> getHeaderRow(HashMap<ExcelColumn, Integer> indexes) {
        List<Cell> headerRow = getListOfEmptyCells(getEmptySheet(1, indexes.size())).get(0);
        indexes.keySet().forEach(i -> headerRow.get(indexes.get(i)).setCellValue(i.getKeyWord()));
        return headerRow;
    }
}
