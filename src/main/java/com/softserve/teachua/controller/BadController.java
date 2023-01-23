package com.softserve.teachua.controller;

import com.softserve.teachua.service.impl.BadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BadController {

    private final BadService badService;

    public BadController(BadService badService) {
        this.badService = badService;
    }

    public String getNoInformation(){
        System.out.println("Print at console");
        log.info("Print in log");
        int index;
        index = 15;
        index=22;
        return badService.getNoting();
    }
}
