package com.softserve.commons.exception;

/**
 * This class is custom exception for entity which not exists in Database or other data storage. The constructor accepts
 * message for Exception
 *
 * <p>Use @code throw new NotExistException("Entity isn't exist")
 *
 * @author Denis Burko
 */
public class NotExistException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private static final String NOT_EXIST_EXCEPTION = "Not exist";

    public NotExistException(String message) {
        super(message.isEmpty() ? NOT_EXIST_EXCEPTION : message);
    }

    public NotExistException() {
        super(NOT_EXIST_EXCEPTION);
    }
}
