package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloseDevAccess implements Api {
    private final FileUtils fileUtils;

    @Autowired
    public CloseDevAccess(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * Use this endpoint to delete key-file and close access to transfer controller methods
     *
     * @return new {@code List<SuccessCreatedTask>}.
     */
    @DeleteMapping("/transfer/close")
    public ResponseEntity<String> closeAccess(){
        return ResponseEntity.ok(fileUtils.deleteFile());
    }
}
