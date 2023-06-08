package com.softserve.teachua.exception.handler;

import com.google.api.client.http.HttpResponseException;
import com.softserve.commons.exception.BadRequestException;
import com.softserve.commons.exception.EntityIsUsedException;
import com.softserve.commons.exception.JsonWriteException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.dto.exception.ExceptionResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.CannotDeleteFileException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.FileUploadException;
import com.softserve.teachua.exception.GoogleApisDocumentException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.LogNotFoundException;
import com.softserve.teachua.exception.MatchingPasswordException;
import com.softserve.teachua.exception.RestoreArchiveException;
import com.softserve.teachua.exception.StreamCloseException;
import com.softserve.teachua.exception.UpdatePasswordException;
import com.softserve.teachua.exception.UserPermissionException;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom exception handler to handle own exceptions and handle Spring's exceptions(BadRequest, MethodNotSupported).
 * Use {@code buildExceptionBody(Exception exception, HttpStatus status)} to create own exception body.
 *
 * @author Denis Burko
 */
@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        return buildExceptionBody(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(UserPermissionException.class)
    public final ResponseEntity<Object> handleUserPermissionException(UserPermissionException exception) {
        return buildExceptionBody(exception, FORBIDDEN);
    }

    @ExceptionHandler(NotExistException.class)
    public final ResponseEntity<Object> handleNotExistException(NotExistException exception) {
        return buildExceptionBody(exception, NOT_FOUND);
    }

    @ExceptionHandler(IncorrectInputException.class)
    public final ResponseEntity<Object> handleIncorrectInputException(IncorrectInputException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public final ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException exception) {
        return buildExceptionBody(exception, CONFLICT);
    }

    @ExceptionHandler(UpdatePasswordException.class)
    public final ResponseEntity<Object> handleUpdatePasswordException(UpdatePasswordException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseRepositoryException.class)
    public final ResponseEntity<Object> handleDatabaseRepositoryException(DatabaseRepositoryException exception) {
        return buildExceptionBody(exception, BAD_GATEWAY);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleDatabaseRepositoryException(BadRequestException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(JsonWriteException.class)
    public final ResponseEntity<Object> handleJsonWriteException(JsonWriteException exception) {
        return buildExceptionBody(exception, CONFLICT);
    }

    @ExceptionHandler(FileUploadException.class)
    public final ResponseEntity<Object> handleFileUploadException(JsonWriteException exception) {
        return buildExceptionBody(exception, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MatchingPasswordException.class)
    public final ResponseEntity<Object> handleMatchingPasswordException(IllegalArgumentException exception) {
        return buildExceptionBody(exception, NOT_FOUND);
    }

    @ExceptionHandler(RestoreArchiveException.class)
    public final ResponseEntity<Object> handleRestoreArchiveException(RestoreArchiveException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(StreamCloseException.class)
    public final ResponseEntity<Object> handleStreamCloseException(StreamCloseException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().status(httpStatus.value())
                .message(exception.getMessage()).build();
        log.debug(exception.getMessage());
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(GoogleApisDocumentException.class)
    public final ResponseEntity<Object> handleGoogleApisDocumentException(GoogleApisDocumentException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(LogNotFoundException.class)
    protected ResponseEntity<Object> handleLogNotFoundException(LogNotFoundException exception) {
        return buildExceptionBody(exception, NOT_FOUND);
    }

    @ExceptionHandler(CannotDeleteFileException.class)
    protected ResponseEntity<Object> handleCannotDeleteFileException(CannotDeleteFileException exception) {
        return buildExceptionBody(exception, CONFLICT);
    }

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<Object> handleHttpResponseException(HttpResponseException exception) {
        HttpStatus httpStatus = Optional.ofNullable(
                HttpStatus.resolve(exception.getStatusCode())
        ).orElse(BAD_REQUEST);
        return buildExceptionBody(exception, httpStatus);
    }

    @ExceptionHandler(EntityIsUsedException.class)
    public ResponseEntity<Object> handleEntityInUseException(EntityIsUsedException exception) {
        return buildExceptionBody(exception, CONFLICT);
    }
}
