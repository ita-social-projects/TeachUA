package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

/**
 * This class is custom exception for problem with authentication. (Invalid password, invalid email etc.)
 * The constructor accepts message for Exception
 *
 * <p>Use {@code throw new UnauthorizedException("Authentication exception")}
 *
 * @author Denis Burko
 */
@Slf4j
public class UnauthorizedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;
    private static final String WRONG_AUTHENTICATION_EXCEPTION = "You are not authenticated";

    public UnauthorizedException(String message) {
        super(message.isEmpty() ? WRONG_AUTHENTICATION_EXCEPTION : message);
    }

    public UnauthorizedException() {
        super(WRONG_AUTHENTICATION_EXCEPTION);
    }
}
