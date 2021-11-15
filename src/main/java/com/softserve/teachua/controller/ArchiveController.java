package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.service.ArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Tag(name="archive", description="the Archive API")
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
    @GetMapping(value ="/archives", produces = {"application/json", "application/xml"})
    public List<Archive> getArchives() {
        return archiveService.findAllArchives();
    }


    /**
     * Use this endpoint to get the Archive information based on ClassName.
     * The controller returns information {@code List <Archive>} about archives by className.
     * @param className - put className here.
     * @return new {@code List <Archive>}.
     */
    @GetMapping(value = "/archives/{className}", produces = {"application/json", "application/xml"})
    public List<Archive> getArchivesByClassName(@PathVariable String className) {
        return archiveService.findArchivesByClassName(className);
    }
}
