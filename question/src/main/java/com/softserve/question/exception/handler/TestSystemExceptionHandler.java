package com.softserve.question.exception.handler;

import com.google.api.client.http.HttpResponseException;
import com.softserve.commons.exception.dto.ExceptionResponse;
import com.softserve.question.exception.TestValidationException;
import com.softserve.question.util.validation.container.ValidationContainer;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Test exception handler to handle exceptions related to testing.
 */

@Slf4j
@RestControllerAdvice
public class TestSystemExceptionHandler {
    @ExceptionHandler(value = TestValidationException.class)
    public ResponseEntity<ValidationContainer> handleTestValidationException(TestValidationException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getContainer(), exception.getStatus());
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException exception) {
        log.debug(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        log.debug(exception.getMessage());
        Throwable rootCause = exception.getRootCause();
        String message = "";
        if (Objects.nonNull(rootCause)) {
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

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<Object> handleHttpResponseException(HttpResponseException exception) {
        HttpStatus httpStatus = Optional.ofNullable(
                HttpStatus.resolve(exception.getStatusCode())
        ).orElse(BAD_REQUEST);
        return buildExceptionBody(exception, httpStatus);
    }

    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().status(httpStatus.value())
                .message(exception.getMessage()).build();
        log.debug(exception.getMessage());
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }
}
