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
    public List<String> getLogs() {
        return logService.getAllLogs();
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
    //Зробити  параметр, при активації якого ми видаляємо логи до того часу, якщо не активовуємо, то видаляємо лише за цей час
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/logs/{date}")
    public Boolean deleteLogsBeforeDate(
            @RequestParam(required = false,defaultValue = "true")Boolean singleDate,
            @PathVariable String date) {
        return logService.deleteLogsByDate(date,singleDate);
    }
}