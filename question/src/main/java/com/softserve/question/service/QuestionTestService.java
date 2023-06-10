package com.softserve.question.service;

import com.softserve.question.model.QuestionTest;

/**
 * This interface contains all methods needed to manage relations between questions and tests.
 */
public interface QuestionTestService {
    /**
     * This method adds new relation between questions and tests.
     * @param questionTest - put result entity.
     * @return new {@code QuestionTest}
     */
    QuestionTest save(QuestionTest questionTest);
}
