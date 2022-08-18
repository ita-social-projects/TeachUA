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

    private int position = 0;
    private List<CertificateTransfer> certificateTransfers;

    @Autowired
    public ScheduleSendServiceImpl(EmailService emailService, ScheduledAnnotationBeanPostProcessor postProcessor, CertificateService certificateService) {
        this.emailService = emailService;
        this.postProcessor = postProcessor;
        this.certificateService = certificateService;
    }

    @Scheduled(fixedRate = 10000)
    public void sendCertificateWithScheduler() {
        CertificateTransfer user = getOneUserFromTheList(); // userCertificateTransfer
        if (user != null) {
            emailService.sendMessageWithAttachmentAndGeneratedPdf(user.getSendToEmail(),
                    "Certificate.",
                    "Вітаю! В додатку ви можете знайти ваш сертифікат.",
                    user);
        } else {
            postProcessor.destroy();
            log.info("Scheduled Certification Service. Done. New task not found.");
            position = 0;
        }
    }

    public CertificateTransfer getOneUserFromTheList() {
        CertificateTransfer result = null;
        if ((certificateTransfers != null)
                && (certificateTransfers.size() > position)) {
            result = certificateTransfers.get(position);
            position++;
        }
        return result;
    }

    public void startSchedule() {
        position = 0;
        certificateTransfers = certificateService.getListOfUnsentCertificates();
        postProcessor.postProcessAfterInitialization(this, SCHEDULED_TASKS);
    }

    public void stopSchedule() {
        position = 0;
        certificateTransfers = null;
        postProcessor.postProcessBeforeDestruction(this, SCHEDULED_TASKS);
    }

    public TaskSchedule listSchedules() {
        return new TaskSchedule(postProcessor.getScheduledTasks());
    }
}
