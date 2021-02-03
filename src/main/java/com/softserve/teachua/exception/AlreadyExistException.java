package com.softserve.teachua.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is custom exception for entity which already
 * exists in Database or other data storage.
 * The constructor accepts message for Exception
 *
 * Use @code throw new AlreadyExistException("Entity already exists")
 *
 * @author Denis Burko
 */
@Slf4j
public class AlreadyExistException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    private static final String ALREADY_EXIST_EXCEPTION = "Already exist";

    public AlreadyExistException(String message) {
        super(message.isEmpty() ? ALREADY_EXIST_EXCEPTION : message);
    }

    public AlreadyExistException() {
        super(ALREADY_EXIST_EXCEPTION);
    }
}
