package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.log.LogResponse;
import com.softserve.teachua.service.LogService;
import io.swagger.v3.oas.annotations.Hidden;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This controller is for managing the logs.
 * */

@RestController
@Hidden
public class LogController implements Api {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Use this endpoint to get all logs.
     * The controller returns list of {@code List<String>}.
     *
     * @return new {@code List<String>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/logs")
    public List<String> getLogs(@RequestParam (value = "filter", required = false, defaultValue = "") String filter) {

        return logService.getAllLogs(filter);
    }

    /**
     * Use this endpoint to get logs by name
     * The controller returns  {@code List<String>}.
     *
     * @param name - put log name.
     * @return {@code List<String>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/log/{name}")
    public List<String> getLogByName(@PathVariable String name) {
        return logService.getLogByName(name);
    }

    /**
     * Use this endpoint to delete logs by filter
     * Default filter - Delete all logs without "catalina" files
     * Filter with string parameter - delete all logs where file contain string parameter in name except "catalina"
     *
     * @param filter
     * @return LogResponse
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/logs")
    public LogResponse deleteLogsByFilter(@RequestParam(required = false,defaultValue = "deleteAll")String filter) {
        return logService.deleteLogsByFilter(filter);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/log/getAbsolutePathToLogs")
    public List<String> getPathToLogs(){
        return  logService.getAbsolutePathForLogs();
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/log")
    public String createSubDirectory(@RequestParam(required = false,defaultValue = "date") String directoryName) throws IOException {
       return logService.createSubDirectoryByName(directoryName);
    }
}
