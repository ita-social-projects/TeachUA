package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller is for managing the tasks.
 * */

@RestController
@Slf4j
@Tag(name = "task", description = "the Task API")
@SecurityRequirement(name = "api")
public class TaskController implements Api {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Use this endpoint to get all tasks of challenge, including tasks that have not yet begun.
     * This feature available only for admins.
     * The controller returns {@code List<TaskPreview>}.
     *
     * @param id - put challenge id here.
     * @return {@code List<TaskPreview>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/challenge/{id}/tasks")
    public List<TaskPreview> getTasksByChallenge(@PathVariable Long id) {
        return taskService.getTasksByChallengeId(id);
    }

    /**
     * Use this endpoint to get all tasks despite challenge.
     * The controller returns {@code List <TaskPreview>}.
     *
     * @return new {@code List<TaskPreview>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/tasks")
    public List<TaskPreview> getTasks() {
        return taskService.getListOfTasks();
    }

    /**
     * Use this endpoint to get full information about task.
     * The controller returns {@code TaskProfile}.
     *
     * @param id - put task id here.
     * @return {@code TaskProfile}
     */
    @GetMapping("/challenge/task/{id}")
    public TaskProfile getTask(@PathVariable("id") Long id) {
        return taskService.getTask(id);
    }

    /**
     * Use this endpoint to create and add new task to challenge.
     * This feature available only for admins.
     * The controller returns {@code SuccessCreatedTask}.
     *
     * @param id         - put challenge id here.
     * @param createTask - put required parameters here.
     * @return {@code SuccessCreatedTask}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/challenge/{id}/task")
    public SuccessCreatedTask createTask(@PathVariable Long id, @Valid @RequestBody CreateTask createTask) {
        return taskService.createTask(id, createTask);
    }

    /**
     * Use this endpoint to update some values of task, including the id of the challenge to which it is linked.
     * This feature available only for admins.
     * The controller returns {@code SuccessUpdatedTask}.
     *
     * @param id         - put task id here.
     * @param updateTask - put new and old parameters here.
     * @return {@code SuccessUpdatedTask} - shows result of updating task.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/challenge/task/{id}")
    public SuccessUpdatedTask updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTask updateTask) {
        return taskService.updateTask(id, updateTask);
    }

    /**
     * Use this endpoint to archive task.
     * This feature available only for admins.
     * The controller returns {@code TaskProfile}.
     *
     * @param id - put task id here.
     * @return {@code TaskProfile} - shows which task was removed.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/challenge/task/{id}")
    public TaskProfile deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}
