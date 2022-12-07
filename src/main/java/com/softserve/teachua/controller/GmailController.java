package com.softserve.teachua.controller;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.softserve.teachua.controller.marker.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GmailController implements Api {

    private final Gmail gmailService;

    public GmailController(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/gmail")
    public void getEmails() throws IOException {
        String user = "me";
        ListMessagesResponse messagesResponse = null;
        try {
            messagesResponse = gmailService.users()
                .messages()
                .list(user)
                .setQ("from:mailer-daemon@googlemail.com is:unread")
                .setMaxResults(3L)
                .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Message> messages = messagesResponse.getMessages();
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Messages:");
            for (Message msg : messages) {
                Message message = gmailService.users()
                    .messages()
                    .get(user, msg.getId())
                    .execute();
                System.out.println("-------------------");
                System.out.println("Raw " + message.getRaw());
                System.out.println("Payload " + message.getPayload());
                System.out.println("Snippet " + message.getSnippet());
                System.out.println("LabelIds " + message.getLabelIds());
                System.out.println("InternalDate " + message.getInternalDate());
                System.out.println("SizeEstimate " + message.getSizeEstimate());
                System.out.println("HistoryId " + message.getHistoryId());
                System.out.println("ThreadId " + message.getThreadId());
            }
        }
    }
}
