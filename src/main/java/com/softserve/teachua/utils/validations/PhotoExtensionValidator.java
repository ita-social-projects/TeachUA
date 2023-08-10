package com.softserve.teachua.utils.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhotoExtensionValidator implements ConstraintValidator<PhotoExtension, String> {
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (String extension : ALLOWED_EXTENSIONS) {
            if (value.toLowerCase().endsWith(extension)) {
                return true;
            }
        }

        return false;
    }
}
