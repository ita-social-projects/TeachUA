package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.service.impl.FileUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class UploadController implements Api {

    @Value("${application.upload.path}")
    private String uploadDirectory;
    private final FileUploadServiceImpl fileUploadServiceImpl;

    @Autowired
    public UploadController(FileUploadServiceImpl fileUploadServiceImpl) {
        this.fileUploadServiceImpl = fileUploadServiceImpl;
    }


    @GetMapping("file")
    public File  getPhoto(@RequestBody FilePathRequest filePath){
        return fileUploadServiceImpl.getPhoto(filePath);
    }

    @PostMapping("file")
    public String uploadPhoto(@RequestParam("image") MultipartFile image,
                              @RequestParam("folder") String folder,
                              @RequestParam("clubId") Long id) {

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String uploadDir = String.format("%s/%s", uploadDirectory, folder);

        return fileUploadServiceImpl.uploadImage(uploadDir, fileName, image,id);
    }

    @PutMapping("/file")
    public String updatePhoto(@RequestParam MultipartFile file,
                              @RequestParam String filePath){

        return fileUploadServiceImpl.updatePhoto(file,filePath,uploadDirectory);
    }

    @DeleteMapping("/file")
    public String deleteFile(
            @RequestBody FilePathRequest fileDeleteRequest) {
       return fileUploadServiceImpl.deleteFile(fileDeleteRequest.getFilePath());

    }
}
