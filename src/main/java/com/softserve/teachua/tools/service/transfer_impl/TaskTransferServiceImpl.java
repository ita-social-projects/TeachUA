package com.softserve.teachua.tools.service.transfer_impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.tools.FileUtils;
import com.softserve.teachua.tools.repository.TaskInfoRepository;
import com.softserve.teachua.tools.service.TaskTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskTransferServiceImpl implements TaskTransferService {

    private final FileUtils fileUtils;
    private final DtoConverter dtoConverter;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskTransferServiceImpl(
            FileUtils fileReader,
            DtoConverter dtoConverter,
            TaskRepository taskRepository) {
        this.fileUtils = fileReader;
        this.dtoConverter = dtoConverter;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<SuccessCreatedTask> createTasksFromFile(String filePath) {
        return createTasks(fileUtils.readFromFile(filePath, TaskProfile.class));
    }

    @Override
    public List<SuccessCreatedTask> createTasksFromRepository() {
        return createTasks(TaskInfoRepository.getTasksList());
    }

    private List<SuccessCreatedTask> createTasks(List<TaskProfile> tasks) {
        return tasks
                .stream()
                .map(taskProfile -> {
                    taskProfile.setPicture(fileUtils.moveImage(taskProfile.getPicture(), "tasks"));
                    log.debug("add task: " + taskProfile);
                    return dtoConverter.convertToEntity(taskProfile, Task.builder().build()).withId(null);
                }).map(taskRepository::save)
                .map(task -> (SuccessCreatedTask) dtoConverter.convertToDto(task, SuccessCreatedTask.class))
                .collect(Collectors.toList());
    }
}
