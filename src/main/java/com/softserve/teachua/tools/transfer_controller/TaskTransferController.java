package com.softserve.teachua.tools.transfer_controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.tools.transfer_service.TaskTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskTransferController implements Api {
    private final TaskTransferService taskTransferService;

    @Autowired
    public TaskTransferController(TaskTransferService taskTransferService) {
        this.taskTransferService = taskTransferService;
    }

    /**
     * Use this endpoint to create task from file.
     * The controller returns list of dto {@code List<SuccessCreatedTask>} of created tasks.
     *
     * @param filePath - path to file with jsons of tasks.
     * @return new {@code List<SuccessCreatedTask>}.
     */
    @PostMapping("transfer/task")
    public List<SuccessCreatedTask> addTasksFromFile(@RequestParam("filePath") String filePath) {
        return taskTransferService.createTasksFromFile(filePath);
    }

    /**
     * Use this endpoint to create task from infoRepository class.
     * The controller returns list of dto {@code List<SuccessCreatedTask>} of created tasks.
     *
     * @return new {@code List<SuccessCreatedTask>}.
     */
    @PostMapping("transfer/task/repository")
    public List<SuccessCreatedTask> addTasksFromRepository() {
        return taskTransferService.createTasksFromRepository();
    }
}
