package com.softserve.certificate.service;

import com.softserve.certificate.dto.schedule.TaskSchedule;

/**
 * This interface contains method to manage scheduling for sending certificate via email.
 */

public interface ScheduleSendService {
    /**
     * The method start scheduling.
     */
    void startSchedule();

    /**
     * The method stop scheduling.
     */
    void stopSchedule();

    /**
     * The method get list of scheduled tasks.
     *
     * @return new {@code TaskSchedule}
     */
    TaskSchedule listSchedules();
}
