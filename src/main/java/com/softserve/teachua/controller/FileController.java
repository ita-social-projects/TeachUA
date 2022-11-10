package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.FileRelevanceService;
import com.softserve.teachua.service.FileOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class FileController implements Api {

    private final FileOperationsService fileOperationsService;

    private final FileRelevanceService fileRelevanceService;

    @Autowired
    public FileController(FileOperationsService fileOperationsService, FileRelevanceService fileRelevanceService) {
        this.fileOperationsService = fileOperationsService;
        this.fileRelevanceService = fileRelevanceService;
    }

    @GetMapping("/files/list")
    public List<String> viewFilesWithPath(@RequestParam(required = false) Optional<String> path) {
        if (path.isPresent()) {
            return fileOperationsService.listFiles(path.get());
        }
        return fileOperationsService.listFiles(".");
    }

    @GetMapping("/files/read")
    public String readFile(@RequestParam String path) {
        return fileOperationsService.readFile(path);
    }

    @GetMapping("/files/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {
        return fileOperationsService.downloadFile(path);
    }

    @DeleteMapping("/files/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String path) {
        return fileOperationsService.deleteFile(path);
    }

    @GetMapping("/files/mentioned-in-db")
    public Set<String> getFilesMentionedInDb() {
        return fileRelevanceService.getAllMentionedFiles();
    }

    @GetMapping("/files/orphaned-files")
    public Set<String> getOrphanedFiles() {
        return fileRelevanceService.getAllOrphanedFiles();
    }

}
