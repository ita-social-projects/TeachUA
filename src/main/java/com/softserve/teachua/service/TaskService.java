package com.softserve.teachua.service;

import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskProfile deleteTask(Long id);

    Task getTaskById(Long id);

    Page<TaskPreview> getTasksByChallengeId(Long id, Pageable pageable);

    TaskProfile getTask(Long id);

    SuccessCreatedTask createTask(Long id, CreateTask createTask);

    SuccessUpdatedTask updateTask(Long id, UpdateTask updateTask);
}
