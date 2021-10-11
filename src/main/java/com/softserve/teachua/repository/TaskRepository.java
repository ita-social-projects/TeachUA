package com.softserve.teachua.repository;

import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findTasksByChallenge(Challenge challenge, Pageable pageable);

}
