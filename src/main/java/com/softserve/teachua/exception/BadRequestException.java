package com.softserve.teachua.exception;

import org.springframework.lang.Nullable;

/**
 * This class is custom exception for interrupt
 * Spring's BadRequest and adding own message to this exception.
 * The constructor accepts message for Exception
 *
 * Use @code new BadRequestException("Bad request")
 *
 * @author Denis Burko
 */
public class BadRequestException extends IllegalArgumentException {
    public static final String CANT_FIND_FIELD_IN_JSON = "Can't find %s in JSON body";
    public static final String JSON_IS_NOT_READABLE = "JSON is not readable";

    private static final long serialVersionUID = 1L;
    private static final String BAD_REQUEST_EXCEPTION = "Bad request";

    public BadRequestException(@Nullable String message) {
        super((message == null || message.isEmpty()) ? BAD_REQUEST_EXCEPTION : message);
    }

    public BadRequestException() {
        super(BAD_REQUEST_EXCEPTION);
    }
}
