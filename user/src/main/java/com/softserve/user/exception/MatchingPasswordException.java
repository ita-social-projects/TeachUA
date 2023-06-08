package com.softserve.user.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is custom exception for entity which already exists in Database or other data storage. The constructor
 * accepts message for Exception
 *
 * <p>Use @code throw new AlreadyExistException("Entity already exists")
 *
 * @author Denis Burko
 */
@Slf4j
public class MatchingPasswordException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private static final String PASSWORD_MATCH_EXCEPTION = "Old and new passwords match";

    public MatchingPasswordException(String message) {
        super(message.isEmpty() ? PASSWORD_MATCH_EXCEPTION : message);
    }

    public MatchingPasswordException() {
        super(PASSWORD_MATCH_EXCEPTION);
    }
}
