package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides an interface for managing {@link Question} model.
 */

@Repository("testQuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByTitle(String title);
    @Query("SELECT qt.question FROM QuestionTest qt WHERE qt.test.id = :id")
    List<Question> findQuestionsByTestId(@Param("id") Long testId);
}
