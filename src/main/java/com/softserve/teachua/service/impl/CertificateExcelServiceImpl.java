package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import com.softserve.teachua.dto.certificateExcel.ExcelValidator;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.service.CertificateExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class CertificateExcelServiceImpl implements CertificateExcelService {
    private static final String FILE_LOAD_EXCEPTION = "Could not load excel file";
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
    private final ExcelValidator validator;
    private final DataFormatter dataFormatter = new DataFormatter();

    @Autowired
    public CertificateExcelServiceImpl(ExcelValidator validator) {
        this.validator = validator;
    }

    @Override
    public ExcelParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelParsingResponse();
        indexes = new int[]{-1, -1, -1};
        try (InputStream inputStream = multipartFile.getInputStream()) {
            response.setCertificatesInfo(createUserCertificates(excelToString(inputStream)));
        } catch (IOException e) {
            log.error("Upload excel error, " + FILE_LOAD_EXCEPTION);
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
        return response;
    }

    private List<List<Cell>> excelToString(InputStream inputStream) {
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
        if (indexes[0] != -1) {
            name = validator.validateName(response.getParsingMistakes(), data.get(indexes[0]));
        }
        if (indexes[1] != -1) {
            date = validator.validateDate(response.getParsingMistakes(), data.get(indexes[1]));
        }
        if (indexes[2] != -1) {
            email = validator.validateEmail(response.getParsingMistakes(), data.get(indexes[2]));
        }
        return CertificateExcel.builder()
                    .name(name)
                    .dateIssued(date)
                    .email(email)
                    .build();
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
        List<String> headerRow = cellToString(row);
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
        validator.validateHeaders(response.getParsingMistakes(), headerRow, indexes, headerRowIndex);
    }

    private List<String> cellToString(List<Cell> cells) {
        List<String> row = new ArrayList<>();
        for (Cell cell : cells) {
            row.add(dataFormatter.formatCellValue(cell));
        }
        return row;
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
