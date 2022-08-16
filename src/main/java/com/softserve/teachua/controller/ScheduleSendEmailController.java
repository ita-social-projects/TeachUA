package com.softserve.teachua.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.schedule.TaskSchedule;
import com.softserve.teachua.service.ScheduleSendService;
import com.softserve.teachua.service.impl.ScheduleSendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
public class ScheduleSendEmailController implements Api {
    private static final String SCHEDULED_TASKS = "scheduleSendService";

    private final ScheduleSendService scheduleSendService;

    @Autowired
    public ScheduleSendEmailController(ScheduleSendService scheduleSendService) {
        this.scheduleSendService = scheduleSendService;
    }

    @DeleteMapping(value = "/scheduler")
    public void stopSchedule() {
        scheduleSendService.stopSchedule();
        log.info("Scheduler was stopped.");
    }

    @PostMapping(value = "/scheduler")
    public void startSchedule() {
        scheduleSendService.startSchedule();
        log.info("Scheduler running.");
    }

    @GetMapping(value = "/scheduler")
    public TaskSchedule listSchedules() {
        TaskSchedule taskSchedule = scheduleSendService.listSchedules();
        log.info(taskSchedule.getTasks().isEmpty()
                ?"Currently no scheduler tasks are running"
                :"Task is working: " + scheduleSendService.listSchedules());
        return taskSchedule;
    }
}
