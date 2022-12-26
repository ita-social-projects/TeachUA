package com.softserve.teachua.service.impl;

import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.GmailService;
import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.gimap.GmailSSLStore;
import com.sun.mail.gimap.GmailThrIdTerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
public class GmailServiceImpl implements GmailService {

    private static final String FAILED_EMAILS_FILTER = "mailer-daemon@googlemail.com";

    private static final String PROTOCOL = "gimaps";

    private Folder certificatesFolder;

    private final Session IMAPSession;

    private final CertificateService certificateService;

    public GmailServiceImpl(Session IMAPSession, CertificateService certificateService) {
        this.IMAPSession = IMAPSession;
        this.certificateService = certificateService;
    }

    @Override
    public void detectFailedSendCertificates() {
        try {
            GmailSSLStore store = (GmailSSLStore) IMAPSession.getStore(PROTOCOL);
            store.connect();

            Folder inboxFolder = store.getFolder("INBOX");
            inboxFolder.open(Folder.READ_WRITE);
            Message[] messagesAboutUndelivered = getMessagesAboutUndelivered(inboxFolder);
            log.info("Messages about undelivered: {}", messagesAboutUndelivered.length);

            certificatesFolder = store.getFolder("Сертифікати");
            certificatesFolder.open(Folder.READ_WRITE);
            findMessagesWithCertificate(messagesAboutUndelivered);

            inboxFolder.close(true);
            certificatesFolder.close(true);
            store.close();
        } catch (MessagingException e) {
            log.error("Exception while process gmail inbox {}", e.getMessage());
        }
    }

    private Message[] getMessagesAboutUndelivered(Folder inboxFolder) throws MessagingException {
        SearchTerm sender = new FromTerm(new InternetAddress(FAILED_EMAILS_FILTER));
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseen = new FlagTerm(seen, false);
        SearchTerm failedAndUnseen = new AndTerm(sender, unseen);

        return inboxFolder.search(failedAndUnseen);
    }

    private void findMessagesWithCertificate(Message[] messagesAboutUndelivered) throws MessagingException {
        for (Message message : messagesAboutUndelivered) {
            long threadId = ((GmailMessage) message).getThrId();
            Message[] messagesWithCertificate = certificatesFolder.search(new GmailThrIdTerm(threadId));
            if (messagesWithCertificate.length == 0) {
                message.setFlag(Flags.Flag.SEEN, true);
                continue;
            }
            processFailedMessage(messagesWithCertificate[0], message);
        }
    }

    private void processFailedMessage(Message failedMessage, Message messageAboutUndelivered) throws MessagingException {
        String sendToEmail = failedMessage.getAllRecipients()[0].toString();
        LocalDate updateStatus = failedMessage.getSentDate().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate();

        List<Certificate> certificates = certificateService.getSentCertificatesByEmailAndUpdateStatus(
            sendToEmail, updateStatus);
        if (!certificates.isEmpty()) {
            certificates.forEach(certificate ->
                certificateService.updateDateAndSendStatus(certificate.getId(), false));
            failedMessage.setFlag(Flags.Flag.DELETED, true);
            certificatesFolder.expunge();
            messageAboutUndelivered.setFlag(Flags.Flag.SEEN, true);

            log.info("Updated {} failed certificates for {}",
                certificates.size(), certificates.get(0).getSendToEmail());
        } else {
            log.info("Gmail has failed email for {} with date {}, but no certificate found in database",
                sendToEmail, updateStatus);
        }
    }
}
