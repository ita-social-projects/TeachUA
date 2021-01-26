package com.softserve.teachua.exception;


public class DatabaseRepositoryException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DATABASE_REPOSITORY_EXCEPTION = "DATABASE REPOSITORY EXCEPTION";

    public DatabaseRepositoryException(String message) {
        super(message.isEmpty() ? DATABASE_REPOSITORY_EXCEPTION : message);
    }

    public DatabaseRepositoryException() {
        super(DATABASE_REPOSITORY_EXCEPTION);
    }
}
