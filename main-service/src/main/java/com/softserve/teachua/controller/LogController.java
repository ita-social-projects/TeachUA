package com.softserve.teachua.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.constants.LogLevel;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogLevelService;
import com.softserve.teachua.service.LogService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the logs.
 */
@RestController
@Hidden
@RequestMapping("/api/v1/log")
public class LogController implements Api {
    private final LogService logService;
    private final LogLevelService logLevelService;

    public LogController(LogService logService, LogLevelService logLevelService) {
        this.logService = logService;
        this.logLevelService = logLevelService;
    }

    /**
     * Use this endpoint to get all logs. The controller returns list of {@code List<String>}.
     *
     * @return new {@code List<String>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping
    public List<String> getLogs() {
        return logService.getAllLogs();
    }

    /**
     * Use this endpoint to get a log by file name.
     * The controller returns {@code List<String>}.
     *
     * @param fileName - put log name.
     * @return {@code List<String>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/{fileName}")
    public List<String> getLogByName(@PathVariable String fileName) {
        return logService.getLogByName(fileName);
    }

    /**
     * Use this endpoint to get a log by file name as a Resource.
     * The controller returns {@code ResponseEntity<Resource>}.
     *
     * @param fileName - put log name.
     * @return {@code ResponseEntity<Resource>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/{fileName}/download")
    public ResponseEntity<Resource> downloadLogByName(@PathVariable String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(logService.loadLogAsResource(fileName), headers, HttpStatus.OK);
    }

    /**
     * Use this endpoint to delete a log by file name.
     *
     * @param fileName - put log name.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{fileName}")
    public void deleteLogByName(@PathVariable String fileName) {
        logService.deleteLogByName(fileName);
    }

    /**
     * Use this endpoint to change logs level for package/class.
     *
     * @param level       use this param to select the logging level
     * @param packagePath use this param to pave the path to package/class
     */
    @AllowedRoles(RoleData.ADMIN)
    @PatchMapping("/level")
    public void changeLogLevel(@RequestParam LogLevel level,
                               @RequestParam(required = false, defaultValue = "") String packagePath) {
        logLevelService.changeLogLevel(level, packagePath);
    }
}
