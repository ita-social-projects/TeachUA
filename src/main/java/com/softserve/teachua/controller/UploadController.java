package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.service.FileUploadService;
import com.softserve.teachua.service.impl.FileUploadServiceImpl;


import com.softserve.teachua.utils.annotation.AllowedRoles;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@Tag(name="upload", description="the Image Upload API")
@SecurityRequirement(name = "api")
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
    public String getPhoto(@RequestParam String filePath){
        return fileUploadService.getPhoto(filePath);
    }

    /**

     * Use this endpoint to confirm and save file
     * This feature available manager and admin
     *
     * @param  uploadProfile - put path and some values of file for save file
     * @return String new file path
     */


    @AllowedRoles(RoleData.ADMIN)
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

    @AllowedRoles(RoleData.ADMIN)
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

    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("file")
    public Boolean deleteFile(@RequestBody FilePathRequest fileDeleteRequest) {
        return fileUploadService.deleteFile(fileDeleteRequest.getFilePath());}


}
