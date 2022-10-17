package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Test;

import java.util.List;

/**
 * This interface contains all methods needed to manage questions.
 */
public interface QuestionService {
    /**
     * This method returns list of questions by test id.
     * @param testId - put test id.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTestId(Long testId);

    /**
     * This method returns list of questions by test id using join fetch.
     * @param testId - put test id.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTestIdEager(Long testId);

    /**
     * This method returns list of questions by test entity.
     * @param test - put test entity.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTest(Test test);

    /**
     * This method returns list of dto {@code QuestionResponse} by test id.
     * @param test - put test id.
     * @return new {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> findQuestionResponsesByTestId(Long test);

    /**
     * This method returns list of dto {@code QuestionResponse} by test entity.
     * @param test - put test entity.
     * @return new {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> findQuestionResponsesByTest(Test test);

    /**
     * This method returns question entity if it was successfully added.
     * @param question - put question entity.
     * @return new {@code Question}.
     */
    Question save(Question question);
}
