package com.softserve.teachua.tools.transfer_service;

import com.softserve.teachua.dto.task.SuccessCreatedTask;

import java.util.List;

public interface TaskTransferService {
    List<SuccessCreatedTask> createTasksFromFile(String filePath);
}
