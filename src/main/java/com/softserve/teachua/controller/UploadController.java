package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.service.impl.FileUploadServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;

import java.io.*;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UploadController implements Api {

    private final FileUploadServiceImpl fileUploadServiceImpl;

    @Autowired
    public UploadController(FileUploadServiceImpl fileUploadServiceImpl) {
        this.fileUploadServiceImpl = fileUploadServiceImpl;
    }

    @GetMapping("file")
    public String  getPhoto(@RequestParam String filePath){
        return fileUploadServiceImpl.getPhoto(filePath);
    }
    
    @PostMapping("file")
    public String uploadPhoto(@RequestBody FileUploadProfile uploadProfile) {
        return fileUploadServiceImpl.uploadImage(uploadProfile);}

    @PutMapping("/file")
    public Boolean updatePhoto(@RequestBody FileUpdateProfile fileUpdateProfile){return fileUploadServiceImpl.updatePhoto(fileUpdateProfile);}

    @DeleteMapping("/file")
    public Boolean deleteFile(@RequestBody FilePathRequest fileDeleteRequest) {return fileUploadServiceImpl.deleteFile(fileDeleteRequest.getFilePath());}

}
