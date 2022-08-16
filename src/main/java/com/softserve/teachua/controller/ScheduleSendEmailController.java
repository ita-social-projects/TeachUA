package com.softserve.teachua.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.service.impl.ScheduleSendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
public class ScheduleSendEmailController {
    private static final String SCHEDULED_TASKS = "scheduleSendService";

    private final ScheduleSendServiceImpl scheduleSendService;

    public ScheduleSendEmailController(ScheduleSendServiceImpl scheduleSendService) {
        this.scheduleSendService = scheduleSendService;
    }

    @DeleteMapping(value = "/scheduler")
    public void stopSchedule(){
        scheduleSendService.getPostProcessor()
                .postProcessBeforeDestruction(scheduleSendService, SCHEDULED_TASKS);
        log.info("Scheduler was stopped.");
    }

    @PostMapping(value = "/scheduler")
    public void startSchedule(){
        scheduleSendService.getPostProcessor()
                .postProcessAfterInitialization(scheduleSendService, SCHEDULED_TASKS);
        log.info("Scheduler running.");
    }

    @GetMapping(value = "/scheduler")
    public void listSchedules(){
        Set<ScheduledTask> setTasks = scheduleSendService.getPostProcessor().getScheduledTasks();
        if(!setTasks.isEmpty()){
            log.info("Task is working: " + setTasks);
        }else{
            log.info("Currently no scheduler tasks are running");
        }
    }
}
