package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * This class is custom exception to check problems with user verification after registration
 * <p>
 * The constructor accepts message for Exception
 * <p>
 * Use @code throw new NotVerifiedUserException("Authentication exception")
 */
@Slf4j
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotVerifiedUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String NOT_VERIFIED_USER_EXCEPTION = "Not verified user exception";

    public NotVerifiedUserException(String message) {
        super(message.isEmpty() ? NOT_VERIFIED_USER_EXCEPTION : message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public NotVerifiedUserException() {
        super(NOT_VERIFIED_USER_EXCEPTION);
    }
}
