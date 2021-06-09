package com.softserve.teachua.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile);
    void deleteImages(String urlLogo, String urlBackground) ;
}
