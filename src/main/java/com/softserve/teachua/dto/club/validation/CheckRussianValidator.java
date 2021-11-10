package com.softserve.teachua.dto.club.validation;

import com.softserve.teachua.exception.IncorrectInputException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class CheckRussianValidator implements ConstraintValidator<CheckRussian,String> {

    private String message;



    @Override
    public void initialize(CheckRussian constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {

        if (text == null){
            return true;
        }

        if (text.isEmpty()){
            throw new IncorrectInputException("Text cannot be empty or null");
        }

        if (!text.matches("[^ёЁъЪэЭыЫ]+")){
            throw new IncorrectInputException(message);
        }
        if (!text.matches("[a-zA-Zа-яА-Я0-9іІїЇєЄґҐ`@~#$%^&*()\\-_=+\\\\!\\[\\]{};:'\",<>.\\s]+")){
            throw  new IncorrectInputException("You can use Ukrainian and English alphabet and some special symbols");
        }
        log.debug("Checked validation on russian symbols");
        return true;
    }
}