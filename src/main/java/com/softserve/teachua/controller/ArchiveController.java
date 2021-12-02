package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * This controller is for managing the archive.
 * */

@RestController
@Slf4j
@Tag(name = "archive", description = "the Archive API")
@SecurityRequirement(name = "api")
public class ArchiveController implements Api {
    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    /**
     * Use this endpoint to get the Archive information for all Archives.
     * The controller returns information {@code List <Archive>} about archives.
     *
     * @return new {@code List <Archive>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/archives")
    public List<Archive> getArchives() {
        return archiveService.findAllArchives();
    }

    /**
     * Use this endpoint to get the Archive information based on ClassName.
     * The controller returns information {@code List <Archive>} about archives by className.
     *
     * @param className - put className here.
     * @return new {@code List <Archive>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/archives/{className}")
    public List<Archive> getArchivesByClassName(@PathVariable String className) {
        return archiveService.findArchivesByClassName(className);
    }


    @AllowedRoles(RoleData.ADMIN)
    @PatchMapping("/archives/{id}")
    public Archive restoreFromArchive(@PathVariable("id") Long id) throws IOException, ClassNotFoundException {
        return archiveService.restoreArchiveObject(id);
    }


}
