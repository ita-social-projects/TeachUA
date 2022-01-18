package com.softserve.teachua.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This interface contain method for files backup
 */

public interface BackupService {
    /**
     * Use this method to see and check file for backup
     *
     * @param fileName
     * @return
     */
    List<String> getAllBackupFiles(String fileName);

    /**
     *  Use this method for download backup files
     *
     * @param backup
     * @throws IOException
     */
    void downloadBackup(HttpServletResponse backup) ;
}
