package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.model.test.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByTitle(String title);

    List<Test> findTestsByTopic(Topic topic);

    List<Test> findTestsByCreator(User creator);

    @Query("SELECT t FROM Test t where t.active = true")
    List<Test> findActiveTests();

    @Query("SELECT t FROM Test t where t.archived = true")
    List<Test> findArchivedTests();

    @Query("SELECT t FROM Test t where t.archived = false")
    List<Test> findUnarchivedTests();
}
