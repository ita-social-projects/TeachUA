package com.softserve.teachua.viberbot.state;

import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.Sender;
import com.softserve.teachua.utils.viberbot.enums.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);
    private final Sender sender;
    private BotState state;
    private Context context;

    public Session(Sender sender) {
        this.sender = sender;
    }

    public String handle(RequestDto request) {
        final String nameOfAction = request.getMessage().getText();
        Action action = Action.of(nameOfAction);
        state = state.handle(action, request);
        return state.getMessage(request);
    }

    public void setState(BotState state) {
        this.state = state;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Sender getSender() {
        return sender;
    }
}
