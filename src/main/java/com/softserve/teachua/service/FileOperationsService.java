package com.softserve.teachua.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * This service contains methods to manage files
 */
public interface FileOperationsService {
    /**
     * This method returns list of all files and directories located at {@code path}.
     * Directories are marked with a "/" at the end.
     *
     * @return {@code List<String>}
     */
    List<String> listFiles(String path);

    /**
     * This method reads all bytes from a file located at {@code path} and returns them in a String.
     *
     * @return {@code String}
     */
    String readFile(String path);

    /**
     * This method returns a downloadable resource for a file located at {@code path}.
     *
     * @return {@code String}
     */
    ResponseEntity<Resource> downloadFile(String path);

    /**
     * This method deletes a file located at {@code path}.
     *
     * @return {@code String}
     */
    ResponseEntity<String> deleteFile(String path);
}
