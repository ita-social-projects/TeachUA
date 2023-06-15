package com.softserve.teachua.controller.viberbot;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.service.viberbot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViberController {

    private final BotService botService;

    @Autowired
    public ViberController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping("/")
    public String incoming(@RequestBody RequestDto dto) {
       return botService.incoming(dto);
    }
}
