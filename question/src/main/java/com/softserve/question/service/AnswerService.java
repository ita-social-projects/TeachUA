package com.softserve.question.service;

import com.softserve.question.model.Answer;
import java.util.List;

/**
 * This interface contains all methods needed to manage answers.
 */
public interface AnswerService {
    /**
     * Get a list of answer entities by question id.
     * @param id a question id.
     * @return new {@code List<Answer>}.
     */
    List<Answer> findByQuestionId(Long id);

    /**
     * Git a list of answer entities by a list of ids.
     * @param ids a list of ids.
     * @return new {@code List<Answer>}.
     */
    List<Answer> findAllById(List<Long> ids);
}
