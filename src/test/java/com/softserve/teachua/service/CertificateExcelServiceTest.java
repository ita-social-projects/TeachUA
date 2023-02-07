package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestConstants.MOCK_MULTIPART_FILE;
import static com.softserve.teachua.TestUtils.*;
import com.softserve.teachua.constants.excel.CertificateExcelColumn;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import com.softserve.teachua.dto.certificate_excel.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.impl.CertificateExcelServiceImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import static org.assertj.core.api.Assertions.assertThat;
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
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateExcelServiceTest {
    public static final String PATTERN = "d.MM.yyyy";
    private final HashMap<ExcelColumn, Integer> columnIndexes = getIndexes(CertificateExcelColumn.values());
    private final DataFormatter cellFormatter = new DataFormatter();
    private final ObjectMapper notMockObjectMapper = new ObjectMapper();
    private final String INVALID_CERTIFICATE_TEMPLATE_PROPERTIES =
            "{\"id\":\"serial_number\",\"fullName\":\"user_name\",\"issueDate\":\"date\",\"countOfHours\":\"hours\","
                    + "\"learningForm\":\"study_form\",\"image\":\"qrCode_white\",\"duration\":\"duration\","
                    + "\"course_number\":\"course_number\"}";
    @Mock
    private DataFormatter dataFormatter;
    @Mock
    private ExcelParserService excelParserService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CertificateTemplateService templateService;
    @Spy
    @InjectMocks
    private CertificateExcelServiceImpl certificateExcelService;
    private CertificateExcel certificateExcel;
    private CertificateByTemplateTransfer certificateTransfer;
    private CertificateTemplate certificateTemplate;

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

    @Test
    void validateCertificateByTemplateExcel_IfValid() throws JsonProcessingException {
        certificateTransfer = getCertificateByTemplateTransfer();
        certificateTemplate = getCertificateTemplate();
        beforeValidateCertificateByTemplateExcel();

        assertThat(certificateExcelService.validateCertificateByTemplateExcel(certificateTransfer).get(0)[1]).isEqualTo(
                "3");
    }

    @Test
    void validateCertificateByTemplateExcel_IfInvalid() throws JsonProcessingException {
        certificateTransfer = getInvalidCertificateByTemplateTransfer();
        certificateTemplate = getCertificateTemplate();
        certificateTemplate.setProperties(INVALID_CERTIFICATE_TEMPLATE_PROPERTIES);
        beforeValidateCertificateByTemplateExcel();

        assertThat(certificateExcelService.validateCertificateByTemplateExcel(certificateTransfer)).hasSizeGreaterThan(
                1);
    }

    private void beforeValidateCertificateByTemplateExcel() throws JsonProcessingException {
        certificateTemplate.setFilePath(FILE_PATH_PDF);
        when(templateService.getTemplateByFilePath(certificateTemplate.getFilePath())).thenReturn(certificateTemplate);
        when(objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTemplate.getProperties(), HashMap.class));
        when(objectMapper.readValue(certificateTransfer.getValues(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTransfer.getValues(), HashMap.class));
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
