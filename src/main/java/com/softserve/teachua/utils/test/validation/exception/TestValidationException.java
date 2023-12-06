package com.softserve.teachua.utils.test.validation.exception;

import com.softserve.teachua.utils.test.validation.container.ValidationContainer;
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
