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
import java.util.Set;

@Service
@Slf4j
@Getter
public class ScheduleSendServiceImpl implements ScheduleSendService {
    private static final String SCHEDULED_TASKS = "scheduleSendService";

    private final EmailService emailService;

    private final ScheduledAnnotationBeanPostProcessor postProcessor;

    private final CertificateService certificateService;

    public int position = 0;

    @Autowired
    public ScheduleSendServiceImpl(EmailService emailService, ScheduledAnnotationBeanPostProcessor postProcessor, CertificateService certificateService) {
        this.emailService = emailService;
        this.postProcessor = postProcessor;
        this.certificateService = certificateService;
    }

    //@Override
    //@Scheduled(fixedRate = 300000)
    @Scheduled(fixedRate = 10000)
    public void sendCertificateWithScheduler() {
        CertificateTransfer user = getOneUserFromTheList();
        if (user != null) {
            emailService.sendMessageWithAttachmentAndGeneratedPdf(user.getSendToEmail(),
                    "Certificate.",
                    "Вітаю! В додатку ви можете знайти ваш сертифікат.",
                    user.getUserName(),
                    user.getDates());
            user.setSendStatus(true);
            user.setUpdateStatus(LocalDate.now());
        } else {
            postProcessor.destroy();
            log.info("No e-mails to send");
            position = 0;
        }
    }

    public CertificateTransfer getOneUserFromTheList() {
        CertificateTransfer result = null;
        if (certificateService.getListOfUnsentCertificates().size() > position){
            result = certificateService.getListOfUnsentCertificates().get(position++);
        }
        return result;
    }

    public void startSchedule() {
        getPostProcessor().postProcessBeforeDestruction(this, SCHEDULED_TASKS);
    }

    public void stopSchedule() {
        getPostProcessor().postProcessAfterInitialization(this, SCHEDULED_TASKS);
    }

    public TaskSchedule listSchedules() {
        return new TaskSchedule(getPostProcessor().getScheduledTasks());
    }
}
