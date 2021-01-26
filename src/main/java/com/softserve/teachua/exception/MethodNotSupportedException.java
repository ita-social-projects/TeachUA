package com.softserve.teachua.exception;

public class MethodNotSupportedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String METHOD_NOT_SUPPORTED = "Method not supported";

    public MethodNotSupportedException(String message) {
        super(message.isEmpty() ? METHOD_NOT_SUPPORTED : message);
    }

    public MethodNotSupportedException() {
        super(METHOD_NOT_SUPPORTED);
    }
}
