package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * The custom exception which say about problem with closing stream.
 */
@Slf4j
public class StreamCloseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String CLOSE_STREAM_EXCEPTION = "Stream wasn`t closed";

    public StreamCloseException() {
        super(CLOSE_STREAM_EXCEPTION);
    }

    public StreamCloseException(String message) {
        super(message.isEmpty() ? CLOSE_STREAM_EXCEPTION : message);
    }
}
