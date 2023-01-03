package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateExcel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.softserve.teachua.utils.validations.CertificateUserNameValidator.NAME_PATTERN;

@Slf4j
@Service
public class CertificateExcelServiceImpl implements CertificateExcelService {
    protected static final String FILE_NOT_FOUND_EXCEPTION = "File %s could not be found";
    protected static final String FILE_NOT_READ_EXCEPTION = "File %s could not be read";
    protected static final String FILE_NOT_CLOSE_EXCEPTION = "File %s could not be closed";
    private static final String FILE_LOAD_EXCEPTION = "Could not load excel file";
    private static final String MISSING_HEADER_ROW = "Відсутній рядок з назвами колонок";
    private static final String INCORRECT_DATE_FORMAT_ERROR = "Неможливо розпізнати дату видачі сертифікату";
    private static final String MISSING_COLUMN_NAME_ERROR = "Відсутня колонка з ім'ям та прізвищем";
    private static final String MISSING_COLUMN_DATE_ERROR = "Відсутня колонка з датою видачі сертифікату";
    private static final String MISSING_COLUMN_EMAIL_ERROR = "Відсутня колонка з електронною адресою";
    private final static String EMPTY_STRING = "";
    private final static String SURNAME = "прізвище";
    private final static String DATE = "дата";
    private final static String EMAIL = "електронна";
    private final static String DATE_FORMAT = "[d.MM.yyyy][MM/d/yy][MM/d/yyyy]";
    private final static String WORD = "([А-ЯІЇЄ][а-яіїє']+[-–—]?){1,2}";
    private static final String INVALID_CHARACTERS_PRESENT = "Присутні недопустимі літери";
    private static final String INVALID_CHARS = "\\w";
    private static final String USER_NAME_ERROR = "Неправильний формат ПІБ!";
    private static final String USER_NAME_FORMAT = NAME_PATTERN;
    private static final String DATE_ERROR =
        "Неправильний формат дати. Будь ласка, використовуйте наступний формат: \"dd.mm.yyyy\"!";
    private static final String DATE_FORMAT_REGEX = "[0-9]{2}.[0-9]{2}.[0-9]{4}";
    private static final String HOURS_ERROR = "Неправильний формат кількості годин!";
    private static final String COURSE_NUMBER_ERROR = "Неправильний формат номера курсу!";
    private static final String EMAIL_ERROR = "Неправильний формат електронної пошти!";
    private static final String EMAIL_FORMAT = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}";
    private final DataFormatter dataFormatter = new DataFormatter();
    private final CertificateTemplateService templateService;
    private final CertificateDataLoaderService dataLoaderService;
    private int headerRowIndex = -1;
    private int[] indexes;
    private ExcelParsingResponse response;

    public CertificateExcelServiceImpl(CertificateTemplateService templateService,
                                       CertificateDataLoaderService dataLoaderService) {
        this.templateService = templateService;
        this.dataLoaderService = dataLoaderService;
    }

    @Override
    public ExcelParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelParsingResponse();
        indexes = new int[] {-1, -1, -1};
        response.setCertificatesInfo(createUserCertificates(excelToList(multipartFile, true)));
        return response;
    }

    @Override
    public CertificateByTemplateExcelParsingResponse parseFlexibleExcel(MultipartFile multipartFile) {
        List<List<Cell>> allCells = excelToList(multipartFile, false);

        List<List<String>> allConvertedCells =
            allCells.stream().map(cellList ->
                cellList.stream().map(element -> {
                    String value = "";
                    switch (element.getCellType()) {
                        case STRING:
                            return element.getRichStringCellValue().getString();
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(element)) {
                                return String.valueOf(element.getDateCellValue());
                            } else {
                                return String.valueOf((int) element.getNumericCellValue());
                            }
                        default:
                            return value;
                    }
                }).collect(Collectors.toList())
            ).collect(Collectors.toList());

        return CertificateByTemplateExcelParsingResponse.builder()
            .columnHeadersList(allConvertedCells.remove(0))
            .excelContent(allConvertedCells)
            .build();
    }

    private List<List<Cell>> excelToList(MultipartFile multipartFile, boolean validate) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            List<List<Cell>> allCells = new ArrayList<List<Cell>>();
            XSSFWorkbook workbook;
            Sheet sheet;
            try {
                workbook = new XSSFWorkbook(inputStream);
                sheet = workbook.getSheetAt(0);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(FILE_NOT_FOUND_EXCEPTION);
            } catch (IOException e) {
                throw new RuntimeException(FILE_NOT_READ_EXCEPTION);
            }
            for (Row row : sheet) {
                if (isRowEmpty(row)) {
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                List<Cell> allRowCells = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (!isColumnEmpty(sheet, currentCell.getColumnIndex())) {
                        String cell = dataFormatter.formatCellValue(currentCell);
                        if (validate && (cell.toLowerCase().contains(DATE) || cell.toLowerCase().contains(SURNAME) ||
                            cell.toLowerCase().contains(EMAIL))) {
                            headerRowIndex = allCells.size();
                        }
                        allRowCells.add(currentCell);
                    }
                }
                allCells.add(allRowCells);
            }
            if (validate) {
                if (headerRowIndex == -1) {
                    response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_HEADER_ROW, EMPTY_STRING, null));
                    return allCells;
                } else {
                    setIndexes(allCells.get(headerRowIndex));
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    throw new RuntimeException(FILE_NOT_CLOSE_EXCEPTION);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(FILE_NOT_CLOSE_EXCEPTION);
                }
            }
            return allCells;
        } catch (IOException e) {
            log.error("Upload excel error, " + FILE_LOAD_EXCEPTION);
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
    }

    private CertificateExcel createUserCertificate(List<Cell> row) {
        List<Cell> data = new ArrayList<>(row);
        String name = null;
        LocalDate date = null;
        String email = null;
        long rowIndex = (long) data.get(0).getRowIndex() + 1;
        if (indexes[0] != -1) {
            Pattern pattern = Pattern.compile(INVALID_CHARS);
            Matcher matcher = pattern.matcher(data.get(indexes[0]).toString());
            if (matcher.find()) {
                response.getParsingMistakes().add(
                    new ExcelParsingMistake(INVALID_CHARACTERS_PRESENT, data.get(indexes[0]).toString(), rowIndex));
            }
            name = formUserName(data.get(indexes[0]));
        }
        if (indexes[1] != -1) {
            String stringDate = dataFormatter.formatCellValue(data.get(indexes[1])).trim();
            try {
                date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            } catch (DateTimeParseException e) {
                response.getParsingMistakes()
                    .add(new ExcelParsingMistake(INCORRECT_DATE_FORMAT_ERROR, stringDate, rowIndex));
            }
        }
        if (indexes[2] != -1) {
            email = dataFormatter.formatCellValue(data.get(indexes[2])).trim();
        }
        CertificateExcel certificateExcel = CertificateExcel.builder().name(name).dateIssued(date).email(email).build();
        validateCertificateExcel(certificateExcel, response, rowIndex);
        return certificateExcel;
    }

    private List<CertificateExcel> createUserCertificates(List<List<Cell>> rows) {
        List<CertificateExcel> result = new ArrayList<>();
        if (headerRowIndex != -1) {
            rows.remove(headerRowIndex);
        }
        for (List<Cell> row : rows) {
            result.add(createUserCertificate(row));
        }
        return result;
    }

    private void setIndexes(List<Cell> row) {
        for (int i = 0; i < row.size(); i++) {
            String cell = dataFormatter.formatCellValue(row.get(i)).toLowerCase();
            if (cell.contains(SURNAME)) {
                indexes[0] = i;
            }
            if (cell.contains(DATE)) {
                indexes[1] = i;
            }
            if (cell.contains(EMAIL)) {
                indexes[2] = i;
            }
        }
        if (indexes[0] == -1) {
            response.getParsingMistakes()
                .add(new ExcelParsingMistake(MISSING_COLUMN_NAME_ERROR, row.toString(), (long) headerRowIndex));
        }
        if (indexes[1] == -1) {
            response.getParsingMistakes()
                .add(new ExcelParsingMistake(MISSING_COLUMN_DATE_ERROR, row.toString(), (long) headerRowIndex));
        }
        if (indexes[2] == -1) {
            response.getParsingMistakes()
                .add(new ExcelParsingMistake(MISSING_COLUMN_EMAIL_ERROR, row.toString(), (long) headerRowIndex));
        }
    }

    private boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();
        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    private boolean isColumnEmpty(Sheet sheet, int columnIndex) {
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                return false;
            }
        }
        return true;
    }

    private String formUserName(Cell nameCell) {
        StringBuilder validatedName = new StringBuilder();
        String name = dataFormatter.formatCellValue(nameCell).trim();
        Pattern pattern = Pattern.compile(WORD);
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            validatedName.append(matcher.group()).append(" ");
        }
        return validatedName.toString().trim();
    }

    private void validateCertificateExcel(CertificateExcel certificateExcel, ExcelParsingResponse response,
                                          long rowIndex) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<CertificateExcel>> violations = validator.validate(certificateExcel);
        for (ConstraintViolation<CertificateExcel> violation : violations) {
            response.getParsingMistakes()
                .add(new ExcelParsingMistake(violation.getMessage(), violation.getInvalidValue().toString(), rowIndex));
        }
    }

    @Override
    public List<String[]> validateCertificateByTemplateExcel(CertificateByTemplateTransfer data)
        throws JsonProcessingException {
        List<String[]> resultList = new ArrayList<>();

        CertificateTemplate certificateTemplate = templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties =
            new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);
        HashMap<String, String> values = new ObjectMapper().readValue(data.getValues(), HashMap.class);

        for (int i = 0; i < data.getExcelContent().size(); i++) {
            List<String> excelValues = data.getExcelContent().get(i);

            for (int j = 0; j < data.getFieldsList().size(); j++) {
                String value = dataLoaderService.getCertificateByTemplateValue(values, data.getFieldsList(),
                    data.getColumnHeadersList(), data.getExcelColumnsOrder(), excelValues, data.getFieldsList().get(j));
                String messageDescription =
                    (!values.get(data.getFieldsList().get(j)).isEmpty() ? " Рядок " + (i + 2) + ". " : "") +
                        "Значення \"" + value + "\".";

                if (value.isEmpty()) {
                    resultList.add(new String[] {"Рядок " + (j + 2) + ". " + "Пуста клітинка.", "2"});
                    continue;
                }
                switch (data.getFieldsList().get(j)) {
                    case "Номер курсу":
                        int courseNumber;
                        try {
                            courseNumber = Integer.parseInt(value);
                        } catch (RuntimeException e) {
                            resultList.add(new String[] {COURSE_NUMBER_ERROR + messageDescription, "2"});
                            continue;
                        }
                        if (courseNumber <= 0) {
                            resultList.add(new String[] {COURSE_NUMBER_ERROR + messageDescription, "2"});
                        }
                        continue;
                    case "Електронна пошта":
                        if (!value.matches(EMAIL_FORMAT)) {
                            resultList.add(new String[] {EMAIL_ERROR + messageDescription, "2"});
                        }
                        continue;
                    default:
                        break;
                }
                switch (templateProperties.get(data.getFieldsList().get(j))) {
                    case "course_number":
                        int courseNumber;
                        try {
                            courseNumber = Integer.parseInt(value);
                        } catch (RuntimeException e) {
                            resultList.add(new String[] {COURSE_NUMBER_ERROR + messageDescription, "2"});
                            break;
                        }
                        if (courseNumber <= 0) {
                            resultList.add(new String[] {COURSE_NUMBER_ERROR + messageDescription, "2"});
                        }
                        break;
                    case "user_name":
                        if (!value.matches(USER_NAME_FORMAT)) {
                            resultList.add(new String[] {USER_NAME_ERROR + messageDescription, "1"});
                        }
                        break;
                    case "date":
                        if (!value.matches(DATE_FORMAT_REGEX)) {
                            resultList.add(new String[] {DATE_ERROR + messageDescription, "1"});
                        }
                        break;
                    case "hours":
                        int hours;
                        try {
                            hours = Integer.parseInt(value);
                        } catch (RuntimeException e) {
                            resultList.add(new String[] {HOURS_ERROR + messageDescription, "2"});
                            break;
                        }
                        if (hours <= 0) {
                            resultList.add(new String[] {HOURS_ERROR + messageDescription, "2"});
                        }
                        break;
                    default:
                        break;
                }

            }

        }
        if (resultList.isEmpty()) {
            resultList.add(new String[] {"Валідація пройшла успішно!", "3"});
        }
        return resultList;
    }

}
