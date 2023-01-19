package com.softserve.teachua.exception;

/**
 * {@code GoogleApisDocumentException} exception can be thrown when interacting with the Google API.
 */
public class GoogleApisDocumentException extends RuntimeException {
    public GoogleApisDocumentException(String message) {
        super(message);
    }
}
