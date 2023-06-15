package com.softserve.teachua.service.impl.viberbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.viberbot.RequestDto;
import com.softserve.teachua.dto.viberbot.Sender;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.MessengerService;
import com.softserve.teachua.service.viberbot.BotService;
import com.softserve.teachua.service.viberbot.MessageDeliveryService;
import com.softserve.teachua.service.viberbot.MessageFactory;
import com.softserve.teachua.utils.viberbot.enums.CallbackType;
import com.softserve.teachua.viberbot.state.Context;
import com.softserve.teachua.viberbot.state.Session;
import com.softserve.teachua.viberbot.state.auth.PreAuthState;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BotServiceImpl implements BotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotServiceImpl.class);
    private final MessageDeliveryService messageDelivery;
    private final Map<Sender, Session> sessionBySender;
    private final ObjectMapper objectMapper;
    private final MessageFactory messageFactory;
    private final MessengerService messengerService;
    private final CertificateService certificateService;

    @Autowired
    public BotServiceImpl(MessageDeliveryService messageDelivery, MessageFactory messageFactory,
                          MessengerService messengerService, CertificateService certificateService) {
        this.messageDelivery = messageDelivery;
        this.messengerService = messengerService;
        this.certificateService = certificateService;
        this.sessionBySender = new ConcurrentHashMap<>();
        this.objectMapper = new ObjectMapper();
        this.messageFactory = messageFactory;
    }

    @Override
    public String incoming(RequestDto request) {
        CallbackType callbackType = CallbackType.of(request.getEvent());
        Sender sender = request.getSender();
        switch (callbackType) {
            case MESSAGE:
                final Session session = sessionBySender.computeIfAbsent(sender, this::newSession);
                final String message = session.handle(request);
                messageDelivery.send(message);
                break;
            case CONVERSATION_STARTED:
                return welcomeMessage();
        }
        return "";
    }

    @SneakyThrows
    private String welcomeMessage() {
        return objectMapper.writeValueAsString(messageFactory.welcomeMessage());
    }

    private Session newSession(Sender sender) {
        final Session session = new Session(sender);
        session.setState(new PreAuthState(session));
        final Context context = new Context(messageFactory, messengerService, certificateService);
        session.setContext(context);
        return session;
    }

}
