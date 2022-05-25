package com.softserve.teachua.repository;

import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByChallenge(Challenge challenge);

    @Query("SELECT task FROM Task AS task"
            + " WHERE task.challenge = :challenge AND task.startDate <= CURRENT_DATE")
    List<Task> findCurrentTasksByChallenge(@Param("challenge") Challenge challenge);

    List<Task> findTaskByChallengeOrderByStartDate(Challenge challenge);

    List<Task> findTasksByChallengeAndStartDateBeforeOrderByStartDate(Challenge challenge, LocalDate date);
}
