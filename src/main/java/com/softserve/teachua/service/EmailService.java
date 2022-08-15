package com.softserve.teachua.service;

import javax.mail.internet.MimeMessage;

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
     * @param name     - indicate name of user.
     * @param userName - indicate username of user.
     * @param date     - indicate date on certificate.
     */
    void sendMessageWithAttachmentAndGeneratedPdf(String to, String subject, String text, String name,
                                                  String userName, String date);

    /**
     * The method generate certificate.
     *
     * @param bytes - accepts bytes to construct pdf body part.
     */
    void constructPdfBodyPart(byte[] bytes);

    /**
     * The method construct mime message.
     *
     * @param to      - indicate receiver.
     * @param subject - indicate subject of email.
     * @param text    - indicate body text.
     * @param message - indicate that current mime message can accept attachment.
     */
    void constructMimeMessageHelper(String to, String subject, String text, MimeMessage message);
}
