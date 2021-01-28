package com.softserve.teachua.exception;

import com.sun.istack.Nullable;

/**
 * This class is custom exception for every Database repository problem.
 * (Database isn't available, can't save entity to Database etc.)
 * The constructor accepts message for Exception
 *
 * Use @code throw new DatabaseRepositoryException("Some problem with Database")
 *
 * @author Denis Burko
 */
public class DatabaseRepositoryException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DATABASE_REPOSITORY_EXCEPTION = "Database repository";

    public DatabaseRepositoryException(@Nullable String message) {
        super(message.isEmpty() ? DATABASE_REPOSITORY_EXCEPTION : message);
    }

    public DatabaseRepositoryException() {
        super(DATABASE_REPOSITORY_EXCEPTION);
    }
}

