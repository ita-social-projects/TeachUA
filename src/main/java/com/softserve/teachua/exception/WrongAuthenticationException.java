package com.softserve.teachua.exception;

public class WrongAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String WRONG_AUTHENTICATION_EXCEPTION = "Wrong authentication exception";

    public WrongAuthenticationException(String message) {
        super(message.isEmpty() ? WRONG_AUTHENTICATION_EXCEPTION : message);
    }

    public WrongAuthenticationException() {
        super(WRONG_AUTHENTICATION_EXCEPTION);
    }

}
