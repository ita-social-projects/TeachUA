package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.FileUploadService;
import com.softserve.teachua.service.TaskService;
import com.softserve.teachua.utils.HtmlValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ArchiveService archiveService;
    private final FileUploadService fileUploadService;
    private final DtoConverter dtoConverter;
    private final ChallengeService challengeService;

    @Autowired
    public TaskServiceImpl(
            TaskRepository taskRepository,
            ArchiveService archiveService,
            FileUploadService fileUploadService,
            DtoConverter dtoConverter,
            ChallengeService challengeService) {
        this.taskRepository = taskRepository;
        this.archiveService = archiveService;
        this.fileUploadService = fileUploadService;
        this.dtoConverter = dtoConverter;
        this.challengeService = challengeService;
    }

    @Override
    public TaskProfile deleteTask(Long id) {
        Task task = getTaskById(id);
        TaskProfile taskProfile = dtoConverter.convertToDto(task, TaskProfile.class);
        taskProfile.setChallengeId(task.getChallenge().getId());
        task.setChallenge(null);
        archiveService.saveModel(task);
        taskRepository.deleteById(id);
        taskRepository.flush();
        return taskProfile;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format("Task not found by id: %s", id)));
    }

    @Override
    public Page<TaskPreview> getTasksByChallengeId(Long id, Pageable pageable) {
        Function<Task, TaskPreview> function =
                (task) -> dtoConverter.convertToDto(task, TaskPreview.class);
        Page<Task> tasks = taskRepository.findTasksByChallenge(challengeService.getChallengeById(id), pageable);
        return new PageImpl<>(tasks
                .stream()
                .map(function)
                .collect(Collectors.toList()),
                tasks.getPageable(), tasks.getTotalElements());
    }

    @Override
    public TaskProfile getTask(Long taskId) {
        Task task = getTaskById(taskId);
        TaskProfile taskProfile = dtoConverter.convertToDto(task, TaskProfile.class);
        taskProfile.setChallengeId(task.getChallenge().getId());
        return taskProfile;
    }

    @Override
    public SuccessCreatedTask createTask(Long id, CreateTask createTask) {
        createTask.setDescription(HtmlValidator.validateDescription(createTask.getDescription()));
        Challenge challenge = challengeService.getChallengeById(id);
        Task task = dtoConverter.convertToEntity(createTask, new Task());
        task.setChallenge(challenge);
        return dtoConverter.convertToDto(taskRepository.save(task), SuccessCreatedTask.class);
    }

    @Override
    public SuccessUpdatedTask updateTask(Long id, UpdateTask updateTask) {
        updateTask.setDescription(HtmlValidator.validateDescription(updateTask.getDescription()));
        Task task = getTaskById(id);
        BeanUtils.copyProperties(updateTask, task);
        task.setChallenge(challengeService.getChallengeById(updateTask.getChallengeId()));
        SuccessUpdatedTask updatedTask = dtoConverter.convertToDto(taskRepository.save(task), SuccessUpdatedTask.class);
        updatedTask.setChallengeId(updatedTask.getChallengeId());
        return updatedTask;
    }
}
