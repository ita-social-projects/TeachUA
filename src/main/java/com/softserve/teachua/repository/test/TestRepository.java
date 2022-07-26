package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.model.test.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findActive();

    Optional<Test> findByTitle(String title);

    List<Test> findArchived();

    List<Test> findTestsByTopic(Topic topic);

    List<Test> findTestsByCreator(User creator);
}
