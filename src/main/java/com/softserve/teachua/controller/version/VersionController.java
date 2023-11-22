package com.softserve.teachua.controller.version;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.version.VersionDto;
import com.softserve.teachua.service.VersionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "version", description = "the build version API")
@SecurityRequirement(name = "api")
public class VersionController implements Api {
    private VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Use this endpoint to get the version information. The controller returns {@code VersionDto}.
     *
     * @return {@code VersionDto}.
     */
    @GetMapping("/version")
    public VersionDto getDate() {
        log.debug("VersionController start");
        return versionService.getVersion();
    }
}
