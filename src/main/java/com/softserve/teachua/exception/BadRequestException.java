package com.softserve.teachua.exception;

public class BadRequestException extends IllegalArgumentException {
    public static final String CANT_FIND_FIELD_IN_JSON = "Can't find %s in JSON body";
    public static final String JSON_IS_NOT_READABLE = "JSON is not readable";

    private static final long serialVersionUID = 1L;
    private static final String BAD_REQUEST_EXCEPTION = "Bad request";

    public BadRequestException(String message) {
        super(message.isEmpty() ? BAD_REQUEST_EXCEPTION : message);
    }

    public BadRequestException() {
        super(BAD_REQUEST_EXCEPTION);
    }
}
