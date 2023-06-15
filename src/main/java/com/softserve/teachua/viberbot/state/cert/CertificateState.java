package com.softserve.teachua.viberbot.state.cert;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.menu.MenuState;
import lombok.SneakyThrows;

public class CertificateState extends BotState {

    public CertificateState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        if (action.equals(Action.MAIN_MENU)) return new MenuState(session);
        return this;
    }
    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        final Message menu = session.getContext()
                .getMessageFactory()
                .certificate(request.getSender(), request.getMessage().getText());
        return objectMapper.writeValueAsString(menu);
    }
}
