package com.softserve.teachua.utils.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CertificateUserNameValidator implements ConstraintValidator<CertificateUserName, String> {

    private static final String EMPTY_EXCEPTION = "Ім'я учасника не може бути порожнім";
    public static final String INCORRECT_NAME_FORMAT_ERROR = "Неможливо розпізнати ім'я та прізвище";
    private final static String NAME_PATTERN = "([А-ЯІЇЄ][а-яіїє']+\\s*){2}(\\(?([А-ЯІЇЄ][а-яіїє']+)\\)?)?";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.trim().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(EMPTY_EXCEPTION);
            return false;
        }
        if (!s.trim().matches(NAME_PATTERN)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(INCORRECT_NAME_FORMAT_ERROR);
            return false;
        }
        return true;
    }
}
