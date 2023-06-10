package com.softserve.certificate.exception.handler;

import com.google.api.client.http.HttpResponseException;
import com.softserve.certificate.dto.exception.ExceptionResponse;
import com.softserve.certificate.exception.CertificateGenerationException;
import com.softserve.certificate.exception.GoogleApisDocumentException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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

    @ExceptionHandler(CertificateGenerationException.class)
    public ResponseEntity<Object> handleEntityInUseException(CertificateGenerationException exception) {
        return buildExceptionBody(exception, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GoogleApisDocumentException.class)
    public final ResponseEntity<Object> handleGoogleApisDocumentException(GoogleApisDocumentException exception) {
        return buildExceptionBody(exception, BAD_REQUEST);
    }

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<Object> handleHttpResponseException(HttpResponseException exception) {
        HttpStatus httpStatus = Optional.ofNullable(
                HttpStatus.resolve(exception.getStatusCode())
        ).orElse(BAD_REQUEST);
        return buildExceptionBody(exception, httpStatus);
    }
}
