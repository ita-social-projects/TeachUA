package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateDates;

/**
 * This interface contains methods to work with emails.
 */

public interface EmailService {
    /**
     * The method handle all additional methods and send email to proper user.
     *
     * @param to       - indicate receiver.
     * @param subject  - indicate subject of email.
     * @param text     - indicate body text.
     * @param userName - indicate username of user.
     * @param date     - indicate date on certificate.
     */
    void sendMessageWithAttachmentAndGeneratedPdf(String to, String subject, String text,
                                                  String userName, CertificateDates date);
}
