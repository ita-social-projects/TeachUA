package com.softserve.teachua.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.exception.NotExistException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileUtils {

    @Value("${application.upload.path}")
    private String uploadDirectory;
    private final ObjectMapper objectMapper;

    @Autowired
    public FileUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List<T> readFromFile(String filePath, Class<T> tClass){
        log.info(this.getClass().getClassLoader().getResource(filePath).getPath());

        Path path = getPathOfFile(filePath);
        log.info(path.toString());
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            return reader.lines().map(line -> {
                try {
                    return objectMapper.readValue(line, tClass);
                } catch (JsonProcessingException e) {
                    throw new JsonWriteException(e.getMessage());
                }
            }).collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new NotExistException();
        }
    }

    public Path getPathOfFile(String filePath){
        try {
            return Paths.get(getClass().getClassLoader().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new NotExistException(String.format("can't find file by path - %s", filePath));
        }
    }

    public String moveImage(String imagePath, String folder) {
        Path path = getPathOfFile(imagePath);
        String name = imagePath.substring(Math.max(imagePath.lastIndexOf('/'), 0));
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Path destination = Files.move(path, Paths.get(String.format("%s\\%s", uploadDir, name)));
            return destination.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}
