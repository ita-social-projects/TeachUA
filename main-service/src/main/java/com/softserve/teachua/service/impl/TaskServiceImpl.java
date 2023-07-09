package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.exception.UserPermissionException;
import com.softserve.commons.security.UserPrincipal;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.teachua.dto.task.CreateTask;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.TaskService;
import com.softserve.teachua.utils.HtmlUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final DtoConverter dtoConverter;
    private final ChallengeService challengeService;
    private final ObjectMapper objectMapper;
    private final ArchiveMQMessageProducer<Task> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, DtoConverter dtoConverter,
                           @Lazy ChallengeService challengeService, ObjectMapper objectMapper,
                           ArchiveMQMessageProducer<Task> archiveMQMessageProducer, ArchiveClient archiveClient) {
        this.taskRepository = taskRepository;
        this.dtoConverter = dtoConverter;
        this.challengeService = challengeService;
        this.objectMapper = objectMapper;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
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
        Function<Task, TaskPreview> function = task -> dtoConverter.convertToDto(task, TaskPreview.class);
        return taskRepository.findTasksByChallenge(challenge).stream().map(function).toList();
    }

    @Override
    public List<TaskPreview> getCurrentTasksByChallengeId(Long id) {
        Challenge challenge = challengeService.getChallengeById(id);
        Function<Task, TaskPreview> function = task -> dtoConverter.convertToDto(task, TaskPreview.class);
        return taskRepository.findCurrentTasksByChallenge(challenge).stream().map(function)
                .toList();
    }

    @Override
    public TaskProfile getTask(Long taskId) {
        Task task = getTaskById(taskId);

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (task.getStartDate().isAfter(LocalDate.now()) && principal.getRole() == RoleData.ADMIN) {
            throw new UserPermissionException();
        }

        TaskProfile taskProfile = dtoConverter.convertToDto(task, TaskProfile.class);
        taskProfile.setChallengeId(task.getChallenge().getId());
        return taskProfile;
    }

    @Override
    public List<TaskPreview> getListOfTasks() {
        List<TaskPreview> taskPreviewList = taskRepository.findAll().stream()
                .map(task -> (TaskPreview) dtoConverter.convertToDto(task, TaskPreview.class))
                .toList();

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
        return dtoConverter.convertToDto(taskRepository.save(task), SuccessUpdatedTask.class);
    }

    private void archiveModel(Task task) {
        archiveMQMessageProducer.publish(task);
    }

    @Override
    public void restoreModel(Long id) {
        var task = objectMapper.convertValue(
                archiveClient.restoreModel(Task.class.getName(), id),
                Task.class);

        taskRepository.save(task);
    }
}
