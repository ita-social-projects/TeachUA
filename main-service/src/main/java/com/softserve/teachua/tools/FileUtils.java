package com.softserve.teachua.tools;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.exception.IncorrectInputException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String UPLOAD_LOCATION = "/upload";
    private static final String FILE_FIND_EXCEPTION = "Can't find file by path - '%s'. "
            + "Path should be, for example, as follows - 'images/image.png' "
            + "('images' package should be located in resource package)";
    private static final String MOVE_FILE_EXCEPTION = "Can't move file form '%s' to '%s'";
    private static final String ALREADY_EXIST_EXCEPTION = "File with path - %s has already exists";
    private static final String MAPPER_EXCEPTION = "Can't convert string - '%s' to %s, by cause: %s";
    private static final String SUCCESS_DELETED = "%s file was successfully deleted";
    private static final String FILE_PATH = "data_for_db/key-file.ssh";
    private final ObjectMapper objectMapper;
    @Value("${application.upload.path}")
    private String uploadDirectory;

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
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String fileLines = String.join("", reader.lines().toList());
            return objectMapper.readValue(fileLines,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (JsonMappingException e) {
            throw new IncorrectInputException(
                    String.format(MAPPER_EXCEPTION, e.getLocalizedMessage(), tClass, e.getMessage()));
        } catch (IOException e) {
            log.error("Error while reading from file {}, {}", filePath, e.getMessage());
        } catch (NotExistException e) {
            throw new NotExistException(String.format(FILE_FIND_EXCEPTION, filePath));
        }
        throw new IllegalArgumentException();
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
                    .orElseThrow(NotExistException::new).toURI());
        } catch (URISyntaxException e) {
            throw new IncorrectInputException(e.getMessage());
        } catch (NotExistException e) {
            throw new NotExistException(String.format(FILE_FIND_EXCEPTION, filePath));
        }
    }

    /**
     * Use this endpoint to move image from resource package to upload package. Returns new destination of image.
     *
     * @param imagePath - string path to image.
     * @param folder    - package in which file will moved
     * @return new {@code String}.
     */
    public String moveImage(String imagePath, String folder) {
        Path path = getPathOfFile(imagePath);
        String name = String.format("%s_%s", DATE_TIME_FORMATTER.format(LocalDateTime.now()),
                imagePath.substring(imagePath.lastIndexOf('/') + 1));
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);
        Path destination = Paths.get(String.format("%s/%s", uploadDir, name));
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String newPath = Files.move(path, destination).toString().replace('\\', '/');
            return newPath
                    .substring(newPath.indexOf(uploadDirectory) + uploadDirectory.length() - UPLOAD_LOCATION.length());
        } catch (FileAlreadyExistsException e) {
            log.error(e.getMessage());
            throw new IncorrectInputException(String.format(ALREADY_EXIST_EXCEPTION, destination));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IncorrectInputException(String.format(MOVE_FILE_EXCEPTION, path, destination));
        }
    }

    /**
     * Use this endpoint to delete from resource package. Returns status message.
     *
     * @return new {@code String}.
     */
    public String deleteFile() {
        try {
            Files.delete(getPathOfFile(FILE_PATH));
        } catch (IOException e) {
            throw new NotExistException(String.format(FILE_FIND_EXCEPTION, FILE_PATH));
        }
        return String.format(SUCCESS_DELETED, FILE_PATH);
    }

    /**
     * Use this endpoint to check if file exists in resource package.
     *
     * @return new {@code Boolean}.
     */
    public Boolean isKeyFileExists() {
        try {
            getPathOfFile(FILE_PATH);
        } catch (NotExistException | IncorrectInputException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
