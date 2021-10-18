package com.softserve.teachua.service.impl;

import com.softserve.teachua.exception.BadRequestException;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    private final String FILE_UPLOAD_EXCEPTION = "Could not save image file: %s";
    private final String DIRECTORY_CREATE_EXCEPTION = "Could not create directory with name: %s";
    private final String UPLOAD_LOCATION = "/upload";


    @Override
    public String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile) {
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

    @Override
    public void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery) {
        // assuming that path to folder will look like target/upload/clubs/uuidv4

        String folderName = null;
        try {
            if (urlLogo != null && urlLogo.contains(UPLOAD_LOCATION)) {
                folderName = urlLogo.substring(0, ordinalIndexOf(urlLogo, "/", 4, false));
            } else if (urlBackground != null && urlBackground.contains(UPLOAD_LOCATION)) {
                folderName = urlBackground.substring(0, ordinalIndexOf(urlBackground, "/", 4, false));
            } else if (urlGallery != null && !urlGallery.isEmpty()) {
                String url = urlGallery.get(0).getUrl();
                folderName = url.substring(0, ordinalIndexOf(url, "/", 4, false));
            }
        } catch (IndexOutOfBoundsException ex) {
            log.error("Incorrect photo url");
        }

        if (folderName != null) {
            try {
                FileUtils.deleteDirectory(new File("target" + folderName));
            } catch (IOException ex) {
                log.error("Folder " + folderName + " can not be deleted");
            }
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path can not be null or empty");
        }
        if (!filePath.contains(UPLOAD_LOCATION)) {
            throw new BadRequestException("Wrong uploaded file path");
        }
        String dirPath = filePath.substring(0, ordinalIndexOf(filePath, "/", 4, false));
        try {
            FileUtils.deleteDirectory(new File("target" + dirPath));
        } catch (IOException e) {
            throw new FileUploadException(String.format("Can't delete directory with path: %s", dirPath));
        }
    }

    private int ordinalIndexOf(final String str, final String searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.lastIndexOf(searchStr, index - 1);
            } else {
                index = str.indexOf(searchStr, index + 1);
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

}
