package com.softserve.teachua.utils.test.validation.exception;

import com.softserve.teachua.utils.test.validation.container.ValidationContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CustomValidationException extends RuntimeException{
    private final HttpStatus status;
    private final ValidationContainer container;

    @Override
    public String getMessage() {
        return container.toString();
    }
}
