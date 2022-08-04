package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.QuestionTest;

/**
 * This interface contains all methods needed to manage relations between questions and tests.
 */
public interface QuestionTestService {
    /**
     * This method adds new relation between questions and tests.
     * @param questionTest - put result entity.
     */
    void save(QuestionTest questionTest);
}
