package com.softserve.teachua.service.viberbot;

import com.softserve.teachua.dto.viberbot.Sender;
import com.softserve.teachua.dto.viberbot.message.Message;

public interface MessageFactory {
    Message mainMenu(Sender sender);
    Message info(Sender sender);
    Message certificates(Sender sender);
    Message certificate(Sender sender, String certName);
    Message welcomeMessage();
    Message auth(Sender sender);
    Message authEmail(Sender sender);
    Message authName(Sender sender);

    Message nameAuthFailed(Sender sender);

    Message emailAuthFailed(Sender sender);
}
