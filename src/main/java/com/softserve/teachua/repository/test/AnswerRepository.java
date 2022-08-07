package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides an interface for managing {@link Answer} model.
 */

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long id);
}
