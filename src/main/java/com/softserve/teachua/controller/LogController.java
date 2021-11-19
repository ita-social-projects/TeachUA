package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
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

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/logs")
    public List<String> getLogs() {

        return logService.getAllLogs();
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/log/{name}")
    public List<String> getLogByName(@PathVariable String name) {

        return logService.getLogByName(name);
    }

    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/logs")
    public Boolean deleteAllLogs() {

        return logService.deleteAllLogs();
    }
}