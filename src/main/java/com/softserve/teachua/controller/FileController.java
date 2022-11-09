package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.FileRelevanceService;
import com.softserve.teachua.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class FileController implements Api {

    private final FileService fileService;

    private final FileRelevanceService fileRelevanceService;

    @Autowired
    public FileController(FileService fileService, FileRelevanceService fileRelevanceService) {
        this.fileService = fileService;
        this.fileRelevanceService = fileRelevanceService;
    }

    @GetMapping("/files/list")
    public List<String> viewFilesWithPath(@RequestParam(required = false) Optional<String> path) {
        if (path.isPresent()) {
            return fileService.listFiles(path.get());
        }
        return fileService.listFiles(".");
    }

    @GetMapping("/files/read")
    public String readFile(@RequestParam String name) {
        return fileService.readFile(name);
    }

    @GetMapping("/files/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String name) {
        return fileService.downloadFile(name);
    }

    @DeleteMapping("/files/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String name) {
        return fileService.deleteFile(name);
    }

    @GetMapping("/files/mentioned-in-db")
    public Set<String> getFilesMentionedInDb() {
        return fileRelevanceService.getAllMentionedFiles();
    }

}
