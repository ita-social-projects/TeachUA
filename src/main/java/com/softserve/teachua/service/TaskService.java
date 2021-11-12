package com.softserve.teachua.service;

import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.model.Task;

import java.util.List;

import java.util.List;

public interface TaskService {
    TaskProfile deleteTask(Long id);

    Task getTaskById(Long id);

    List<TaskPreview> getTasksByChallengeId(Long id);

    List<TaskPreview> getListOfTasks();

    TaskProfile getTask(Long id);

    SuccessCreatedTask createTask(Long id, CreateTask createTask);

    SuccessUpdatedTask updateTask(Long id, UpdateTask updateTask);
}
