package com.softserve.teachua.service.impl;

import com.softserve.teachua.config.ConfigureSMTPProperties;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.EmailService;
import com.sun.mail.smtp.SMTPAddressFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value(value = "${spring.mail.username}")
    private String SEND_FROM_ADDRESS;

    private final ConfigureSMTPProperties emailSender;

    private final CertificateService certificateService;

    private MimeBodyPart pdfBodyPart;

    @Autowired
    public EmailServiceImpl(ConfigureSMTPProperties emailSender, CertificateService certificateService) {
        this.emailSender = emailSender;
        this.certificateService = certificateService;
    }

    @Override
    public void sendMessageWithAttachmentAndGeneratedPdf(String to, String subject, String text,
                                                         String userName, CertificateDates date, CertificateTransfer certificateTransfer) {
        MimeMessage message = emailSender.getJavaMailSender().createMimeMessage();

//        CertificateTransfer userInformation = CertificateTransfer.builder()
//                .userName(userName)
//                .dates(date)
//                .build();

        byte[] bytes = certificateService.getPdfOutput(certificateTransfer);

        try {
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("certificate.pdf");
        } catch (MessagingException exception) {
            log.info("Problem with creation pdf certificate. Pdf body part.");
            exception.printStackTrace();
        }

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SEND_FROM_ADDRESS);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.getMimeMultipart().addBodyPart(pdfBodyPart);
        } catch (SMTPAddressFailedException exception) {
            log.info("Problem with email or attached file.");
        } catch (MailSendException | MessagingException exception) {
            log.info("Problems with sending email");
        }

        emailSender.getJavaMailSender().send(message);
        log.info("Message was successfully send to: " + to);
    }
}
