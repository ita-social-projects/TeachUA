package com.softserve.teachua.dto.certificateExcel;

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
    public static final String INCORRECT_NAME_FORMAT_ERROR = "Неможливо розпізнати ім'я та прізвище";
    public static final String INCORRECT_DATE_FORMAT_ERROR = "Неможливо розпізнати дату видачі сертифікату";
    public static final String INCORRECT_EMAIL_FORMAT_ERROR = "Неможливо розпізнати електронну адресу";
    private final static String SPACE = " ";
    private final static String DATE_FORMAT = "d.MM.yyyy";
    private final static String NAME_PATTERN = "([А-ЯІЇЄ][а-яіїє']+[ ]*){2}(\\(?([А-ЯІЇЄ][а-яіїє']+)\\)?)?";
    private final static String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final static String WORD = "[А-ЯІЇЄ][а-яіїє']+";

    public void validateHeaders(List<ExcelParsingMistake> mistakes, List<String> headers, int[] indexes) {
        if (indexes[0] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_NAME_ERROR, headers.toString()));
        }
        if (indexes[1] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_DATE_ERROR, headers.toString()));
        }
        if (indexes[2] == -1) {
            mistakes.add(new ExcelParsingMistake(MISSING_COLUMN_EMAIL_ERROR, headers.toString()));
        }
    }
    public String validateName(List<ExcelParsingMistake> mistakes, String name) {
        StringBuilder validatedName = new StringBuilder();
        if (!name.isEmpty() && Pattern.matches(NAME_PATTERN, name)) {
            Pattern pattern = Pattern.compile(WORD);
            Matcher matcher = pattern.matcher(name);
            while (matcher.find()) {
                validatedName.append(matcher.group()).append(SPACE);
            }
        } else {
            mistakes.add(new ExcelParsingMistake(INCORRECT_NAME_FORMAT_ERROR, name));
        }
        return validatedName.toString().trim();
    }

    public String validateEmail(List<ExcelParsingMistake> mistakes, String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            mistakes.add(new ExcelParsingMistake(INCORRECT_EMAIL_FORMAT_ERROR, email));
        }
        return email;
    }

    public LocalDate validateDate(List<ExcelParsingMistake> mistakes, String date) {
        LocalDate localDate = null;
        if (date != null) {
            try {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
                return localDate;
            } catch (DateTimeParseException e) {
                mistakes.add(new ExcelParsingMistake(INCORRECT_DATE_FORMAT_ERROR, date));
            }
        }
        return localDate;
    }

}
