package com.softserve.teachua.viberbot.state.menu;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.cert.CertificatesState;
import com.softserve.teachua.viberbot.state.info.InfoState;
import lombok.SneakyThrows;

public class MenuState extends BotState {

    public MenuState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
         switch (action) {
            case INFO:
                return new InfoState(session);
            case CERTIFICATES :
                return new CertificatesState(session);
            default : return this;
        }
    }

    @SneakyThrows
    public String getMessage(RequestDto request) {
        final Message menu = session.getContext()
                .getMessageFactory()
                .mainMenu(request.getSender());
        return objectMapper.writeValueAsString(menu);
    }
}
