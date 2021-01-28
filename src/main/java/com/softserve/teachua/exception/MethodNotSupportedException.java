package com.softserve.teachua.exception;

/**
 * This class is custom exception for interrupt
 * Spring's MethodNotSupported and adding own message to this exception.
 * The constructor accepts message for Exception
 *
 * Use @code new BadRequestException("Method not supported")
 *
 * @author Denis Burko
 */
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
