package com.softserve.teachua.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the logs.
 */

@RestController
@Hidden
public class LogController implements Api {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Use this endpoint to get all logs. The controller returns list of {@code List<String>}.
     *
     * @return new {@code List<String>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/logs")
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
    @GetMapping("/logs/{fileName}")
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
    @GetMapping("/logs/{fileName}/download")
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
    @DeleteMapping("/logs/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLogByName(@PathVariable String fileName) {
        logService.deleteLogByName(fileName);
    }
}
