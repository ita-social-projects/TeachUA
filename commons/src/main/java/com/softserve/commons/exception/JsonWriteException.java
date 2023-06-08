package com.softserve.commons.exception;

/**
 * This class is custom exception for impossibility of converting model to json The constructor accepts message for
 * Exception.
 *
 * <p>Use @code throw new JsonWriteException("son can't be written")
 */
public class JsonWriteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String JSON_WRITE_EXCEPTION = "Json can't be created";

    public JsonWriteException(String message) {
        super(message.isEmpty() ? JSON_WRITE_EXCEPTION : message);
    }

    public JsonWriteException() {
        super(JSON_WRITE_EXCEPTION);
    }
}
