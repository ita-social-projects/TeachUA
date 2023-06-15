package com.softserve.teachua.service.impl.viberbot;

import com.softserve.teachua.dto.viberbot.Sender;
import com.softserve.teachua.dto.viberbot.message.Button;
import com.softserve.teachua.dto.viberbot.message.Frame;
import com.softserve.teachua.dto.viberbot.message.Message;
import com.softserve.teachua.dto.viberbot.message.MessageKeyboard;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.viberbot.MessageFactory;
import com.softserve.teachua.utils.viberbot.constants.TextConstants;
import com.softserve.teachua.utils.viberbot.enums.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.teachua.viberbot.state.TagBuilder.defaultColor;;

@Service
public class MessageFactoryImpl implements MessageFactory {

    private final CertificateService certificatesService;

    @Autowired
    public MessageFactoryImpl(CertificateService certificatesService) {
        this.certificatesService = certificatesService;
    }

    @Override
    public Message mainMenu(Sender sender) {
        return Message.builder()
                .text(TextConstants.MENU_MESSAGE)
                .receiver(sender.getId())
                .keyboard(getMessageKeyboard(getMenuButtons()))
                .build();
    }

    @Override
    public Message info(Sender sender) {
        return Message.builder()
                .receiver(sender.getId())
                .keyboard(getSingleButtonKeyboard(TextConstants.MENU_MENU, Action.MAIN_MENU.name()))
                .text(TextConstants.INFO_MESSAGE)
                .build();
    }

    @Override
    public Message certificates(Sender sender) {
        List<Certificate> certificates = certificatesService.getByMessengerId(sender.getId());
        String text = certificates.isEmpty() ?
                TextConstants.NOT_FOUND_CERT_MESSAGE :
                TextConstants.CERT_MESSAGE;
        return Message.builder()
                .text(text)
                .receiver(sender.getId())
                .keyboard(getMessageKeyboard(getCertificatesButton(certificates)))
                .build();
    }

    @Override
    public Message certificate(Sender sender, String certName) {
        return Message.builder()
                .text(TextConstants.CERTIFICATE_REQUEST_MESSAGE)
                .receiver(sender.getId())
                .keyboard(getMessageKeyboard(Collections.singletonList(createMenuButton())))
                .build();
    }

    @Override
    public Message welcomeMessage() {
        MessageKeyboard keyboard = getSingleButtonKeyboard(TextConstants.START_CONV, Action.MAIN_MENU.name());
        return Message.builder()
                .text(TextConstants.WELCOME_MESSAGE)
                .keyboard(keyboard)
                .build();
    }

    @Override
    public Message auth(Sender sender) {
        return Message.builder()
                .text(TextConstants.AUTH_MESSAGE)
                .receiver(sender.getId())
                .keyboard(getSingleButtonKeyboard(TextConstants.AUTH, Action.AUTH.name()))
                .build();
    }

    @Override
    public Message authEmail(Sender sender) {
        return Message.builder()
                .text(TextConstants.AUTH_EMAIL_MESSAGE)
                .receiver(sender.getId())
                .build();
    }

    @Override
    public Message authName(Sender sender) {
        return Message.builder()
                .text(TextConstants.AUTH_NAME_MESSAGE)
                .receiver(sender.getId())
                .build();
    }

    @Override
    public Message nameAuthFailed(Sender sender) {
        return Message.builder()
                .text(TextConstants.AUTH_FAILED_MESSAGE)
                .keyboard(getSingleButtonKeyboard(TextConstants.BACK, Action.BACK.name()))
                .receiver(sender.getId())
                .build();
    }

    @Override
    public Message emailAuthFailed(Sender sender) {
        return Message.builder()
                .text(TextConstants.AUTH_FAILED_MESSAGE)
                .receiver(sender.getId())
                .build();
    }

    private List<Button> getCertificatesButton(List<Certificate> certificates) {
        List<Button> buttons = certificates.stream().map(certificate ->
                        createButton(String.valueOf(certificate.getId()),
                            TextConstants.CERT_PREFIX + certificate.getId()))
                .collect(Collectors.toList());
        buttons.add(createMenuButton());
        return buttons;
    }
    private MessageKeyboard getMessageKeyboard(List<Button> buttons) {
        return MessageKeyboard.builder()
                .buttons(buttons)
                .build();
    }
    private List<Button> getMenuButtons() {
        return Arrays.asList(
                createButton(TextConstants.MENU_CERTIFICATES, Action.CERTIFICATES.name()),
                createButton(TextConstants.MENU_INFO, Action.INFO.name())
        );
    }

    private Button createButton(String name, String actionBody) {
        return Button.builder()
                .frame(Frame.builder().build())
                .text(defaultColor(name))
                .actionBody(actionBody)
                .build();
    }

    private Button createButton(String name, String actionBody, Integer columns) {
        return Button.builder()
                .frame(Frame.builder().build())
                .text(defaultColor(name))
                .actionBody(actionBody)
                .columns(columns)
                .build();
    }

    private MessageKeyboard getSingleButtonKeyboard(String name, String actionBody) {
        return  MessageKeyboard.builder()
                .buttons(Collections.singletonList(createButton(name, actionBody, 6)))
                .build();
    }
    private Button createMenuButton() {
        return Button.builder()
                .frame(Frame.builder().build())
                .text(defaultColor(TextConstants.MENU_MENU))
                .actionBody(Action.MAIN_MENU.name())
                .columns(6)
                .build();
    }

}
