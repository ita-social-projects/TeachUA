package com.softserve.teachua.exception;


import org.springframework.lang.Nullable;

/**
 * This class is custom exception for interrupt Spring's BadRequest and adding own message to this exception. The
 * constructor accepts message for Exception
 *
 * <p>Use @code new BadRequestException("Bad request")
 *
 * @author Denis Burko
 */
public class BadRequestException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    private static final String BAD_REQUEST_EXCEPTION = "Bad request";

    public BadRequestException(@Nullable String message) {
        super((message == null || message.isEmpty()) ? BAD_REQUEST_EXCEPTION : message);
    }

    public BadRequestException() {
        super(BAD_REQUEST_EXCEPTION);
    }
}
