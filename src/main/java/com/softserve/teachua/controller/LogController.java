package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogService;
import io.swagger.v3.oas.annotations.Hidden;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<String> getLogs(@RequestParam (value = "filter", required = false, defaultValue = "") String filter,
                                @RequestParam (value = "content", required = false, defaultValue = "") String content) {

        return logService.getAllLogs(filter, content);
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
     * Use this endpoint to delete all logs.
     * The controller returns {@code Boolean}.
     *
     * @return new {@code Boolean}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/logs")
    public Boolean deleteAllLogs() {
        return logService.deleteAllLogs();
    }
}
