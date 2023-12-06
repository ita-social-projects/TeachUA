package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.tools.service.TaskTransferService;
import com.softserve.teachua.utils.annotation.DevPermit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskTransferController implements Api {
    private static final String ALTER_WELL = "Alter, well done";
    private final TaskTransferService taskTransferService;

    @Autowired
    public TaskTransferController(TaskTransferService taskTransferService) {
        this.taskTransferService = taskTransferService;
    }

    /**
     * Use this endpoint to create task from file. The controller returns list of dto {@code List<SuccessCreatedTask>}
     * of created tasks.
     *
     * @param filePath - path to file with jsons of tasks.
     * @return new {@code List<SuccessCreatedTask>}.
     */
    @DevPermit
    @PostMapping("transfer/task")
    public List<SuccessCreatedTask> addTasksFromFile(@RequestParam("filePath") String filePath) {
        return taskTransferService.createTasksFromFile(filePath);
    }

    /**
     * Use this endpoint to create task from infoRepository class. The controller returns list of dto
     * {@code List<SuccessCreatedTask>} of created tasks.
     *
     * @return new {@code List<SuccessCreatedTask>}.
     */
    @DevPermit
    @PostMapping("transfer/task/repository")
    public List<SuccessCreatedTask> addTasksFromRepository() {
        return taskTransferService.createTasksFromRepository();
    }

    @DevPermit
    @DeleteMapping("transfer/task/addconst")
    public ResponseEntity<String> alterDb() {
        taskTransferService.addConstrain();
        return ResponseEntity.ok(ALTER_WELL);
    }

    @DevPermit
    @DeleteMapping("transfer/task/dropconst")
    public ResponseEntity<String> dropConstrain() {
        taskTransferService.dropConstrain();
        return ResponseEntity.ok(ALTER_WELL);
    }

    @DevPermit
    @DeleteMapping("transfer/task/droptable")
    public ResponseEntity<String> dropTable() {
        taskTransferService.dropRedundantTable();
        return ResponseEntity.ok(ALTER_WELL);
    }
}
