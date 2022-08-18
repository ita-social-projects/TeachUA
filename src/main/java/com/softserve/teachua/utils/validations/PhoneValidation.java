package com.softserve.teachua.utils.validations;

import com.softserve.teachua.exception.IncorrectInputException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PhoneValidation implements ConstraintValidator<Phone, String> {
    private static final String PHONE_NUMBER_REGEX = "^(\\\\+\\\\d{3}( )?)?\n\\\\d{10}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (phoneNumber.length() < 10) {
                throw new IncorrectInputException("Phone number length must be 10");
            }
            if (phoneNumber.matches(".*[a-zA-Z]+.*")) {
                throw new IncorrectInputException("Phone number cannot contain letters.");
            }
            if (phoneNumber.matches(".*[$&+,:;=?@#|'<>.-^*()%!]+.*")) {
                throw new IncorrectInputException("Phone number contains illegal symbols");
            }
            Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
            return matcher.matches();
        } catch (ValidationException e) {
            log.error("An exception occurred!");
        }
        return false;
    }
}
