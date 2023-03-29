package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.CertificateValidator;
import static com.softserve.teachua.utils.validations.CertificateUserNameValidator.NAME_PATTERN;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidatorImpl implements CertificateValidator {
    public static final String DATE_FORMAT_REGEX = "\\d{2}.\\d{2}.\\d{4}";
    public static final String USER_NAME_ERROR = "Неправильний формат ПІБ!";
    public static final String DATE_ERROR =
            "Неправильний формат дати. Будь ласка, використовуйте наступний формат: \"dd.mm.yyyy\"!";
    public static final String HOURS_ERROR = "Неправильний формат кількості годин!";
    public static final String COURSE_NUMBER_ERROR = "Неправильний формат номера курсу!";
    public static final String EMAIL_ERROR = "Неправильний формат електронної пошти!";
    public static final String EMAIL_FORMAT = "[\\w-.]+@([\\w-]+\\.){1,61}+[\\w-]{2,4}";
    public static final String EMPTY_FIELD_ERROR = "Усі поля повинні бути заповнені";

    public boolean validateNaturalNumber(String value, List<String[]> resultList, String errorDescription,
                                         String messageDescription) {
        if (value.trim().isEmpty()) {
            resultList.add(new String[] {EMPTY_FIELD_ERROR, "2"});
            return false;
        }
        int number;
        try {
            number = Integer.parseInt(value);
            if (number <= 0) {
                resultList.add(new String[] {errorDescription + messageDescription, "2"});
                return false;
            }
        } catch (NumberFormatException e) {
            resultList.add(new String[] {errorDescription + messageDescription, "2"});
        }
        return true;
    }

    public boolean validateEmail(String value, List<String[]> resultList, String messageDescription) {
        if (!value.matches(EMAIL_FORMAT)) {
            resultList.add(new String[] {EMAIL_ERROR + messageDescription, "2"});
            return false;
        }
        return true;
    }

    public boolean validateUserName(String value, List<String[]> resultList, String messageDescription) {
        if (!value.matches(NAME_PATTERN)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (value.contains("  ")) {
                stringBuilder.append(" Подвійний пробіл!");
            }
            if (value.trim().length() < value.length()) {
                stringBuilder.append(" ПІБ починається/закінчується пробілом!");
            }
            resultList.add(new String[] {USER_NAME_ERROR + messageDescription + stringBuilder, "1"});
            return false;
        }
        return true;
    }

    public boolean validateDate(String value, List<String[]> resultList, String messageDescription) {
        if (!value.matches(DATE_FORMAT_REGEX)) {
            resultList.add(new String[] {DATE_ERROR + messageDescription, "1"});
            return false;
        }
        return true;
    }
}
