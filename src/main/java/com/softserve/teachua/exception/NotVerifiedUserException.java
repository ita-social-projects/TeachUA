package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is custom exception to check problems with user verification after registration.
 *
 * <p>The constructor accepts message for Exception
 *
 * <p>Use @code throw new NotVerifiedUserException("User is not verified")
 */
@Slf4j
public class NotVerifiedUserException extends UserPermissionException {
    private static final long serialVersionUID = 1L;
    private static final String NOT_VERIFIED_USER_EXCEPTION = "User is not verified";

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
