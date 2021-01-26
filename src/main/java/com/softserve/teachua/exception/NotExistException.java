package com.softserve.teachua.exception;

public class NotExistException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private static final String NOT_EXIST_EXCEPTION = "NOT EXIST EXCEPTION";

    public NotExistException(String message) {
        super(message.isEmpty() ? NOT_EXIST_EXCEPTION : message);
    }

    public NotExistException() {
        super(NOT_EXIST_EXCEPTION);
    }
}
