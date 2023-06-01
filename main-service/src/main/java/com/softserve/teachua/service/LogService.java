package com.softserve.teachua.service;

import java.util.List;
import org.springframework.core.io.Resource;

/**
 * This interface contains all needed methods to manage logs.
 */

public interface LogService {
    /**
     * The method returns list of logs {@code List<String>}.
     */
    List<String> getAllLogs();

    /**
     * The method returns list of lines {@code List<String>} of the log by name.
     *
     * @param fileName - put log name.
     * @return new {@code List<String>}
     */
    List<String> getLogByName(String fileName);

    /**
     * The method returns log file as a resource {@code Resource} of the log by name.
     *
     * @param fileName - put log name.
     * @return resource {@code Resource}
     */
    Resource loadLogAsResource(String fileName);

    /**
     * Use this endpoint to delete a log by file name.
     *
     * @param fileName - put log name.
     */
    void deleteLogByName(String fileName);
}
