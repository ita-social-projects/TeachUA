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
     * This method a page of questions configured by pageable object
     *
     * @param pageable pageable object that configures page number, page size and sorting
     * @return new {@code Page<QuestionResponse>}
     */
    Page<QuestionResponse> findAllQuestionsPageable(Pageable pageable);

    /**
     * This method a page of questions searched by query and configured by pageable object
     *
     * @param pageable pageable object that configures page number, page size and sorting
     * @param query    search query for question title, can be blank for all questions
     * @param type     optional question type title
     * @param category optional question category title
     * @return new {@code Page<QuestionResponse>}
     */
    Page<QuestionResponse> searchAllQuestionsPageable(Pageable pageable, String query, String type, String category);

    /**
     * Finds a question by id, throws {@code ResponseStatusException} if not found
     *
     * @param id question id
     * @return new {@code QuestionResponse}
     */
    QuestionResponse findQuestionById(Long id);

    /**
     * Returns question by id, throws {@code ResponseStatusException} if not found
     *
     * @param id question id
     * @return
     */
    Question getQuestionById(Long id);

    /**
     * This method returns list of questions by test id.
     *
     * @param testId - put test id.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTestId(Long testId);

    /**
     * This method returns list of questions by test id using join fetch.
     *
     * @param testId - put test id.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTestIdEager(Long testId);

    /**
     * This method returns list of questions by test entity.
     *
     * @param test - put test entity.
     * @return new {@code List<Question>}.
     */
    List<Question> findQuestionsByTest(Test test);

    /**
     * This method returns list of dto {@code QuestionResponse} by test id.
     *
     * @param test - put test id.
     * @return new {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> findQuestionResponsesByTestId(Long test);

    /**
     * This method returns list of dto {@code QuestionResponse} by test entity.
     *
     * @param test - put test entity.
     * @return new {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> findQuestionResponsesByTest(Test test);

    /**
     * This method returns question entity if it was successfully added.
     *
     * @param question - put question entity.
     * @return new {@code Question}.
     */
    Question save(Question question);

    /**
     * This method returns question entity if it was successfully added.
     *
     * @param questionResponse question dto
     * @return new {@code Question}
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
     * Delete a question by id, throws {@code ResponseStatusException} if not found
     *
     * @param id question id
     */
    void delete(long id);

    /**
     * This method returns TRUE if it was successfully import from Google Form.
     *
     * @param formId    - put form's ID.
     * @param creatorId - put creator's ID.
     * @throws IOException - throws IOException .
     */
    void questionsImport(String formId, Long creatorId) throws IOException;

    List<QuestionPreview> getAllQuestions();
}
