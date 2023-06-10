package com.softserve.question.exception;

import com.softserve.question.util.validation.container.ValidationContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TestValidationException extends RuntimeException {
    private final HttpStatus status;
    private final transient ValidationContainer container;

    @Override
    public String getMessage() {
        return container.toString();
    }
}
