package com.softserve.teachua.exception.handler;

import com.softserve.teachua.dto.exception.ExceptionResponse;
import com.softserve.teachua.exception.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;


/**
 * Custom exception handler to handle own exceptions
 * and handle Spring's exceptions(BadRequest, MethodNotSupported).
 *
 * Use @code buildExceptionBody(Exception exception, HttpStatus status) to create
 * own exception body.
 *
 * @author Denis Burko
 */
@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(WrongAuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(WrongAuthenticationException exception) {
        return buildExceptionBody(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(NotExistException.class)
    public final ResponseEntity<Object> handleNotExistException(NotExistException exception) {
        return buildExceptionBody(exception, NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public final ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException exception) {
        return buildExceptionBody(exception, FORBIDDEN);
    }

    @ExceptionHandler(DatabaseRepositoryException.class)
    public final ResponseEntity<Object> handleDatabaseRepositoryException(DatabaseRepositoryException exception) {
        return buildExceptionBody(exception, BAD_GATEWAY);
    }

    @ExceptionHandler(JsonWriteException.class)
    public final ResponseEntity<Object> handleJsonWriteException(JsonWriteException exception) {
        return buildExceptionBody(exception, CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String exceptionMessage = exception.getMessage();
        String message = String.format(BadRequestException.CANT_FIND_FIELD_IN_JSON,
                exceptionMessage.substring(exceptionMessage.indexOf("field"), exceptionMessage.indexOf("':") + 1));
        return buildExceptionBody(new BadRequestException(message), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildExceptionBody(new BadRequestException(BadRequestException.JSON_IS_NOT_READABLE), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildExceptionBody(new MethodNotSupportedException(exception.getMessage()), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildExceptionBody(new BadRequestException(exception.getMessage()), status);
    }

    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .build();
        log.warn(exception.getMessage());
        return ResponseEntity
                .status(httpStatus)
                .body(exceptionResponse);
    }

}
