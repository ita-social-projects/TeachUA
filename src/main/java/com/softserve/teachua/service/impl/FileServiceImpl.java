package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

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
    public String readFile(String name) {
        try {
            return new String(Files.readAllBytes(Paths.get(name)));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + ", " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String name) {
        try {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(Paths.get(name)));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + ", " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteFile(String name) {
        try {
            Files.deleteIfExists(Paths.get(name));
            return ResponseEntity.noContent()
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + ", " + e.getMessage());
        }
    }


}
