package com.softserve.teachua.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.exception.NotExistException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileReader {

    private final ObjectMapper objectMapper;

    @Autowired
    public FileReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List<T> readFromFile(String filePath, Class<T> tClass){
        log.info(this.getClass().getClassLoader().getResource(filePath).getPath());

        Path path = null;
        try {
            path = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
}
