package com.softserve.teachua.service.impl;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.GmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class GmailServiceImpl implements GmailService {

    private static final String USER_ID = "me";

    private static final String FAILED_EMAILS_FILTER = "from:mailer-daemon@googlemail.com is:unread";

    private final Gmail gmail;

    private final CertificateService certificateService;

    public GmailServiceImpl(Gmail gmail, CertificateService certificateService) {
        this.gmail = gmail;
        this.certificateService = certificateService;
    }

    @Override
    public void detectFailedSendCertificates() {
        try {
            ListThreadsResponse threadsResponse = getMessagesResponse(1L);
            for (Thread thread : threadsResponse.getThreads()) {
                Thread fullThread = getThreadById(thread.getId());
                List<Certificate> certificates = getCertificatesByMessage(fullThread.getMessages().get(0));
                if (!certificates.isEmpty()) {
                    certificates.forEach(certificate ->
                        certificateService.updateDateAndSendStatus(certificate.getId(), false));
                    removeUnreadLabelFromThread(thread.getId());
                    log.info("Updated {} failed certificates for {}",
                        certificates.size(), certificates.get(0).getSendToEmail());
                }
            }
        } catch (IOException e) {
            log.error("IOException while process gmail inbox {}", e.getMessage());
        }
    }

    private List<Certificate> getCertificatesByMessage(Message messageWithCertificate) {
        List<MessagePartHeader> headers = messageWithCertificate.getPayload().getHeaders();
        MessagePartHeader dateHeader = headers.get(2);
        MessagePartHeader sentToHeader = headers.get(4);

        TemporalAccessor parse = DateTimeFormatter.RFC_1123_DATE_TIME.parse(
            dateHeader.getValue(), new ParsePosition(0)
        );

        LocalDate sendDate = LocalDate.from(parse);
        String userEmail = sentToHeader.getValue();

        List<Certificate> certificates = certificateService
            .getSentCertificatesByEmailAndUpdateStatus(userEmail, sendDate);

        if (certificates.isEmpty()) {
            log.warn("Gmail has failed email for {} with date {}, but no certificate found in database",
                userEmail, sendDate);
        }
        return certificates;
    }

    private ListThreadsResponse getMessagesResponse(long maxResults) throws IOException {
        return gmail.users()
            .threads()
            .list(USER_ID)
            .setQ(FAILED_EMAILS_FILTER)
            .setMaxResults(maxResults)
            .execute();
    }

    private Thread getThreadById(String threadId) throws IOException {
        return gmail.users()
            .threads()
            .get(USER_ID, threadId)
            .setFormat("metadata")
            .execute();
    }

    public void removeUnreadLabelFromThread(String threadId) throws IOException {
        ModifyThreadRequest request = new ModifyThreadRequest()
            .setRemoveLabelIds(Collections.singletonList("UNREAD"));
        gmail.users()
            .threads()
            .modify(USER_ID, threadId, request)
            .execute();
    }
}
