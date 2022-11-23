package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class BackUpController implements Api {

    private final BackupService backupService;

    @Autowired
    public BackUpController(BackupService backupService) {
        this.backupService = backupService;
    }

    /**
     * Use this endpoint to see files for backup
     *
     * @param fileName
     *
     * @return
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
     * @param response
     *
     * @throws IOException
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping(value = "/backup/download", produces = "application/.zip")
    public void downloadBackup(HttpServletResponse response) throws IOException {
        backupService.downloadBackup(response);
    }

    /**
     *
     * Use this endpoint to upload backup archive(.zip(downloaded from /backup/download)) to project
     *
     * @param file
     *
     * @return
     *
     * @throws IOException
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping(value = "/backup/upload")
    public List<String> uploadBackUp(@RequestParam("file") MultipartFile file) throws IOException {
        return backupService.uploadBackup(file);
    }
}