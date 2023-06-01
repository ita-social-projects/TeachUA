package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.schedule.TaskSchedule;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.EmailService;
import com.softserve.teachua.service.GmailService;
import com.softserve.teachua.service.ScheduleSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleSendServiceImpl implements ScheduleSendService {
    private static final String SCHEDULED_TASKS = "scheduleSendService";

    private final EmailService emailService;

    private final GmailService gmailService;

    private final ScheduledAnnotationBeanPostProcessor postProcessor;

    private final CertificateService certificateService;

    @Autowired
    public ScheduleSendServiceImpl(EmailService emailService, GmailService gmailService,
                                   ScheduledAnnotationBeanPostProcessor postProcessor,
                                   CertificateService certificateService) {
        this.emailService = emailService;
        this.gmailService = gmailService;
        this.postProcessor = postProcessor;
        this.certificateService = certificateService;
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Scheduled(fixedRate = 180000) // 1 email / 3 min
    public void sendCertificateWithScheduler() {
        CertificateTransfer certificate = certificateService.getOneUnsentCertificate();

        if (certificate != null) {
            log.info("Preparing message for {}, id={}", certificate, certificate.getId());
            try {
                emailService.sendMessageWithAttachmentAndGeneratedPdf(certificate.getSendToEmail().trim(),
                        "Сертифікат проєкту \"Єдині\"",
                        """
                                Вітаємо! Дякуємо Вам, що долучилися до проєкту "Єдині". Дякуємо за спільну роботу задля великої мети.

                                Ваш сертифікат додано у вкладенні до цього листа.""",
                        certificate);
                certificateService.updateDateAndSendStatus(certificate.getId(), true);
            } catch (Exception e) {
                log.error("Error send certificate {} {}", e.getClass(), e.getMessage());
                certificateService.updateDateAndSendStatus(certificate.getId(), false);
            }
        } else {
            postProcessor.destroy();
            log.info("Scheduled Certification Service. Done. New task not found.");
        }
        gmailService.detectFailedSendCertificates();
    }

    public void startSchedule() {
        postProcessor.postProcessAfterInitialization(this, SCHEDULED_TASKS);
    }

    public void stopSchedule() {
        postProcessor.postProcessBeforeDestruction(this, SCHEDULED_TASKS);
    }

    public TaskSchedule listSchedules() {
        return new TaskSchedule(postProcessor.getScheduledTasks());
    }
}
