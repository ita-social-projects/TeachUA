package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.TaskNameProfile;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.FileUploadService;
import com.softserve.teachua.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ArchiveService archiveService;
    private final FileUploadService fileUploadService;
    private final DtoConverter dtoConverter;

    @Autowired
    public TaskServiceImpl(
            TaskRepository taskRepository,
            ArchiveService archiveService,
            FileUploadService fileUploadService,
            DtoConverter dtoConverter) {
        this.taskRepository = taskRepository;
        this.archiveService = archiveService;
        this.fileUploadService = fileUploadService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public TaskNameProfile deleteTask(Long id) {
        Task task = getTaskById(id);
        archiveService.saveModel(task);
        fileUploadService.deleteFile(task.getPicture());
        taskRepository.deleteById(id);
        taskRepository.flush();
        // TODO: exc
        return dtoConverter.convertToDto(task, TaskNameProfile.class);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotExistException(String.format("Task not found by id: %s", id));
                });
    }
}
