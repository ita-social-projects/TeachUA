package com.softserve.teachua.utils.validations;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class CheckRussianValidator implements ConstraintValidator<CheckRussian, String> {
    @Override
    public void initialize(CheckRussian constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        }
        return text.matches("[^ёЁъЪэЭыЫ]+");
    }
}