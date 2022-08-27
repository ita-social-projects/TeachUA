package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge.UpdateChallengeDate;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.service.impl.ChallengeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private ChallengeServiceImpl challengeService;

    private Challenge challenge;
    private Task task;
    private UpdateChallengeDate updateChallengeDate;
    private UpdateTask updateTask;
    private SuccessUpdatedTask successUpdatedTask;

    private final Long EXISTING_TASK_ID = 1L;
    private final String EXISTING_TASK_NAME = "Existing Task";
    private final Long EXISTING_CHALLENGE_ID = 1L;
    private final Long NOT_EXISTING_CHALLENGE_ID = 5L;
    private final String EXISTING_CHALLENGE_NAME = "Existing Challenge";
    private final LocalDate START_DATE = LocalDate.of(2022, 1, 20);
    private final LocalDate CORRECT_UPDATE_DATE = LocalDate.now().plusDays(10);

    @BeforeEach
    public void setUp() {
        challengeService.setTaskService(taskService);
        task = Task.builder()
                .id(EXISTING_TASK_ID)
                .name(EXISTING_TASK_NAME)
                .startDate(START_DATE)
                .build();
        challenge = Challenge.builder()
                .id(EXISTING_CHALLENGE_ID)
                .name(EXISTING_CHALLENGE_NAME)
                .tasks(Collections.singleton(task))
                .build();
        updateChallengeDate = UpdateChallengeDate.builder()
                .startDate(CORRECT_UPDATE_DATE)
                .build();
        updateTask = UpdateTask.builder()
                .name(EXISTING_TASK_NAME)
                .startDate(START_DATE)
                .build();
        successUpdatedTask = SuccessUpdatedTask.builder()
                .id(EXISTING_TASK_ID)
                .name(EXISTING_TASK_NAME)
                .startDate(CORRECT_UPDATE_DATE)
                .build();
    }

    @Test
    void cloneChallengeWithNotExistingIdShouldThrowNotExistException() {
        when(challengeRepository.findById(NOT_EXISTING_CHALLENGE_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> challengeService.cloneChallenge(NOT_EXISTING_CHALLENGE_ID, updateChallengeDate))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void cloneChallengeShouldReturnListOfSuccessUpdatedTask() {
        when(challengeRepository.findById(EXISTING_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(dtoConverter.convertFromDtoToDto(task, new UpdateTask())).thenReturn(updateTask);
        when(taskService.updateTask(EXISTING_TASK_ID, updateTask)).thenReturn(successUpdatedTask);
        assertThat(challengeService.cloneChallenge(EXISTING_CHALLENGE_ID, updateChallengeDate))
                .isEqualTo(Collections.singletonList(successUpdatedTask));
    }



//    @Test
//    void cloneChallengeWithNullDateShouldThrowIllegalArgumentException() {
//        assertThatThrownBy(() -> challengeService.cloneChallenge(EXISTING_CHALLENGE_ID, null))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

}
