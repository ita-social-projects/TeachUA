package com.softserve.teachua.service;

import com.softserve.teachua.dto.question.QuestionProfile;
import com.softserve.teachua.dto.question.QuestionResponse;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.teachua.model.Question;
import java.util.List;

/**
 * This interface contains all needed methods to manage questions.
 */

public interface QuestionService {
    /**
     * Method find {@link Question}.
     *
     * @param id
     *            - place question id
     *
     * @return new {@code Question}
     */
    Question getQuestionById(Long id);

    /**
     * The method returns dto {@code QuestionResponse} if question successfully added.
     *
     * @param questionProfile
     *            - place dto with all params.
     *
     * @return new {@code QuestionResponse}.
     *
     * @throws AlreadyExistException
     *             if question already exists.
     */
    QuestionResponse addQuestion(QuestionProfile questionProfile);

    /**
     * The method returns dto {@code QuestionProfile} of updated question.
     *
     * @param id
     *            - put question id
     * @param questionProfile
     *            - place body of dto {@code QuestionProfile}.
     *
     * @return new {@code QuestionProfile}.
     */
    QuestionProfile updateQuestionById(Long id, QuestionProfile questionProfile);

    /**
     * The method returns dto {@code QuestionProfile} of deleted question by id.
     *
     * @param id
     *            - put question id
     *
     * @return new {@code QuestionProfile}.
     *
     * @throws DatabaseRepositoryException
     *             if question contain foreign keys.
     */
    QuestionProfile deleteQuestionById(Long id);

    /**
     * The method returns list of dto {@code List<QuestionResponse>} of all questions.
     *
     * @return new {@code List<QuestionResponse>}.
     */
    List<QuestionResponse> getAllQuestions();
}
