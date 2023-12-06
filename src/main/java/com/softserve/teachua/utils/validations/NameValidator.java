package com.softserve.teachua.utils.validations;

import com.softserve.teachua.exception.IncorrectInputException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

@Slf4j
public class NameValidator implements ConstraintValidator<Name, String> {
    private static final String NULL_EXCEPTION = "\"%s\" can`t be null";
    private static final String EMPTY_EXCEPTION = "\"%s\" can`t be empty";
    private static final String LENGTH_EXCEPTION = "\"%s\" can contain from 1 to 25 letters";
    private static final String RUSSIAN_EXCEPTION = "\"%s\" can`t contain russian letters";
    private static final String NUMBERS_EXCEPTION = "\"%s\" can`t contain numbers";
    private static final String CAN_CONTAIN_TEXT = "\"%s\" can contain only ukrainian and english letters";

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        String fieldName = ((ConstraintValidatorContextImpl) constraintValidatorContext)
                .getConstraintViolationCreationContexts().get(0).getPath().toString();

        if (text == null) {
            throw new IncorrectInputException(String.format(NULL_EXCEPTION, fieldName));
        }
        if (text.trim().isEmpty()) {
            throw new IncorrectInputException(String.format(EMPTY_EXCEPTION, fieldName));
        }
        if (text.length() < 1 || text.length() > 25) {
            throw new IncorrectInputException(String.format(LENGTH_EXCEPTION, fieldName));
        }
        if (!text.matches("[^ЁёЪъЫыЭэ]+")) {
            throw new IncorrectInputException(String.format(RUSSIAN_EXCEPTION, fieldName));
        }
        if (!text.matches("\\D+")) {
            throw new IncorrectInputException(String.format(NUMBERS_EXCEPTION, fieldName));
        }
        if (!text.matches("[a-zA-Zа-яА-ЯіІєЄїЇґҐ]+")) {
            throw new IncorrectInputException(String.format(CAN_CONTAIN_TEXT, fieldName));
        }
        return true;
    }
}
