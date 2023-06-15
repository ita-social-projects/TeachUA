package com.softserve.teachua.viberbot.state.auth;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.UserDetails;
import com.softserve.teachua.utils.viberbot.enums.Action;
import com.softserve.teachua.viberbot.state.BotState;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.menu.MenuState;
import lombok.SneakyThrows;

public class CheckNameState extends BotState {

    protected CheckNameState(Session session) {
        super(session);
    }

    @Override
    public BotState handle(Action action, RequestDto request) {
        switch (action) {
            case BACK: return new CheckEmailState(session);
            case USER_INPUT: {
                final UserDetails userDetails = session.getContext().getUserDetails();
                if (session.getContext().getCertificateService().existByEmailAndName(
                        userDetails.getEmail(),
                        request.getMessage().getText())) {
                    userDetails.setName(request.getMessage().getText());
                    userDetails.setUserId(request.getSender().getId());
                    session.getContext().getMessengerService().createMessenger(
                        userDetails.getEmail(), userDetails.getUserId()
                    );
                    return new MenuState(session);
                } else {
                    return new CheckNameFailedState(session);
                }
            }
        }
        return this;
    }

    @SneakyThrows
    @Override
    public String getMessage(RequestDto request) {
        return objectMapper.writeValueAsString(
                session.getContext().getMessageFactory().authName(request.getSender())
        );
    }
}
