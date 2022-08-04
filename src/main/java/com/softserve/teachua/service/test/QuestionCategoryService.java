package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.QuestionCategory;

/**
 * This interface contains all methods needed to manage categories of questions.
 */
public interface QuestionCategoryService {
    /**
     * This method returns a QuestionCategory entity found by title.
     * @param title - put topic title.
     * @return new {@code QuestionCategory}.
     */
    QuestionCategory findByTitle(String title);
}
