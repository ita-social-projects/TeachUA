package com.softserve.archive.controller;

import com.softserve.archive.service.ArchiveService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/archive")
public class ArchiveController {
    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping(params = {"className", "id"})
    public Map<String, String> restoreModel(@RequestParam("className") String className,
                                            @RequestParam("id") Number id) {
        return archiveService.restoreModel(className, id);
    }
}
