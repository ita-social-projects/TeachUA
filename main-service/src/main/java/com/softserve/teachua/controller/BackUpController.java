package com.softserve.teachua.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/backup")
public class BackUpController implements Api {
    private final BackupService backupService;

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
    @GetMapping("/files")
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
    @GetMapping(value = "/download", produces = "application/.zip")
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
    @PostMapping(value = "/upload")
    public List<String> uploadBackUp(@RequestParam("file") MultipartFile file) throws IOException {
        return backupService.uploadBackup(file);
    }
}
