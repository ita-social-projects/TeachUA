package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application Controller.
 */
@RestController
@Hidden
public class ApplicationController implements Api {
    //todo
    //@Value("${server.servlet.context-path}")
    private String rootUri;

    /**
     * Returns URI that which is specified as environmental variable in system.
     *
     * @return rootUri
     */
    @GetMapping("/uri")
    public String getUri() {
        return rootUri;
    }
}
