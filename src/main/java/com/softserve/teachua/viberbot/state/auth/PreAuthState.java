package com.softserve.teachua.viberbot.state.auth;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.menu.MenuState;
import lombok.SneakyThrows;

public class PreAuthState extends BotState {
    public PreAuthState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        if (session.getContext().getMessengerService().messengerWithAccessKeyExist(request.getSender().getId()))
            return new MenuState(session);
        if (action == Action.AUTH) return new CheckEmailState(session);
        return this;
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        final Message message = session.getContext()
                .getMessageFactory()
                .auth(request.getSender());
        return objectMapper.writeValueAsString(message);
    }
}
