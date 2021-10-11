package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.task.CreateTask;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class TaskController implements Api {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/challenge/{id}/tasks")
    public Page<TaskPreview> getTasksByChallenge(
            @PathVariable Long id,
            @PageableDefault(value = 2, sort = "startDate") Pageable pageable)
    {
        return taskService.getTasksByChallengeId(id, pageable);
    }

    @PostMapping("/challenge/{id}/task")
    public SuccessCreatedTask createTask(@PathVariable Long id, @RequestBody CreateTask createTask) {
        return taskService.createTask(id, createTask);
    }

    @GetMapping("/challenge/task/{id}")
    public TaskProfile getTask(@PathVariable("id") Long id) {
        return taskService.getTask(id);
    }
}
