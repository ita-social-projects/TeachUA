package com.softserve.teachua.utils;

import com.softserve.teachua.dto.certificateExcel.ExcelParsingMistake;
import java.util.List;
import org.springframework.stereotype.Component;

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
