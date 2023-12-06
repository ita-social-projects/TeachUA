package com.softserve.teachua.utils.validations;

import com.softserve.teachua.exception.IncorrectInputException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneValidation implements ConstraintValidator<Phone, String> {
    private static final String PHONE_NUMBER_REGEX = "^(\\\\+\\\\d{3}( )?)?\n\\\\d{10}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber.length() < 10) {
            throw new IncorrectInputException("Phone number length must be 10");
        }
        if (phoneNumber.matches(".*\\w+.*")) {
            throw new IncorrectInputException("Phone number cannot contain letters.");
        }
        if (phoneNumber.matches(".*\\W+.*")) {
            throw new IncorrectInputException("Phone number contains illegal symbols");
        }
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
}
