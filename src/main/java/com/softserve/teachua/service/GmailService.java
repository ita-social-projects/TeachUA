package com.softserve.teachua.service;

import com.google.api.services.gmail.model.Message;
import com.softserve.teachua.model.Certificate;

import java.util.List;

/**
 * This service contains methods to manage Gmail API
 */
public interface GmailService {

    /**
     * This method returns certificates from database according to sendToEmail and sendDate of message with certificate
     *
     * @param messageWithCertificate first message of mailer-daemon thread (contains certificate)
     * @return list os failed sent certificates
     */
    List<Certificate> getCertificatesByMessage(Message messageWithCertificate);

    /**
     * This method checks inbox for unread thread with mailer-daemon@googlemail.com,
     * gets first message and mark its certificate as unsent
     */
    void detectFailedSendCertificates();
}
