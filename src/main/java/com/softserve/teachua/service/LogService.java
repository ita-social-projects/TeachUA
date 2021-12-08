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
    List<String> getAllLogs( String filter, String content);

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
     * Use this method to search Absolute path to logs on prod
     *
     * @return
     */
    List<String> getAbsolutePathForLogs();

    String createSubDirectoryByName(String name) throws IOException;

    /**
     * Use this method for movingFile from logs to sub directory by name
     * @param directoryName
     * @return LogResponse
     */
    LogResponse moveLogsToSubDirectoryByDirectoryName(String directoryName);

}

