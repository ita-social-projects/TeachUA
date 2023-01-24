package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.FileOperationsService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FileOperationsServiceImpl implements FileOperationsService {
    private static final String ALLOWED_DELETE_ROOT = "upload";

    @Override
    public List<String> listFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, path + ", directory does not exist.");
        }
        return Arrays.stream(files)
                .map(file -> file.isDirectory() ? (file.getName() + "/") : file.getName())
                .collect(Collectors.toList());
    }

    @Override
    public String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, path + ", " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String path) {
        try {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(Paths.get(path)));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, path + ", " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteFile(String path) {
        Path filePath = Paths.get(path);
        if (!filePath.startsWith(Paths.get(ALLOWED_DELETE_ROOT))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deletion is not allowed");
        }
        try {
            Files.deleteIfExists(filePath);
            return ResponseEntity.noContent()
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, path + ", " + e.getMessage());
        }
    }
}
