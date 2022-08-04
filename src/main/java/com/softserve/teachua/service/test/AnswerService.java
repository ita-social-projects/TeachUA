package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Answer;

import java.util.List;
import java.util.Optional;

/**
 * This interface contains all methods needed to manage answers.
 */
public interface AnswerService {
    /**
     * This method returns list of answer entities by question id.
     * @param id - put question id.
     * @return new {@code List<Answer>}.
     */
    List<Answer> findByQuestionId(Long id);

    /**
     * This method returns list of answer entities by a list of ids.
     * @param ids - put list of ids.
     * @return new {@code List<Answer>}.
     */
    List<Answer> findAllById(List<Long> ids);
}
