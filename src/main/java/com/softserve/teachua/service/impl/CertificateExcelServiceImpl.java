package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.service.CertificateExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CertificateExcelServiceImpl implements CertificateExcelService {
    private static final String FILE_LOAD_EXCEPTION = "Could not load excel file";
    protected static final String FILE_NOT_FOUND_EXCEPTION = "File %s could not be found";
    protected static final String FILE_NOT_READ_EXCEPTION = "File %s could not be read";
    protected static final String FILE_NOT_CLOSE_EXCEPTION = "File %s could not be closed";
    public static final String MISSING_HEADER_ROW = "Відсутній рядок з назвами колонок";
    public static final String INCORRECT_DATE_FORMAT_ERROR = "Неможливо розпізнати дату видачі сертифікату";
    public static final String MISSING_COLUMN_NAME_ERROR = "Відсутня колонка з ім'ям та прізвищем";
    public static final String MISSING_COLUMN_DATE_ERROR = "Відсутня колонка з датою видачі сертифікату";
    public static final String MISSING_COLUMN_EMAIL_ERROR = "Відсутня колонка з електронною адресою";
    private final static String EMPTY_STRING = "";
    private final static String SURNAME = "прізвище";
    private final static String DATE = "дата";
    private final static String EMAIL = "електронна";
    private final static String DATE_FORMAT = "d.MM.yyyy";
    private final static String WORD = "[А-ЯІЇЄ][а-яіїє']+";
    private int headerRowIndex = -1;
    private int[] indexes;
    private ExcelParsingResponse response;
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    public ExcelParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelParsingResponse();
        indexes = new int[]{-1, -1, -1};
        try (InputStream inputStream = multipartFile.getInputStream()) {
            response.setCertificatesInfo(createUserCertificates(excelToList(inputStream)));
        } catch (IOException e) {
            log.error("Upload excel error, " + FILE_LOAD_EXCEPTION);
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
        return response;
    }

    private List<List<Cell>> excelToList(InputStream inputStream) {
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
                    if (cell.toLowerCase().contains(DATE) || cell.toLowerCase().contains(SURNAME) || cell.toLowerCase().contains(EMAIL)) {
                        headerRowIndex = allCells.size();
                    }
                    allRowCells.add(currentCell);
                }
            }
            allCells.add(allRowCells);
        }
        if (headerRowIndex == -1) {
            response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_HEADER_ROW, EMPTY_STRING, null));
            return allCells;
        } else {
            setIndexes(allCells.get(headerRowIndex));
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
    }

    private CertificateExcel createUserCertificate(List<Cell> row) {
        List<Cell> data = new ArrayList<>(row);
        String name = null;
        LocalDate date = null;
        String email = null;
        long rowIndex = (long) data.get(0).getRowIndex() + 1;
        if (indexes[0] != -1) {
            name = formUserName(data.get(indexes[0]));
        }
        if (indexes[1] != -1) {
            String stringDate = dataFormatter.formatCellValue(data.get(indexes[1])).trim();
            try {
                date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            } catch (DateTimeParseException e) {
                response.getParsingMistakes().add(new ExcelParsingMistake(INCORRECT_DATE_FORMAT_ERROR, stringDate, rowIndex));
            }
        }
        if (indexes[2] != -1) {
            email = dataFormatter.formatCellValue(data.get(indexes[2])).trim();
        }
        CertificateExcel certificateExcel = CertificateExcel.builder()
                .name(name)
                .dateIssued(date)
                .email(email)
                .build();
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
            response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_COLUMN_NAME_ERROR, row.toString(), (long) headerRowIndex));
        }
        if (indexes[1] == -1) {
            response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_COLUMN_DATE_ERROR, row.toString(), (long) headerRowIndex));
        }
        if (indexes[2] == -1) {
            response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_COLUMN_EMAIL_ERROR, row.toString(), (long) headerRowIndex));
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

    private void validateCertificateExcel(CertificateExcel certificateExcel, ExcelParsingResponse response, long rowIndex) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<CertificateExcel>> violations = validator.validate(certificateExcel);
        for (ConstraintViolation<CertificateExcel> violation : violations) {
            response.getParsingMistakes().add(new ExcelParsingMistake(violation.getMessage(), violation.getInvalidValue().toString(), rowIndex));
        }
    }
}
