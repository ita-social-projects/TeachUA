package com.softserve.teachua.service;

import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.model.GalleryPhoto;

import java.io.File;
import java.util.List;

public interface FileUploadService {
    String uploadImage(FileUploadProfile fileUploadProfile);
    void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery);
    String deleteFile(String filePath);
    String getPhoto(FilePathRequest filePath);
    String updatePhoto(FileUpdateProfile fileUpdateProfile);
}
