package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.utils.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController implements Api {

    @Value("${application.upload.path}")
    private String uploadDirectory;

    @PostMapping("/upload-image")
    public String uploadPhoto(
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("folder") String folder) {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);

        return FileUpload.uploadImage(uploadDir, fileName, multipartFile);
    }

}
