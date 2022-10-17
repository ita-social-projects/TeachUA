package com.softserve.teachua.exception.test;

import com.softserve.teachua.dto.exception.ExceptionResponse;
import com.softserve.teachua.utils.test.validation.exception.TestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Test exception handler to handle exceptions related to testing.
 */

@Slf4j
@RestControllerAdvice
public class TestSystemExceptionHandler {
    @ExceptionHandler(value = TestValidationException.class)
    public ResponseEntity<?> handleTestValidationException(TestValidationException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getContainer(), exception.getStatus());
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.debug(exception.getMessage());
        Throwable rootCause = exception.getRootCause();
        String message = "";
        if(Objects.nonNull(rootCause)){
            message = rootCause.getMessage();
        }
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }
}
