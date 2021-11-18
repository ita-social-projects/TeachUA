package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.service.FileUploadService;
import com.softserve.teachua.service.impl.FileUploadServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@RestController
public class UploadController implements Api {

    private final FileUploadService fileUploadService;

    @Autowired
    public UploadController( FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * Use this endpoint to get byte code which is converted to String
     * This feature available manager and admin
     *
     * @param filePath param
     * @return String byte code of file
     */
    @GetMapping("file")
    public String  getPhoto(@RequestParam String filePath){
        return fileUploadService.getPhoto(filePath);
    }

    /**
     * Use this endpoint to confirm and save file
     * This feature available manager and admin
     *
     * @param  uploadProfile - put path and some values of file for save file
     * @return String new file path
     */
    
    @PostMapping("file")
    public String uploadPhoto(@RequestBody FileUploadProfile uploadProfile) {
        return fileUploadService.uploadImage(uploadProfile);}

    /**
     * Use this endpoint to update some values of photo
     * This feature available manager and admin
     *
     * @param fileUpdateProfile put new and old parameters here
     * @return if done successed return true else false
     */

    @PutMapping("file")
    public Boolean updatePhoto(@RequestBody FileUpdateProfile fileUpdateProfile){
        return fileUploadService.updatePhoto(fileUpdateProfile);}

    /**
     * Use this endpoint to delete file
     * This feature available manager and admin
     *
     * @param fileDeleteRequest - put path to file
     * @return if done successed return true else false
     */
    @DeleteMapping("file")
    public Boolean deleteFile(@RequestBody FilePathRequest fileDeleteRequest) {
        return fileUploadService.deleteFile(fileDeleteRequest.getFilePath());}

}
