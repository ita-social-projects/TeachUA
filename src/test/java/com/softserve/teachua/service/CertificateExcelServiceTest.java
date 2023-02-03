package com.softserve.teachua.service;

import static com.softserve.teachua.TestConstants.MOCK_MULTIPART_FILE;
import static com.softserve.teachua.TestUtils.getCertificateExcel;
import static com.softserve.teachua.TestUtils.getEmptyCell;
import static com.softserve.teachua.TestUtils.getEmptySheet;
import static com.softserve.teachua.TestUtils.getIndexes;
import static com.softserve.teachua.TestUtils.getListOfEmptyCells;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.service.impl.CertificateExcelServiceImpl;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.constants.excel.CertificateExcelColumn;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateExcelServiceTest {
    public static final String PATTERN = "d.MM.yyyy";
    @Mock
    private DataFormatter dataFormatter;
    @Mock
    private ExcelParserService excelParserService;
    @Spy
    @InjectMocks
    private CertificateExcelServiceImpl certificateExcelService;
    private final HashMap<ExcelColumn, Integer> columnIndexes = getIndexes(CertificateExcelColumn.values());
    private final DataFormatter cellFormatter = new DataFormatter();
    private CertificateExcel certificateExcel;

    @BeforeEach
    void setUp() {
        certificateExcel = getCertificateExcel();
    }

    @Test
    @DisplayName("parseExcel should return ParsingMistakes with mistakes without setting CertificateExcel")
    void givenParsingMistakes_whenParseExcel_thenDoNotSetCertificatesInfo() {
        when(excelParserService.excelToList(MOCK_MULTIPART_FILE))
                .thenReturn(singletonList(singletonList(getEmptyCell())));
        when(excelParserService.getColumnIndexes(anyList(), eq(CertificateExcelColumn.values())))
                .thenReturn(columnIndexes);
        when(excelParserService
                .validateColumnsPresent(anyList(), eq(CertificateExcelColumn.values()), eq(columnIndexes)))
                .thenReturn(singletonList(new ExcelParsingMistake()));

        ExcelParsingResponse excelParsingResponse = certificateExcelService.parseExcel(MOCK_MULTIPART_FILE);

        verify(certificateExcelService, never()).createUserCertificates(anyList());
        assertTrue(excelParsingResponse.getCertificatesInfo().isEmpty());
    }

    @Test
    @DisplayName("parseExcel should return correct CertificateExcel after parsing valid row of sheet")
    void givenValidRow_whenParseExcel_thenReturnCertificateExcel() {
        when(excelParserService.excelToList(MOCK_MULTIPART_FILE))
                .thenReturn(getValidRowCertificateExcel());
        when(excelParserService.getColumnIndexes(anyList(), eq(CertificateExcelColumn.values())))
                .thenReturn(columnIndexes);
        when(excelParserService
                .validateColumnsPresent(anyList(), eq(CertificateExcelColumn.values()), eq(columnIndexes)))
                .thenReturn(emptyList());
        when(dataFormatter.formatCellValue(ArgumentMatchers.any(Cell.class)))
                .then(invocation -> cellFormatter.formatCellValue((Cell) invocation.getArguments()[0]));

        ExcelParsingResponse excelParsingResponse = certificateExcelService.parseExcel(MOCK_MULTIPART_FILE);

        assertEquals(singletonList(certificateExcel), excelParsingResponse.getCertificatesInfo());
        assertTrue(excelParsingResponse.getParsingMistakes().isEmpty());
    }

    @Test
    @DisplayName("parseExcel should return ExcelParsingMistakes for each invalid cell of row")
    void givenInvalidRow_whenParseExcel_thenReturnExcelParsingMistakes() {
        when(excelParserService.excelToList(MOCK_MULTIPART_FILE))
                .thenReturn(getInvalidRowCertificateExcel());
        when(excelParserService.getColumnIndexes(anyList(), eq(CertificateExcelColumn.values())))
                .thenReturn(columnIndexes);
        when(excelParserService
                .validateColumnsPresent(anyList(), eq(CertificateExcelColumn.values()), eq(columnIndexes)))
                .thenReturn(emptyList());
        when(dataFormatter.formatCellValue(ArgumentMatchers.any(Cell.class)))
                .then(invocation -> cellFormatter.formatCellValue((Cell) invocation.getArguments()[0]));

        ExcelParsingResponse excelParsingResponse = certificateExcelService.parseExcel(MOCK_MULTIPART_FILE);

        assertEquals(4, excelParsingResponse.getParsingMistakes().size());
    }

    private List<List<Cell>> getValidRowCertificateExcel() {
        List<List<Cell>> rows =
                getListOfEmptyCells(getEmptySheet(2, CertificateExcelColumn.values().length));
        List<Cell> row = rows.get(1);
        row.get(CertificateExcelColumn.SURNAME.ordinal()).setCellValue(certificateExcel.getName());
        row.get(CertificateExcelColumn.EMAIL.ordinal()).setCellValue(certificateExcel.getEmail());
        row.get(CertificateExcelColumn.DATE.ordinal())
                .setCellValue(certificateExcel.getDateIssued().format(DateTimeFormatter.ofPattern(PATTERN)));
        return rows;
    }

    private List<List<Cell>> getInvalidRowCertificateExcel() {
        List<List<Cell>> rows =
                getListOfEmptyCells(getEmptySheet(2, CertificateExcelColumn.values().length));
        List<Cell> row = rows.get(1);
        row.get(CertificateExcelColumn.SURNAME.ordinal()).setCellValue("invalidNAME&");
        row.get(CertificateExcelColumn.EMAIL.ordinal()).setCellValue("noEmail(((");
        row.get(CertificateExcelColumn.DATE.ordinal()).setCellValue(LocalDate.MIN);
        return rows;
    }
}