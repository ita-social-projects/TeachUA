package com.softserve.teachua.viberbot.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.utils.viberbot.enums.Action;

public abstract class BotState {
    protected final Session session;
    protected final ObjectMapper objectMapper;

    protected BotState(Session session) {
        this.session = session;
        this.objectMapper = new ObjectMapper();
    }

    public abstract BotState handle(Action action, RequestDto request);

    public abstract String getMessage(RequestDto request);

}
