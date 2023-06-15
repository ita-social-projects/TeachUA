package com.softserve.teachua.viberbot.state.auth;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import lombok.SneakyThrows;

public class CheckEmailFailedState extends BotState {
    protected CheckEmailFailedState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        return new CheckEmailState(session).handle(action, request);
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        return objectMapper.writeValueAsString(session.getContext()
                .getMessageFactory().emailAuthFailed(request.getSender()));
    }
}
