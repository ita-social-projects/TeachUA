package com.softserve.teachua.service;

import java.util.List;

/**
 * This interface contains all needed methods to manage logs.
 */

public interface LogService {
    /**
     * The method returns list of logs {@code List<String>}.
     */
    List<String> getAllLogs( String filter);

    /**
     * The method returns list of logs {@code List<String>} by name.
     *
     * @param name - put log name.
     * @return new {@code List<String>}
     */
    List<String> getLogByName(String name);

    /**
     * The method deletes all logs and returns true if successfully deleted.
     */
    Boolean deleteAllLogs();
}
