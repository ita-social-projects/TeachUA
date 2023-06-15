package com.softserve.teachua.viberbot.state.auth;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import lombok.SneakyThrows;

public class CheckEmailState extends BotState {

    protected CheckEmailState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        if (action == Action.USER_INPUT) {
            String email = request.getMessage().getText();
            if (session.getContext().getCertificateService().existByEmail(email)) {
                session.getContext().getUserDetails().setEmail(email);
                return new CheckNameState(session);
            } else {
               return new CheckEmailFailedState(session);
            }
        }
        return this;
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        final Message message = session.getContext()
                .getMessageFactory()
                .authEmail(request.getSender());
        return objectMapper.writeValueAsString(message);
    }
}
