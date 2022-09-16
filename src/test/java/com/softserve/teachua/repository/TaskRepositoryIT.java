package com.softserve.teachua.repository;

import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryIT {
    private static final String CHALLENGE_ONE = "Challenge1";
    private static final String CHALLENGE_TWO = "Challenge2";
    private static final String TASK = "Task";
    private final Challenge challenge1 = Challenge.builder().name(CHALLENGE_ONE).title(CHALLENGE_ONE)
            .description(CHALLENGE_ONE).picture(CHALLENGE_ONE).sortNumber(1L).isActive(true).build();
    private final Challenge challenge2 = Challenge.builder().name(CHALLENGE_TWO).title(CHALLENGE_TWO)
            .description(CHALLENGE_TWO).picture(CHALLENGE_TWO).sortNumber(2L).isActive(true).build();
    private final Task task1 = Task.builder().name(TASK).startDate(LocalDate.now()).headerText(TASK).picture(TASK)
            .challenge(challenge1).build();
    private final Task task2 = Task.builder().name(TASK).startDate(LocalDate.now().plusDays(1)).headerText(TASK)
            .picture(TASK).challenge(challenge1).build();
    private final List<Task> tasksWithChallenge1Sorted = Arrays.asList(task1, task2);
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        entityManager.persist(challenge1);
        entityManager.persist(challenge2);
        entityManager.persist(task1);
        entityManager.persist(task2);
    }

    @Test
    public void findTasksByChallengeShouldReturnListOfTasks() {
        assertThat(taskRepository.findTasksByChallenge(challenge1)).isEqualTo(tasksWithChallenge1Sorted);
    }

    @Test
    public void findTasksByChallengeShouldReturnEmptyListWhenNothingFound() {
        assertThat(taskRepository.findTasksByChallenge(challenge2)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void findTasksByChallengeAndStartDateBeforeOrderByStartDateShouldReturnSortedListOfTasks() {
        assertThat(taskRepository.findTasksByChallengeAndStartDateBeforeOrderByStartDate(challenge1,
                LocalDate.now().plusDays(2))).isEqualTo(tasksWithChallenge1Sorted);
    }

    @Test
    public void findTasksByChallengeAndStartDateBeforeOrderByStartDateShouldReturnEmptyListWithWrongDate() {
        assertThat(taskRepository.findTasksByChallengeAndStartDateBeforeOrderByStartDate(challenge1,
                LocalDate.now().plusDays(0))).isEqualTo(Collections.emptyList());
    }

    @Test
    public void findTasksByChallengeAndStartDateBeforeOrderByStartDateShouldReturnEmptyListWithChallengeWithoutTasks() {
        assertThat(taskRepository.findTasksByChallengeAndStartDateBeforeOrderByStartDate(challenge2,
                LocalDate.now().plusDays(2))).isEqualTo(Collections.emptyList());
    }
}
