package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/unloadBackup", produces = "application/zip")
    public void unloadBackup(HttpServletResponse backup)  {
        backupService.unloadBackup(backup);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping(value = "/downloadBackup",consumes ="application/zip" )
    public void downloadBackUp(HttpServletRequest request) throws IOException {
        backupService.uploadBackup(request);
    }
}

