package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.QuestionType;

import java.util.List;

/**
 * This interface contains all methods needed to manage types of questions.
 */
public interface QuestionTypeService {

    /**
     * Returns a list of all question types
     * @return {@code List<QuestionType>}
     */
    List<QuestionType> findAll();

    /**
     * This method returns a QuestionType entity found by title.
     * @param title - put QuestionType title.
     * @return new {@code QuestionType}.
     */
    QuestionType findByTitle(String title);
}
