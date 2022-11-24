package com.softserve.edu.utils;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DtoConverter.class);

    private FileUtil() {
        // Default private constructor, no object creation
    }

    /**
     * Read Properties file by file path
     * @param filePath - file path in resources
     * @return - properties
     */
    public static Properties readFileAsProperties(String filePath) {
        Properties props = new Properties();
        try(InputStream myIs = FileUtil.class.getResourceAsStream(filePath)) {
            props.load(myIs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return props;
    }

    /**
     * Load file content by file path from resources
     *
     * @param path - path to a file
     * @return String file contents
     */
    public static String loadFileAsString(String path) {
        LOGGER.error("Load file as String from resources: {}", path);
        String fileContents = null;
        try(InputStream inputStream = FileUtil.class.getResourceAsStream(path)) {
            fileContents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            // fileContents = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);       // since Java 9
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return fileContents;
    }

    /**
     * Gets an input stream from a file
     *
     * @param filePath - path to file
     * @param configClass - loader
     * @return InputStream of located file
     * @throws FileNotFoundException in case if file was not found
     */
    public static InputStream getFileInputStream(String filePath, Class<?> configClass) throws IOException {
        InputStream inputStream = configClass.getResourceAsStream(filePath);
        if(inputStream == null) {
            inputStream = Files.newInputStream(Paths.get(filePath));
        }
        return inputStream;
    }

}
