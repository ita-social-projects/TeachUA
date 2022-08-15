package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendMessageWithAttachmentAndGeneratedPdf(String to, String subject, String text,
                                                         String name, String userName, String date) {

    }

    @Override
    public void constructPdfBodyPart(byte[] bytes) {

    }

    @Override
    public void constructMimeMessageHelper(String to, String subject, String text, MimeMessage message) {

    }
}
