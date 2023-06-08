package com.softserve.commons.exception;

/**
 * This class is custom exception for problem with user permissions. (have no necessary role)
 * The constructor accepts message for Exception
 *
 * <p>Use {@code throw new UserPermissionException("You are not authenticated")}
 */
public class UserPermissionException extends RuntimeException {
    private static final String PERMIT_EXCEPTION = "You have no necessary permissions (role)";

    public UserPermissionException(String message) {
        super(message.isEmpty() ? PERMIT_EXCEPTION : message);
    }

    public UserPermissionException() {
        super(PERMIT_EXCEPTION);
    }
}
