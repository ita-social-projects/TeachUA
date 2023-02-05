package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.model.test.QuestionCategory;
import java.util.List;

/**
 * This interface contains all methods needed to manage categories of questions.
 */
public interface QuestionCategoryService {
    /**
     * Returns a list of all question categories.
     *
     * @return {@code List<QuestionCategory>}
     */
    List<QuestionCategory> findAll();

    /**
     * Find QuestionCategory entity by given id.
     *
     * @param id a question category id
     * @return QuestionCategory entity found by id
     */
    QuestionCategory findById(Long id);

    /**
     * Find a QuestionCategory entity by title.
     *
     * @param title topic title
     * @return QuestionCategory entity found by title
     */
    QuestionCategory findByTitle(String title);

    /**
     * Find all QuestionCategory entities.
     *
     * @return a list of all QuestionCategory entities
     */
    List<QuestionCategoryProfile> findAllCategoryProfiles();

    /**
     * This method creates a new QuestionCategory entity.
     *
     * @param categoryProfile - contains information about the new question category.
     */
    QuestionCategoryProfile save(QuestionCategoryProfile categoryProfile);

    /**
     * Update a QuestionCategory entity by given id.
     *
     * @param categoryProfile - contains information about the new question category
     * @param id question category id
     * @return dto {@code QuestionCategoryProfile} if question category was successfully updated.
     */
    QuestionCategoryProfile updateById(QuestionCategoryProfile categoryProfile, Long id);
}
