package com.softserve.teachua.exception;

import org.springframework.lang.Nullable;

public class RestoreArchiveException extends IllegalStateException {
    private static final String PROGRAM_WAS_CHANGED = "(the structure of the program was changed)";
    public static final String CANT_FIND_CLASS = "Can't find serviceClass " + PROGRAM_WAS_CHANGED;
    public static final String CANT_READ_JSON = "JSON is not readable " + PROGRAM_WAS_CHANGED;
    public static final String CANT_WRITE_JSON = "Can't create JSON of model";

    private static final long serialVersionUID = 1L;
    private static final String RESTORE_ARCHIVE_EXCEPTION = "Can't restore model " + PROGRAM_WAS_CHANGED;

    public RestoreArchiveException(String message) {
        super(message.isEmpty() ? RESTORE_ARCHIVE_EXCEPTION : message);
    }

    public RestoreArchiveException() {
        super(RESTORE_ARCHIVE_EXCEPTION);
    }
}
