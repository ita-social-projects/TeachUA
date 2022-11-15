package com.softserve.teachua.service;

import com.softserve.teachua.dto.log.LogResponse;

import java.io.IOException;
import java.util.List;

/**
 * This interface contains all needed methods to manage logs.
 */

public interface LogService {
    /**
     * The method returns list of logs {@code List<String>}.
     */
    List<String> getAllLogs();

    /**
     * The method returns list of logs {@code List<String>} by name.
     *
     * @param name
     *            put log name.
     *
     * @return new {@code List<String>}
     */
    List<String> getLogByName(String name);

    /**
     * The method delete logs by filter In case default filter delete all logs without "catalina" In case custom filter
     * delete all logs by custom parameter without "catalina"
     *
     * @param name
     *            user write in url
     */
    void deleteLogByName(String name);

    /**
     * Use this method to get absolute path to log in dev or production
     *
     * @return new {@code List<String>}
     */
    List<String> getAbsolutePathForLogs();

    String createSubDirectoryByName(String name) throws IOException;

    /**
     * Use this method for movingFile from logs to sub directory by name
     *
     * @param directoryName
     *
     * @return LogResponse
     */
    LogResponse moveLogsToSubDirectoryByDirectoryName(String directoryName);

    /**
     * Use this method for delete empty logs
     *
     * @param filter
     *
     * @return {@code LogResponse}
     */
    LogResponse deleteEmptyLogs(Boolean filter);

}
