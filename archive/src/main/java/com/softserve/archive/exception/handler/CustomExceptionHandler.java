package com.softserve.archive.exception.handler;

import com.softserve.commons.exception.dto.ExceptionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().status(httpStatus.value())
                .message(exception.getMessage()).build();
        log.debug(exception.getMessage());
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<Object> handleIncorrectResultSizeDataAccessException(
            IncorrectResultSizeDataAccessException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }
}
