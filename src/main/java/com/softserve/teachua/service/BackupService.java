package com.softserve.teachua.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
     * @param response
     * @throws IOException
     */
    void unloadBackup(HttpServletResponse response) throws IOException;


    List<String> uploadBackup(MultipartFile file) throws IOException;



}
