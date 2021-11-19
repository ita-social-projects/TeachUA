package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController implements Api {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/logs")
    public List<String> getLogs() {
        return logService.getAllLogs();
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/log/{name}")
    public List<String> getLogByName(@PathVariable String name) {
        return logService.getLogByName(name);
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @DeleteMapping("/logs")
    public Boolean deleteAllLogs() {
        return logService.deleteAllLogs();
    }
}