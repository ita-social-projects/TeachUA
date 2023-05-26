package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BackUpController implements Api {
    private final BackupService backupService;

    @Autowired
    public BackUpController(BackupService backupService) {
        this.backupService = backupService;
    }

    /**
     * Use this endpoint to see files for backup.
     *
     * @param fileName a file name
     * @return a list of the files
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/backup/files")
    public List<String> getAllFileForBackUp(
            @RequestParam(value = "fileName", required = false, defaultValue = "all") String fileName) {
        return backupService.getAllBackupFiles(fileName);
    }

    /**
     * Use this endpoint to download .zip file with backup resources
     *
     * @param response HttpServletResponse
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping(value = "/backup/download", produces = "application/.zip")
    public void downloadBackup(HttpServletResponse response) {
        backupService.downloadBackup(response);
    }

    /**
     * Use this endpoint to upload backup archive(.zip(downloaded from /backup/download)) to project
     *
     * @param file uploaded file to back up
     * @return a list of uploaded files
     * @throws IOException can occur while I/O operations
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping(value = "/backup/upload")
    public List<String> uploadBackUp(@RequestParam("file") MultipartFile file) throws IOException {
        return backupService.uploadBackup(file);
    }
}
