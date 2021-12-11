package com.softserve.teachua.tools.transfer_service;

import com.softserve.teachua.dto.task.SuccessCreatedTask;

import java.util.List;

public interface TaskTransferService {
    /**
     * The method returns list of dto {@code List<SuccessCreatedTask>} if tasks successfully added.
     *
     * @param filePath - path to file with jsons of tasks.
     * @return new {@code List<SuccessCreatedTask>}.
     */
    List<SuccessCreatedTask> createTasksFromFile(String filePath);

    /**
     * The method returns list of dto {@code List<SuccessCreatedTask>} if tasks successfully added.
     *
     * @return new {@code List<SuccessCreatedTask>}.
     */
    List<SuccessCreatedTask> createTasksFromRepository();
}
