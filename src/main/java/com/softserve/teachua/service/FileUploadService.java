package com.softserve.teachua.service;

import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.model.GalleryPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileUploadService {
    String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile,Long id);
    void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery);
    String deleteFile(String filePath);
    String updatePhoto(MultipartFile file,String filePath,String uploadDirectory);
    File getPhoto(FilePathRequest filePath);
}
