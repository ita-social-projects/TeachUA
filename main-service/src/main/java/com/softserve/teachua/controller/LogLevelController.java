package com.softserve.teachua.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.LogLevelService;
import com.softserve.teachua.constants.LogLevel;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class LogLevelController implements Api {
    private final LogLevelService logLevelService;

    public LogLevelController(LogLevelService logLevelService) {
        this.logLevelService = logLevelService;
    }

    /**
     * Use this endpoint to change logs level for package/class.
     *
     * @param level
     *            use this param to select the logging level
     * @param packagePath
     *            use this param to pave the path to package/class
     */
    @AllowedRoles(RoleData.ADMIN)
    @PatchMapping("/logs/level")
    public void changeLogLevel(@RequestParam LogLevel level,
            @RequestParam(required = false, defaultValue = "") String packagePath) {
        logLevelService.changeLogLevel(level, packagePath);
    }
}
