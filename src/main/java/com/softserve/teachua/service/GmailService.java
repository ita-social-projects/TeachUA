package com.softserve.teachua.service;

/**
 * This service contains methods to manage Gmail API
 */
public interface GmailService {
    /**
     * This method checks gmail inbox for unread threads with mailer-daemon@googlemail.com,
     * then gets first message and mark its certificate as unsent
     */
    void detectFailedSendCertificates();
}
