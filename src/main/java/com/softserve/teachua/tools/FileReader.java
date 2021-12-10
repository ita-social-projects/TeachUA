package com.softserve.teachua.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileReader {

    private final ObjectMapper objectMapper;

    @Autowired
    public FileReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List<T> readFromFile(String filePath){
        Path path = Paths.get(filePath);
        Class<T> t = null;
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            return reader.lines().map(line -> {
                try {
                    return objectMapper.readValue(line, t);
                } catch (JsonProcessingException e) {
                    throw new JsonWriteException();
                }
            }).collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new NotExistException();
        }
    }
}
