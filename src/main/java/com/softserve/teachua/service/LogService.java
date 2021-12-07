package com.softserve.teachua.service;

import com.softserve.teachua.dto.log.LogResponse;

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
     * The method delete logs by filter
     * In case default filter delete all logs without "catalina"
     * In case custom filter delete all logs by custom parameter without "catalina"
     *
     * @param filter - user write in url
     * @return {@code LogResponse}
     */
    LogResponse deleteLogsByFilter(String filter);

    /**
     * Use this method to get absolute path to log in dev or production
     * @return new {@code List<String>}
     */
    List<String> getAbsolutePathForLogs();

    /**
     * Use this method for delete empty logs
     *
     * @param filter
     * @return {@code LogResponse}
     */
    LogResponse  deleteEmptyLogs(Boolean filter);
}

