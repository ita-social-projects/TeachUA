package com.softserve.teachua.service;

import com.softserve.teachua.model.GalleryPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile);
    void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery);
}
