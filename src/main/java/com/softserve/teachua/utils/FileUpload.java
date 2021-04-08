package com.softserve.teachua.utils;

import com.softserve.teachua.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUpload {
    private static final String FILE_UPLOAD_EXCEPTION = "Could not save image file: %s";
    private static final String DIRECTORY_CREATE_EXCEPTION = "Could not create directory with name: %s";
    private static final String UPLOAD_LOCATION = "/upload";


    public static String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new FileUploadException(String.format(DIRECTORY_CREATE_EXCEPTION, fileName));
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new FileUploadException(String.format(FILE_UPLOAD_EXCEPTION, fileName));
        }

        String actualPath = String.format("/%s/%s", uploadDir, fileName);
        return actualPath.substring(actualPath.indexOf(UPLOAD_LOCATION));
    }


}
