package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dao.BackupDaoImpl;
import com.softserve.teachua.dao.service.BackupService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class TablesBackupController implements Api {

    private final BackupService backupService;

    @Autowired
    public TablesBackupController(BackupDaoImpl backupDaoImpl, BackupService backupService) {
        this.backupService = backupService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/backup/{tableName}")
    public String getTable(@PathVariable String tableName) {
        return backupService.getTable(tableName);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/backup")
    public String getAllTables(@RequestParam String tableNames) {
        return backupService.getAllTables(tableNames);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String tableNames) throws IOException {

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=backup.sql");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(backupService.getAllTables(tableNames).getBytes());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(inputStream.available())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
