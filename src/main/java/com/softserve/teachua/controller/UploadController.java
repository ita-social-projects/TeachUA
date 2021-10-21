package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.impl.FileUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController implements Api {

    @Value("${application.upload.path}")
    private String uploadDirectory;
    private final FileUploadServiceImpl fileUploadServiceImpl;

    @Autowired
    public UploadController(FileUploadServiceImpl fileUploadServiceImpl) {
        this.fileUploadServiceImpl = fileUploadServiceImpl;
    }

    @PostMapping("/upload-image")
    public String uploadPhoto(@RequestParam("image") MultipartFile image,
                              @RequestParam("folder") String folder) {

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);

        return fileUploadServiceImpl.uploadImage(uploadDir, fileName, image);
    }

    @DeleteMapping("/delete-file")
    public String deleteFile(@RequestParam String filePath) {
        fileUploadServiceImpl.deleteFile(filePath);
        return filePath+" successfully deleted.";
    }
}
