package com.softserve.teachua.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.log.EnvironmentResponce;
import com.softserve.teachua.service.EnvironmentService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//todo
@RestController
@Hidden
public class EnvironmentController implements Api {
    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/env")
    public EnvironmentResponce getEnvironment(
            @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        return environmentService.getEnvironment(name);
    }
}
