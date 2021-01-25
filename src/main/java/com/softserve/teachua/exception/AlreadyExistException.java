package com.softserve.teachua.exception;

public class AlreadyExistException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    private static final String ALREADY_EXIST_EXCEPTION = "ALREADY EXIST EXCEPTION";

    public AlreadyExistException(String message) {
        super(message.isEmpty() ? ALREADY_EXIST_EXCEPTION : message);
    }
}
