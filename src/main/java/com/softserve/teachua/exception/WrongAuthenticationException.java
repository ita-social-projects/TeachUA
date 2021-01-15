package com.softserve.teachua.exception;

public class WrongAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongAuthenticationException(String message) {
		super(message);
	}
	
}
