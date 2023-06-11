package com.softserve.certificate.controller;

import com.softserve.certificate.dto.schedule.TaskSchedule;
import com.softserve.certificate.service.ScheduleSendService;
import com.softserve.certificate.utils.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the scheduling of sending certificates.
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/certificate/scheduler")
public class ScheduleSendEmailController {
    private final ScheduleSendService scheduleSendService;

    public ScheduleSendEmailController(ScheduleSendService scheduleSendService) {
        this.scheduleSendService = scheduleSendService;
    }

    /**
     * Use this endpoint to stop scheduling.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping
    public void stopSchedule() {
        scheduleSendService.stopSchedule();
        log.info("Scheduler was stopped.");
    }

    /**
     * Use this endpoint to start scheduling.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public void startSchedule() {
        scheduleSendService.startSchedule();
        log.info("Scheduler running.");
    }

    /**
     * Use this endpoint to get scheduled task. The controller returns dto {@code TaskSchedule} information about
     * scheduled task.
     *
     * @return new {@code TaskSchedule}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping
    public TaskSchedule listSchedules() {
        TaskSchedule taskSchedule = scheduleSendService.listSchedules();
        log.info(taskSchedule.getTasks().isEmpty() ? "Currently no scheduler tasks are running"
                : "Task is working: " + scheduleSendService.listSchedules());
        return taskSchedule;
    }
}
