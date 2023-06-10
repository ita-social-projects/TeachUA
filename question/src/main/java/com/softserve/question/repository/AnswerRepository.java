package com.softserve.question.repository;

import com.softserve.question.model.Answer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Answer} model.
 */

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long id);
}
