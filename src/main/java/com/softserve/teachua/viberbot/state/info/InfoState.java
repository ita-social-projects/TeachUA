package com.softserve.teachua.viberbot.state.info;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.menu.MenuState;
import lombok.SneakyThrows;

public class InfoState extends BotState {
    public InfoState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        return Action.MAIN_MENU.equals(action) ? new MenuState(session) : this;
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        final Message info = session.getContext()
                .getMessageFactory()
                .info(request.getSender());
        return objectMapper.writeValueAsString(info);

    }
}
