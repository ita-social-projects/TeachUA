package com.softserve.teachua.service;

import com.softserve.teachua.model.GalleryPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This interface contains all needed methods to manage file uploader.
 */

public interface FileUploadService {
    /**
    * The method uploads image.
    * */
    String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile);

    /**
     * The method deletes image.
     * */
    void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery);

    /**
     * The method deletes file.
     * */
    void deleteFile(String filePath);
}
