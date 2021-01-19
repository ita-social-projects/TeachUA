package com.softserve.teachua.exception;

public class WrongAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String EMAIL_NOT_EXIST = "EMAIL_NOT_EXIST";

    public WrongAuthenticationException(String message) {
        super(message);
    }

}
