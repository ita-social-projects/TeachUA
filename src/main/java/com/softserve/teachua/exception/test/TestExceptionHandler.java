package com.softserve.teachua.exception.test;

import com.softserve.teachua.utils.test.validation.exception.TestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Test exception handler to handle exceptions related to testing.
 */

@Slf4j
@RestControllerAdvice
public class TestExceptionHandler {
    @ExceptionHandler(value = TestValidationException.class)
    public ResponseEntity<?> handleTestValidationException(TestValidationException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getContainer(), exception.getStatus());
    }
}
