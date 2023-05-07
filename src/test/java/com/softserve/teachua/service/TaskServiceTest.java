package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.CreateTask;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.impl.TaskServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskProfile taskProfile;
    private TaskPreview taskPreview;
    private CreateTask createTask;
    private UpdateTask updateTask;
    private SuccessCreatedTask successCreatedTask;
    private SuccessUpdatedTask successUpdatedTask;
    private Challenge challenge;

    // task
    private final Long CORRECT_TASK_ID = 1L;
    private final Long WRONG_TASK_ID = 2L;
    private final String CORRECT_TASK_NAME = "MyTask";
    private final String WRONG_TASK_NAME = "BadTask";

    // challenge
    private final Long CORRECT_CHALLENGE_ID = 2L;
    private final String CORRECT_CHALLENGE_NAME = "MyChallenge";

    @BeforeEach
    public void setMocks() {
        challenge = Challenge.builder().id(CORRECT_CHALLENGE_ID).name(CORRECT_CHALLENGE_NAME).build();

        task = Task.builder().id(CORRECT_TASK_ID).name(CORRECT_TASK_NAME).challenge(challenge).build();

        taskPreview = TaskPreview.builder().id(CORRECT_TASK_ID).name(CORRECT_TASK_NAME).build();

        updateTask = UpdateTask.builder().name(CORRECT_TASK_NAME).challengeId(CORRECT_CHALLENGE_ID).build();

        successUpdatedTask = SuccessUpdatedTask.builder().id(CORRECT_TASK_ID).name(CORRECT_TASK_NAME)
                .challengeId(CORRECT_CHALLENGE_ID).build();

        createTask = CreateTask.builder().name(CORRECT_TASK_NAME).build();

        successCreatedTask = SuccessCreatedTask.builder().id(CORRECT_TASK_ID).name(CORRECT_TASK_NAME)
                .challengeId(CORRECT_CHALLENGE_ID).build();

        taskProfile = TaskProfile.builder().id(CORRECT_TASK_ID).name(CORRECT_TASK_NAME)
                .challengeId(CORRECT_CHALLENGE_ID).build();

    }

    @Test
    public void getTaskByCorrectIdShouldReturnTask() {
        when(taskRepository.findById(CORRECT_TASK_ID)).thenReturn(Optional.of(task));
        assertThat(taskService.getTaskById(CORRECT_TASK_ID)).isEqualTo(task);
    }

    @Test
    public void getTaskByWrongIdShouldThrowNotExistException() {
        when(taskRepository.findById(WRONG_TASK_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getTaskById(WRONG_TASK_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getTasksByCorrectChallengeIdShouldReturnListOfTaskPreview() {
        when(challengeService.getChallengeById(CORRECT_CHALLENGE_ID)).thenReturn(challenge);
        when(dtoConverter.convertToDto(task, TaskPreview.class)).thenReturn(taskPreview);
        when(taskRepository.findTasksByChallenge(challenge)).thenReturn(Collections.singletonList(task));
        assertThat(taskService.getTasksByChallengeId(CORRECT_CHALLENGE_ID)).isNotEmpty()
                .isEqualTo(Collections.singletonList(taskPreview));
    }

    @Test
    public void getListOfTasksShouldReturnListOfTaskPreview() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(dtoConverter.convertToDto(task, TaskPreview.class)).thenReturn(taskPreview);
        assertThat(taskService.getListOfTasks()).isNotEmpty().isEqualTo(Collections.singletonList(taskPreview));
    }

    @Test
    public void getListOfTasksShouldReturnEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());
        assertThat(taskService.getListOfTasks()).isEmpty();
    }

    @Test
    public void createTaskShouldReturnSuccessCreatedTask() {
        when(challengeService.getChallengeById(CORRECT_CHALLENGE_ID)).thenReturn(challenge);
        when(dtoConverter.convertToEntity(createTask, new Task())).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(dtoConverter.convertToDto(task, SuccessCreatedTask.class)).thenReturn(successCreatedTask);
        assertThat(taskService.createTask(CORRECT_CHALLENGE_ID, createTask)).isEqualTo(successCreatedTask);
    }

    @Test
    public void updateTaskShouldReturnSuccessUpdatedTask() {
        when(taskRepository.findById(CORRECT_TASK_ID)).thenReturn(Optional.of(task));
        when(challengeService.getChallengeById(CORRECT_CHALLENGE_ID)).thenReturn(challenge);
        when(taskRepository.save(task)).thenReturn(task);
        when(dtoConverter.convertToDto(task, SuccessUpdatedTask.class)).thenReturn(successUpdatedTask);
        assertThat(taskService.updateTask(CORRECT_TASK_ID, updateTask)).isEqualTo(successUpdatedTask);
    }

    @Test
    public void deleteTaskShouldReturnTaskProfile() {
        when(taskRepository.findById(CORRECT_TASK_ID)).thenReturn(Optional.of(task));
        when(dtoConverter.convertToDto(task, TaskProfile.class)).thenReturn(taskProfile);
        // when(archiveService.saveModel(task)).thenReturn(task);
        doNothing().when(taskRepository).deleteById(anyLong());
        doNothing().when(taskRepository).flush();
        assertThat(taskService.deleteTask(CORRECT_TASK_ID)).isEqualTo(taskProfile);
    }

}
