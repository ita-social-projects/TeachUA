package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class TaskController implements Api {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Use this endpoint to get all tasks of challenge, including tasks that have not yet begun.
     * This feature available only for admins.
     *
     * @param id - put challenge id here.
     * @return {@code Page<TaskPreview} - all tasks with pagination
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/challenge/{id}/tasks")
    public List<TaskPreview> getTasksByChallenge(@PathVariable Long id) {
        return taskService.getTasksByChallengeId(id);
    }

    /**
     * The controller returns information {@code List <TaskPreview>} about tasks.
     *
     * @return new {@code List<TaskPreview>}
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/tasks")
    public List<TaskPreview> getTasks() {
        return taskService.getListOfTasks();
    }

    /**
     * Use this endpoint to get full information about task.
     *
     * @param id - put task id here.
     * @return {@code TaskProfile}
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/challenge/task/{id}")
    public TaskProfile getTask(@PathVariable("id") Long id) {
        return taskService.getTask(id);
    }

    /**
     * Use this endpoint to create and add new task to challenge.
     * This feature available only for admins.
     *
     * @param id         - put challenge id here.
     * @param createTask - put required parameters here.
     * @return {@code SuccessCreatedTask}
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PostMapping("/challenge/{id}/task")
    public SuccessCreatedTask createTask(@PathVariable Long id, @Valid @RequestBody CreateTask createTask) {
        return taskService.createTask(id, createTask);
    }

    /**
     * Use this endpoint to update some values of task, including the id of the challenge to which it is linked.
     * This feature available only for admins.
     *
     * @param id         - put task id here.
     * @param updateTask - put new and old parameters here.
     * @return {@code SuccessUpdatedTask} - shows result of updating task.
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PutMapping("/challenge/task/{id}")
    public SuccessUpdatedTask updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTask updateTask) {
        return taskService.updateTask(id, updateTask);
    }

    /**
     * Use this endpoint to archive task.
     * This feature available only for admins.
     *
     * @param id - put task id here.
     * @return {@code TaskProfile} - shows which task was removed.
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @DeleteMapping("/challenge/task/{id}")
    public TaskProfile deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}
