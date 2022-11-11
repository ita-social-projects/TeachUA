package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.schedule.TaskSchedule;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.EmailService;
import com.softserve.teachua.service.ScheduleSendService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ScheduleSendServiceImpl implements ScheduleSendService {
    private static final String SCHEDULED_TASKS = "scheduleSendService";

    private final EmailService emailService;

    private final ScheduledAnnotationBeanPostProcessor postProcessor;

    private final CertificateService certificateService;

    private long previousId;

    @Autowired
    public ScheduleSendServiceImpl(EmailService emailService, ScheduledAnnotationBeanPostProcessor postProcessor,
            CertificateService certificateService) {
        this.emailService = emailService;
        this.postProcessor = postProcessor;
        this.certificateService = certificateService;
        previousId = 0;
    }

    // @Scheduled(fixedRate = 10000)
    @Scheduled(fixedRate = 180000) // 1 email / 3 min
    public void sendCertificateWithScheduler() {
        CertificateTransfer user = certificateService.getOneUnsentCertificate();
        if (user != null) {
            log.info("Generate Certificate for " + user.toString() + " id = " + user.getId());
            //
            if (previousId == 0) {
                previousId = user.getId();
            } else if (previousId == user.getId()) {
                log.error("Error Generate Certificate for " + user.toString() + " id = " + user.getId());
                certificateService.updateDateAndSendStatus(user.getId(), false);
                previousId = 0;
            }
        }
        //
        if (user != null) {
            emailService.sendMessageWithAttachmentAndGeneratedPdf(user.getSendToEmail().trim(),
                    // "Certificate.",
                    // "Вітаю! В додатку ви можете знайти ваш сертифікат.",
                    "Сертифікат проєкту \"Єдині\"",
                    "Вітаємо! Дякуємо Вам, що долучилися до проєкту \"Єдині\". Дякуємо за спільну роботу задля великої мети."
                            + "\n\nВаш сертифікат додано у вкладенні до цього листа.",
                    user);
            certificateService.updateDateAndSendStatus(user.getId(), true);
            //
            previousId = 0;
        } else {
            postProcessor.destroy();
            log.info("Scheduled Certification Service. Done. New task not found.");
            previousId = 0;
        }
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
