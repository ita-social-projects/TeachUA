package com.softserve.teachua.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileUtils {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String UPLOAD_LOCATION = "\\upload";
    private static final String FILE_FIND_EXCEPTION =
            "Can't find file by path - %s. Path should be, for example, as follows - " +
                    "'images/image.png' ('images' package should be located in resource package)";
    private static final String MOVE_FILE_EXCEPTION = "Can't move file form '%s' to '%s'";
    private static final String ALREADY_EXIST_EXCEPTION = "File with path - %s has already exists";
    private static final String MAPPER_EXCEPTION = "Can't convert string - '%s' to %s, by cause: %s";
    @Value("${application.upload.path}")
    private String uploadDirectory;
    private final ObjectMapper objectMapper;


    @Autowired
    public FileUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Use this endpoint to get list of objects from file with jsons.
     *
     * @param filePath - string path to file.
     * @param tClass   - type class of objects.
     * @return new {@code List<T>}.
     */
    public <T> List<T> readFromFile(String filePath, Class<T> tClass) {
        Path path = getPathOfFile(filePath);
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            return reader.lines().map(line -> {
                try {
                    return objectMapper.readValue(line, tClass);
                } catch (JsonProcessingException e) {
                    throw new IncorrectInputException(String.format(MAPPER_EXCEPTION, line, tClass, e.getMessage()));
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new NotExistException(String.format(FILE_FIND_EXCEPTION, filePath));
        }
    }

    /**
     * Use this endpoint to get path of string path.
     *
     * @param filePath - string path to file.
     * @return new {@code Path}.
     */
    public Path getPathOfFile(String filePath) {
        try {
            return Paths.get(Optional.ofNullable(getClass().getClassLoader().getResource(filePath))
                    .orElseThrow(() -> new NotExistException())
                    .toURI());
        } catch (URISyntaxException e) {
            throw new IncorrectInputException(e.getMessage());
        } catch (NotExistException e) {
            throw new NotExistException(String.format(FILE_FIND_EXCEPTION, filePath));
        }
    }

    /**
     * Use this endpoint to move image from resource package to upload package.
     * Returns new destination of image.
     *
     * @param imagePath - string path to image.
     * @param folder    - package in which file will moved
     * @return new {@code String}.
     */
    public String moveImage(String imagePath, String folder) {
        Path path = getPathOfFile(imagePath);
        String name = String.format(
                "%s_%s",
                dateTimeFormat.format(LocalDateTime.now()),
                imagePath.substring(imagePath.lastIndexOf('/') + 1));
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);
        Path destination = Paths.get(String.format("%s/%s", uploadDir, name));
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String newPath = Files.move(path, destination).toString();
            return newPath.substring(newPath.indexOf(UPLOAD_LOCATION)).replace('\\', '/');
        } catch (FileAlreadyExistsException e) {
            throw new IncorrectInputException(String.format(ALREADY_EXIST_EXCEPTION, destination));
        } catch (IOException e) {
            throw new IncorrectInputException(String.format(MOVE_FILE_EXCEPTION, path, destination));
        }
    }
}
