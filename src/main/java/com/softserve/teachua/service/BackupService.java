package com.softserve.teachua.service;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface contain method for files backup.
 */

public interface BackupService {
    /**
     * Use this method to see and check file for backup.
     *
     * @param fileName a file name to check for back up
     * @return a list of files
     */
    List<String> getAllBackupFiles(String fileName);

    /**
     * Use this method for download backup files.
     *
     * @param response add backup file
     */
    void downloadBackup(HttpServletResponse response);

    /**
     * Use this method for upload backup files to project(use file downloaded from @downloadBackup).
     *
     * @param file a file to save
     * @return a list of filename
     * @throws IOException can occur while I/O operations
     */

    List<String> uploadBackup(MultipartFile file) throws IOException;
}
