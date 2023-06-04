package com.softserve.certificate.service.impl;

import com.softserve.certificate.config.ConfigureSMTPProperties;
import com.softserve.certificate.dto.certificate.CertificateTransfer;
import com.softserve.certificate.service.CertificateService;
import com.softserve.certificate.service.EmailService;
import com.sun.mail.smtp.SMTPAddressFailedException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value(value = "${spring.mail.username}")
    private String sendFromAddress;

    private final ConfigureSMTPProperties emailSender;

    private final CertificateService certificateService;

    @SuppressWarnings("squid:S1450") //Suppressed because of project's business logic.
    private MimeBodyPart pdfBodyPart;

    @Autowired
    public EmailServiceImpl(ConfigureSMTPProperties emailSender, CertificateService certificateService) {
        this.emailSender = emailSender;
        this.certificateService = certificateService;
    }

    @Override
    public void sendMessageWithAttachmentAndGeneratedPdf(String to, String subject, String text,
                                                         CertificateTransfer certificateTransfer) {
        MimeMessage message = emailSender.getJavaMailSender().createMimeMessage();

        byte[] bytes = certificateService.getPdfOutput(certificateTransfer);

        try {
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("certificate.pdf");
        } catch (MessagingException exception) {
            log.warn("Problem with creation pdf certificate. Pdf body part. {}", exception.getMessage());
        }

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sendFromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.getMimeMultipart().addBodyPart(pdfBodyPart);
        } catch (SMTPAddressFailedException exception) {
            log.warn("Problem with email or attached file. {}", exception.getMessage());
        } catch (MailSendException | MessagingException exception) {
            log.warn("Problems with sending email. {}", exception.getMessage());
        }

        emailSender.getJavaMailSender().send(message);
        log.info("Message was successfully send to: " + to);
    }
}
