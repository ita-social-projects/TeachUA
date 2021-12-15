package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloseDevAccess implements Api {
    private final FileUtils fileUtils;

    @Autowired
    public CloseDevAccess(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @DeleteMapping("/transfer/close")
    public String closeAccess(){
        return fileUtils.deleteFile();
    }
}
