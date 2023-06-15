package com.softserve.teachua.viberbot.state.cert;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.menu.MenuState;
import lombok.SneakyThrows;

public class CertificatesState extends BotState {
    public CertificatesState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        switch (action) {
            case MAIN_MENU: return new MenuState(session);
            case USER_INPUT: return new CertificateState(session);
        }
        return this;
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        final Message menu = session.getContext()
                .getMessageFactory()
                .certificates(request.getSender());
        return objectMapper.writeValueAsString(menu);
    }

}
