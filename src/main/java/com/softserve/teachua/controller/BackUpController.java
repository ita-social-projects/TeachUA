package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     *  Use this endpoint to see files for backup
     * @param fileName
     * @return
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/getBackUpFiles")
    public List<String> getAllFileForBackUp(@RequestParam(value = "fileName", required = false, defaultValue = "all") String fileName) {
        return backupService.getAllBackupFiles(fileName);
    }

    /**
     * Use this endpoint to download .zip file with backup resources
     *
     * @param backup
     * @throws IOException
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping(value = "/downloadBackup", produces = "application/zip")
    public void downloadBackup(HttpServletResponse backup)  {
        backupService.downloadBackup(backup);
    }
}

