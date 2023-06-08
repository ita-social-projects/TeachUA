package com.softserve.user.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This class is custom exception for problem with authentication. (Invalid password, invalid email etc.)
 * The constructor accepts message for Exception
 *
 * <p>Use {@code throw new UserAuthenticationException("You are not authenticated")}
 */
public class UserAuthenticationException extends AuthenticationException {
    private static final String WRONG_AUTHENTICATION_EXCEPTION = "You are not authenticated";

    public UserAuthenticationException(String message) {
        super(message.isEmpty() ? WRONG_AUTHENTICATION_EXCEPTION : message);
    }

    public UserAuthenticationException() {
        super(WRONG_AUTHENTICATION_EXCEPTION);
    }
}
