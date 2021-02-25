package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController implements Api {

    @Value("${server.servlet.context-path}")
    private String rootUri;

    @GetMapping("/uri")
    public String getUri() {
        return rootUri;
    }

}
