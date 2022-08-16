package com.softserve.teachua.service;

import com.softserve.teachua.dto.schedule.TaskSchedule;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.Set;

/**
 * This interface contains method to manage scheduling for sending certificate via email.
 */

public interface ScheduleSendService {
    /**
     * The method schedule sending of certificate.
     */
    //void sendCertificateWithScheduler();

    void startSchedule();

    void stopSchedule();

    TaskSchedule listSchedules();
}
