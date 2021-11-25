package com.softserve.teachua.service;

import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.model.GalleryPhoto;

import java.util.List;

/**
 * This interface contains all needed methods to manage file uploader.
 */

public interface FileUploadService {

    String uploadImage(FileUploadProfile fileUploadProfile);
    void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery);
    Boolean deleteFile(String filePath);
    String getPhoto(String filePath);
    Boolean updatePhoto(FileUpdateProfile fileUpdateProfile);

}
