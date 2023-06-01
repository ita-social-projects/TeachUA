package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionPreview;
import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Test;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface contains all methods needed to manage questions.
 */
public interface QuestionService {
    /**
     * This method a page of questions configured by pageable object.
     *
     * @param pageable pageable object that configures page number, page size and sorting
     * @return new {@code Page<QuestionResponse>}.
     */
    Page<QuestionResponse> findAllQuestionsPageable(Pageable pageable);

    /**
     * This method a page of questions searched by query and configured by pageable object.
     *
     * @param pageable pageable object that configures page number, page size and sorting
     * @param query    search query for question title can be blank for all questions
     * @param type     optional question type title
     * @param category optional question category title
     * @return new {@code Page<QuestionResponse>}
     */
    Page<QuestionResponse> searchAllQuestionsPageable(Pageable pageable, String query, String type, String category);

    /**
     * Finds a question by id, throws {@code ResponseStatusException} if not found.
     *
     * @param id question id
     * @return new {@code QuestionResponse}
     */
    QuestionResponse findQuestionById(Long id);

    /**
     * Returns question by id, throws {@code ResponseStatusException} if not found.
     *
     * @param id question id
     * @return the question
     */
    Question getQuestionById(Long id);

    /**
     * Find all the questions by test id.
     *
     * @param testId test id
     * @return list founded questions
     */
    List<Question> findQuestionsByTestId(Long testId);

    /**
     * Find Questions by test id using join fetch.
     *
     * @param testId test id
     * @return a list of questions
     */
    List<Question> findQuestionsByTestIdEager(Long testId);

    /**
     * Find a list of questions by test entity.
     *
     * @param test test entity
     * @return a list of questions
     */
    List<Question> findQuestionsByTest(Test test);

    /**
     * Find a list of dto {@code QuestionResponse} by test id.
     *
     * @param test test id
     * @return list of dto {@code QuestionResponse}
     */
    List<QuestionResponse> findQuestionResponsesByTestId(Long test);

    /**
     * Find a list of dto {@code QuestionResponse} by test entity.
     *
     * @param test test entity
     * @return a list of dto {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> findQuestionResponsesByTest(Test test);

    /**
     * Create a new question entity.
     *
     * @param question question entity
     * @return question entity if it was successfully added
     */
    Question save(Question question);

    /**
     * Create a new question entity by given QuestionResponse.
     *
     * @param questionResponse question dto
     * @return a question entity if it was successfully added
     */
    Question save(QuestionResponse questionResponse);

    /**
     * Updates question and returns question entity if it was successfully added.
     *
     * @param questionResponse question dto
     * @return new {@code Question}
     */
    Question update(QuestionResponse questionResponse);

    /**
     * Delete a question by id, throws {@code ResponseStatusException} if not found.
     *
     * @param id question id
     */
    void delete(long id);

    /**
     * Import questions from Google Form.
     *
     * @param formId    - put form's ID.
     * @param creatorId - put creator's ID.
     * @throws IOException - throws IOException .
     */
    void questionsImport(String formId, Long creatorId) throws IOException;

    List<QuestionPreview> getAllQuestions();
}
