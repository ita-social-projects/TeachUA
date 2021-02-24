package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ArchiveController implements Api {

    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    /**
     * The controller returns information {@code List <Archive>} about archives.
     *
     * @return new {@code List <Archive>}.
     */
    @GetMapping("/archives")
    public List<Archive> getArchives() {
        return archiveService.findAllArchives();
    }


    /**
     * The controller returns information {@code List <Archive>} about archives by className.
     *
     * @return new {@code List <Archive>}.
     */
    @GetMapping("/archives/{className}")
    public List<Archive> getArchivesByClassName(@PathVariable String className) {
        return archiveService.findArchivesByClassName(className);
    }
}
