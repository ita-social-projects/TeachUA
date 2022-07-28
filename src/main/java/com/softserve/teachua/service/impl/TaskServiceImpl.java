package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.model.archivable.TaskArch;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.*;
import com.softserve.teachua.utils.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService, ArchiveMark<Task> {
    private final TaskRepository taskRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;
    private final ChallengeService challengeService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TaskServiceImpl(
            TaskRepository taskRepository,
            ArchiveService archiveService,
            DtoConverter dtoConverter,
            ChallengeService challengeService,
            UserService userService, ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
        this.challengeService = challengeService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public TaskProfile deleteTask(Long id) {
        Task task = getTaskById(id);
        TaskProfile taskProfile = dtoConverter.convertToDto(task, TaskProfile.class);
        taskProfile.setChallengeId(task.getChallenge().getId());
        taskRepository.deleteById(id);
        taskRepository.flush();
        archiveModel(task);
        return taskProfile;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format("Task not found by id: %s", id)));
    }

    @Override
    public List<TaskPreview> getTasksByChallengeId(Long id) {
        Challenge challenge = challengeService.getChallengeById(id);
        Function<Task, TaskPreview> function = (task) -> dtoConverter.convertToDto(task, TaskPreview.class);
        return taskRepository.findTasksByChallenge(challenge)
                .stream().map(function).collect(Collectors.toList());
    }

    @Override
    public List<TaskPreview> getCurrentTasksByChallengeId(Long id) {
        Challenge challenge = challengeService.getChallengeById(id);
        Function<Task, TaskPreview> function = (task) -> dtoConverter.convertToDto(task, TaskPreview.class);
        return taskRepository.findCurrentTasksByChallenge(challenge)
                .stream().map(function).collect(Collectors.toList());
    }

    @Override
    public TaskProfile getTask(Long taskId) {
        Task task = getTaskById(taskId);
        if (task.getStartDate().isAfter(LocalDate.now())) {
            userService.verifyIsUserAdmin();
        }
        TaskProfile taskProfile = dtoConverter.convertToDto(task, TaskProfile.class);
        taskProfile.setChallengeId(task.getChallenge().getId());
        return taskProfile;
    }

    @Override
    public List<TaskPreview> getListOfTasks() {
        List<TaskPreview> taskPreviewList = taskRepository.findAll()
                .stream()
                .map(task -> (TaskPreview) dtoConverter.convertToDto(task, TaskPreview.class))
                .collect(Collectors.toList());

        log.debug("getting list of tasks {}", taskPreviewList);
        return taskPreviewList;
    }

    @Override
    public SuccessCreatedTask createTask(Long id, CreateTask createTask) {
        HtmlUtils.validateDescription(createTask.getDescription());
        HtmlUtils.validateDescription(createTask.getHeaderText());
        Challenge challenge = challengeService.getChallengeById(id);
        Task task = dtoConverter.convertToEntity(createTask, new Task());
        task.setChallenge(challenge);
        return dtoConverter.convertToDto(taskRepository.save(task), SuccessCreatedTask.class);
    }

    @Override
    public SuccessUpdatedTask updateTask(Long id, UpdateTask updateTask) {
        HtmlUtils.validateDescription(updateTask.getDescription());
        HtmlUtils.validateDescription(updateTask.getHeaderText());
        Task task = getTaskById(id);
        BeanUtils.copyProperties(updateTask, task);
        task.setChallenge(challengeService.getChallengeById(updateTask.getChallengeId()));
        SuccessUpdatedTask updatedTask = dtoConverter.convertToDto(taskRepository.save(task), SuccessUpdatedTask.class);
        // updatedTask.setChallengeId(updatedTask.getChallengeId());
        return updatedTask;
    }

    @Override
    public void archiveModel(Task task) {
        archiveService.saveModel(dtoConverter.convertToDto(task, TaskArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        TaskArch taskArch = objectMapper.readValue(archiveObject, TaskArch.class);
        Task task = dtoConverter.convertToEntity(taskArch, Task.builder().build())
                .withId(null)
                .withChallenge(challengeService.getChallengeById(taskArch.getChallengeId()));
        taskRepository.save(task);
    }
}
