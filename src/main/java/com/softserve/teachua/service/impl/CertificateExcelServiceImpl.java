package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.MessageType;
import com.softserve.teachua.constants.excel.CertificateExcelColumn;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificate_excel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.certificate_excel.CertificateByTemplateExcelValidationResult;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import com.softserve.teachua.dto.certificate_excel.ExcelParsingResponse;
import com.softserve.teachua.dto.database_transfer.ExcelParsingMistake;
import com.softserve.teachua.exception.BadRequestException;
import com.softserve.teachua.model.CertificateTemplate;
import static com.softserve.teachua.service.CertificateDataLoaderService.getCertificateByTemplateValue;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.CertificateValidator;
import com.softserve.teachua.service.ExcelParserService;
import static com.softserve.teachua.service.impl.CertificateValidatorImpl.COURSE_NUMBER_ERROR;
import static com.softserve.teachua.service.impl.CertificateValidatorImpl.HOURS_ERROR;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CertificateExcelServiceImpl implements CertificateExcelService {
    public static final String FULL_NAME_TEMPLATE_PROPERTY = "user_name";
    private static final String INCORRECT_DATE_FORMAT_ERROR = "Неможливо розпізнати дату видачі сертифікату";
    private static final String DATE_FORMAT = "[d.MM.yyyy][MM/d/yy][MM/d/yyyy]";
    private static final String WORD = "([А-ЯІЇЄ][а-яіїє']+[-–—]?){1,2}";
    private static final String INVALID_CHARACTERS_PRESENT = "Присутні недопустимі літери";
    private static final String INVALID_CHARS = "\\w";
    private final DataFormatter dataFormatter;
    private final CertificateTemplateService templateService;
    private final CertificateValidator certificateValidator;
    private final ExcelParserService excelParserService;
    private final ObjectMapper objectMapper;
    private HashMap<ExcelColumn, Integer> indexes;
    private ExcelParsingResponse response;

    public CertificateExcelServiceImpl(DataFormatter dataFormatter, CertificateTemplateService templateService,
                                       CertificateValidator certificateValidator, ExcelParserService excelParserService,
                                       ObjectMapper objectMapper) {
        this.dataFormatter = dataFormatter;
        this.templateService = templateService;
        this.certificateValidator = certificateValidator;
        this.excelParserService = excelParserService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ExcelParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelParsingResponse();
        List<List<Cell>> rows = excelParserService.excelToList(multipartFile);
        indexes = excelParserService.getColumnIndexes(rows.get(0), CertificateExcelColumn.values());
        response.getParsingMistakes().addAll(
                excelParserService.validateColumnsPresent(rows.get(0), CertificateExcelColumn.values(), indexes));
        if (response.getParsingMistakes().isEmpty()) {
            response.setCertificatesInfo(createUserCertificates(rows));
        }
        return response;
    }

    @Override
    @SuppressWarnings("checkstyle:Indentation") //Suppressed because of unsupported switch style
    public CertificateByTemplateExcelParsingResponse parseFlexibleExcel(MultipartFile multipartFile) {
        List<List<Cell>> allCells = excelParserService.excelToList(multipartFile);
        List<List<String>> allConvertedCells =
                allCells.stream().map(cellList ->
                        cellList.stream().map(element -> {
                            String value = "";
                            switch (element.getCellType()) {
                                case STRING -> {
                                    return element.getRichStringCellValue().getString();
                                }
                                case NUMERIC -> {
                                    if (DateUtil.isCellDateFormatted(element)) {
                                        return String.valueOf(element.getDateCellValue());
                                    } else {
                                        return String.valueOf((int) element.getNumericCellValue());
                                    }
                                }
                                default -> {
                                    return value;
                                }
                            }
                        }).toList()
                ).toList();
        return CertificateByTemplateExcelParsingResponse.builder()
                /*.columnHeadersList(allConvertedCells.remove(0)) */
                .columnHeadersList(allConvertedCells.get(1))
                .excelContent(allConvertedCells)
                .build();
    }

    private CertificateExcel createUserCertificate(List<Cell> row) {
        int rowIndex = row.get(0).getRowIndex() + 1;

        String name = getName(row, rowIndex);
        LocalDate date = getDate(row, rowIndex);
        String email = getEmail(row);

        CertificateExcel certificateExcel = CertificateExcel.builder()
                .name(name)
                .dateIssued(date)
                .email(email)
                .build();
        validateCertificateExcel(certificateExcel, rowIndex);
        return certificateExcel;
    }

    private String getEmail(List<Cell> row) {
        Cell emailCell = row.get(indexes.get(CertificateExcelColumn.EMAIL));
        return dataFormatter.formatCellValue(emailCell).trim();
    }

    private LocalDate getDate(List<Cell> row, int rowIndex) {
        Cell dateCell = row.get(indexes.get(CertificateExcelColumn.DATE));
        String stringDate = dataFormatter.formatCellValue(dateCell).trim();
        LocalDate date = null;
        try {
            date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            response.getParsingMistakes()
                    .add(new ExcelParsingMistake(INCORRECT_DATE_FORMAT_ERROR, stringDate, rowIndex));
        }
        return date;
    }

    private String getName(List<Cell> row, int rowIndex) {
        Cell nameCell = row.get(indexes.get(CertificateExcelColumn.SURNAME));
        Pattern pattern = Pattern.compile(INVALID_CHARS);
        Matcher matcher = pattern.matcher(nameCell.toString());
        if (matcher.find()) {
            response.getParsingMistakes().add(
                    new ExcelParsingMistake(INVALID_CHARACTERS_PRESENT, nameCell.toString(), rowIndex));
        }
        return formUserName(nameCell);
    }

    @Override
    public List<CertificateExcel> createUserCertificates(List<List<Cell>> rows) {
        List<CertificateExcel> result = new ArrayList<>();
        rows.remove(0);
        for (List<Cell> row : rows) {
            result.add(createUserCertificate(row));
        }
        return result;
    }

    private String formUserName(Cell nameCell) {
        StringBuilder validatedName = new StringBuilder();
        String name = dataFormatter.formatCellValue(nameCell).trim();
        Matcher matcher = Pattern.compile(WORD).matcher(name);
        while (matcher.find()) {
            validatedName.append(matcher.group()).append(" ");
        }
        return validatedName.toString().trim();
    }

    private void validateCertificateExcel(CertificateExcel certificateExcel, int rowIndex) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<CertificateExcel>> violations = validator.validate(certificateExcel);
        for (ConstraintViolation<CertificateExcel> violation : violations) {
            response.getParsingMistakes().add(
                    new ExcelParsingMistake(violation.getMessage(), violation.getInvalidValue().toString(), rowIndex));
        }
    }

    @Override
    public CertificateByTemplateExcelValidationResult validateCertificateByTemplateExcel(
            CertificateByTemplateTransfer data) {
        List<Pair<String, MessageType>> resultList = new ArrayList<>();

        CertificateTemplate certificateTemplate = templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties;
        HashMap<String, String> values;
        try {
            templateProperties = objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class);
            values = objectMapper.readValue(data.getValues(), HashMap.class);
        } catch (JsonProcessingException e) {
            log.error("Error json parsing");
            throw new BadRequestException();
        }

        for (int i = 0; i < data.getExcelContent().size(); i++) {
            for (int j = 0; j < data.getFieldsList().size(); j++) {
                String value = getCertificateByTemplateValue(values, data, i, "",
                        data.getFieldsList().get(j));

                if (value.trim().isEmpty()) {
                    String messageDescription = String.format("Рядок %d. Пуста клітинка.", j + 2);
                    resultList.add(Pair.of(messageDescription, MessageType.ERROR));
                } else {
                    String messageDescription = String.format(" Рядок %d. Значення \"%s\".", j + 2, value);

                    if (!validateSpecialProperties(data.getFieldsList().get(j), value, resultList,
                            messageDescription)) {
                        validateCertificateProperties(templateProperties.get(data.getFieldsList().get(j)), value,
                                resultList, messageDescription);
                    }
                }
            }
        }
        if (resultList.isEmpty()) {
            resultList.add(Pair.of("Валідація пройшла успішно!", MessageType.SUCCESS));
        }
        return CertificateByTemplateExcelValidationResult.builder().messages(resultList).build();
    }

    private void validateCertificateProperties(String propertyName, String value,
                                               List<Pair<String, MessageType>> resultList,
                                               String messageDescription) {
        // @formatter:off
        switch (propertyName) {
          case "course_number":
              certificateValidator.validateNaturalNumber(value, resultList, COURSE_NUMBER_ERROR,
                      messageDescription);
              break;
          case FULL_NAME_TEMPLATE_PROPERTY:
              certificateValidator.validateUserName(value, resultList, messageDescription);
              break;
          case "date":
              certificateValidator.validateDate(value, resultList, messageDescription);
              break;
          case "hours":
              certificateValidator.validateNaturalNumber(value, resultList, HOURS_ERROR, messageDescription);
              break;
          default:
              break;
        }
        // @formatter:on
    }

    private boolean validateSpecialProperties(String propertyName, String value,
                                              List<Pair<String, MessageType>> resultList,
                                              String messageDescription) {
        // @formatter:off
        switch (propertyName) {
          case "Номер курсу":
              certificateValidator.validateNaturalNumber(value, resultList, COURSE_NUMBER_ERROR,
                      messageDescription);
              return true;
          case "Електронна пошта":
              certificateValidator.validateEmail(value, resultList, messageDescription);
              return true;
          default:
              return false;
        }
        // @formatter:on
    }

    public byte[] getBadCertificateValuesExcelBytes(String invalidCertificateValues) {
        List<Map<String, String>> invalidValuesList;
        try {
            invalidValuesList = objectMapper.readValue(invalidCertificateValues, List.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing invalid certificate values {}", invalidCertificateValues);
            throw new BadRequestException();
        }
        List<String> columnNames = new ArrayList<>(invalidValuesList.get(0).keySet());
        List<ArrayList<String>> rows = invalidValuesList.stream()
                .map(Map::values)
                .map(ArrayList::new)
                .toList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(new XSSFColor(new Color(250, 140, 22),
                    new DefaultIndexedColorMap()));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < columnNames.size(); i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames.get(i));
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < rows.size(); i++) {
                List<String> rowData = rows.get(i);
                XSSFRow dataRow = sheet.createRow(i + 1);
                for (int j = 0; j < rowData.size(); j++) {
                    XSSFCell cell = dataRow.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }

            for (int i = 0; i < columnNames.size(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 100 * 256));
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Error creating .excel file of bad certificates values");
            throw new BadRequestException();
        }

        return outputStream.toByteArray();
    }
}
