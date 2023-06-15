package com.softserve.teachua.service.viberbot;

import com.softserve.teachua.dto.viberbot.RequestDto;

public interface BotService {
    String incoming(RequestDto dto);
}
