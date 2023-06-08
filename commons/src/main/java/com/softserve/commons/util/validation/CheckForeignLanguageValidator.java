package com.softserve.commons.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckForeignLanguageValidator implements ConstraintValidator<CheckForeignLanguage, String> {
    @Override
    public void initialize(CheckForeignLanguage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        }
        return text.matches(
                "^[А-ЩЬЮЯҐЄІЇа-щьюяґєіїa-zA-Z0-9 \\n?><,:;—–“”«»\"./{}()\\-_+=!@#$%&*|'’‘`~№\\[\\]^]*$");
    }
}
