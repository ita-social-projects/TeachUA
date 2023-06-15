package com.softserve.teachua.viberbot.state;

import com.softserve.teachua.dto.viberbot.UserDetails;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.MessageService;
import com.softserve.teachua.service.MessengerService;
import com.softserve.teachua.service.viberbot.MessageFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@EqualsAndHashCode
public final class Context {
    private final MessageFactory messageFactory;
    private final MessengerService messengerService;
    private final CertificateService certificateService;
    private final UserDetails userDetails;

    public Context(MessageFactory messageFactory, MessengerService messengerService, CertificateService certificateService) {
        this.messageFactory = messageFactory;
        this.messengerService = messengerService;
        this.certificateService = certificateService;
        this.userDetails = new UserDetails();
    }
}
