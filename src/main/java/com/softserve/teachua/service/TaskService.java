package com.softserve.teachua.service;

import com.softserve.teachua.dto.task.TaskNameProfile;
import com.softserve.teachua.model.Task;

public interface TaskService {
    TaskNameProfile deleteTask(Long id);

    Task getTaskById(Long id);
}
