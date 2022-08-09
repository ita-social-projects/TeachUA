package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import com.softserve.teachua.dto.certificateExcel.ExcelValidator;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.service.ExcelCertificateService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelCertificateServiceImpl implements ExcelCertificateService {
    protected static final String FILE_NOT_FOUND_EXCEPTION = "File %s could not be found";
    protected static final String FILE_NOT_READ_EXCEPTION = "File %s could not be read";
    protected static final String FILE_NOT_CLOSE_EXCEPTION = "File %s could not be closed";
    private final static String EMPTY_STRING = "";
    private final static String SURNAME = "прізвище";
    private final static String DATE = "дата";
    private final static String EMAIL = "електронна";
    public static final String MISSING_HEADER_ROW = "Відсутній рядок з назвами колонок";
    private int headerRowIndex = -1;
    private int[] indexes;
    private ExcelParsingResponse response;
    private final ExcelValidator validator = new ExcelValidator();
//    private final static String NAME = "ім'я";
//    private final static String INITIALS = "піб";

    @Override
    public ExcelParsingResponse parseExcel(InputStream inputStream) {
        response = new ExcelParsingResponse();
        indexes = new int[]{-1, -1, -1};
        response.setCertificatesInfo(createUserCertificates(excelToString(inputStream)));
        return response;
    }

    private List<List<String>> excelToString(InputStream inputStream) {
        List<List<String>> allCells = new ArrayList<List<String>>();
        DataFormatter dataFormatter = new DataFormatter();
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
            List<String> allRowCells = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell current = cellIterator.next();
                if (!isColumnEmpty(sheet, current.getColumnIndex())) {
                    String cell = dataFormatter.formatCellValue(current);
                    if (cell.toLowerCase().contains(DATE) || cell.toLowerCase().contains(SURNAME) || cell.toLowerCase().contains(EMAIL)) {
                        headerRowIndex = current.getRowIndex();
                    }
                    allRowCells.add(cell);
                }
            }
            System.out.println(allRowCells);
            allCells.add(allRowCells);
        }
        if (headerRowIndex == -1) {
            response.getParsingMistakes().add(new ExcelParsingMistake(MISSING_HEADER_ROW, EMPTY_STRING));
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

    private CertificateExcel createUserCertificate(List<String> row) {
        List<String> data = new ArrayList<>(row);
        String name = null;
        LocalDate date = null;
        String email = null;
        if (indexes[0] != -1) {
            name = validator.validateName(response.getParsingMistakes(), data.get(indexes[0]).trim());
        }
        if (indexes[1] != -1) {
            date = validator.validateDate(response.getParsingMistakes(), data.get(indexes[1]).trim());
        }
        if (indexes[2] != -1) {
            email = validator.validateEmail(response.getParsingMistakes(), data.get(indexes[2]).trim());
        }
        try {
            return CertificateExcel.builder()
                    .name(name)
                    .dateIssued(date)
                    .email(email)
                    .build();
        } catch (IndexOutOfBoundsException ignored) {}
        return new CertificateExcel();
    }

    private List<CertificateExcel> createUserCertificates(List<List<String>> rows) {
        List<CertificateExcel> result = new ArrayList<>();
        if (headerRowIndex != -1) {
            rows.remove(headerRowIndex);
        }
        for (List<String> row : rows) {
            result.add(createUserCertificate(row));
        }
        return result;
    }

    private void setIndexes(List<String> headerRow) {
        for (int i = 0; i < headerRow.size(); i++) {
            if (headerRow.get(i).toLowerCase().contains(SURNAME)) {
                indexes[0] = i;
            }
            if (headerRow.get(i).toLowerCase().contains(DATE)) {
                indexes[1] = i;
            }
            if (headerRow.get(i).toLowerCase().contains(EMAIL)) {
                indexes[2] = i;
            }
        }
        validator.validateHeaders(response.getParsingMistakes(), headerRow, indexes);
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
}
