package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is custom exception for problem with authentication.
 * (Invalid password, invalid email etc.)
 * The constructor accepts message for Exception
 *
 * Use @code throw new WrongAuthenticationException("Authentication exception")
 *
 * @author Denis Burko
 */
@Slf4j
public class WrongAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String WRONG_AUTHENTICATION_EXCEPTION = "Wrong authentication exception";

    public WrongAuthenticationException(String message) {
        super(message.isEmpty() ? WRONG_AUTHENTICATION_EXCEPTION : message);
    }

    public WrongAuthenticationException() {
        super(WRONG_AUTHENTICATION_EXCEPTION);
    }
}
