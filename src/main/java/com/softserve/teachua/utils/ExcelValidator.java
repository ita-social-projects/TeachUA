package com.softserve.teachua.utils;

import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExcelValidator {

    public static final String MISSING_COLUMN_NAME_ERROR = "Відсутня колонка з ім'ям та прізвищем";
    public static final String MISSING_COLUMN_DATE_ERROR = "Відсутня колонка з датою видачі сертифікату";
    public static final String MISSING_COLUMN_EMAIL_ERROR = "Відсутня колонка з електронною адресою";

    public void validateHeaders(List<ExcelParsingMistake> mistakes, List<String> headerRow, int[] indexes,
            long headerRowIndex) {
        headerRowIndex += 1;
        if (indexes[0] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_NAME_ERROR, headerRow.toString(), headerRowIndex));
        }
        if (indexes[1] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_DATE_ERROR, headerRow.toString(), headerRowIndex));
        }
        if (indexes[2] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_EMAIL_ERROR, headerRow.toString(), headerRowIndex));
        }
    }
}
