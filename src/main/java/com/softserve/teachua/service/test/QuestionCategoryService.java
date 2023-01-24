package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.model.test.QuestionCategory;
import java.util.List;

/**
 * This interface contains all methods needed to manage categories of questions.
 */
public interface QuestionCategoryService {
    /**
     * Returns a list of all question categories
     *
     * @return {@code List<QuestionCategory>}
     */
    List<QuestionCategory> findAll();

    /**
     * This method returns QuestionCategory entity found by id.
     *
     * @param id - put question category id.
     * @return new {@code QuestionCategory}
     */
    QuestionCategory findById(Long id);

    /**
     * This method returns the QuestionCategory entity found by title.
     *
     * @param title - put topic title.
     * @return new {@code QuestionCategory}.
     */
    QuestionCategory findByTitle(String title);

    /**
     * This method returns list of all QuestionCategory entities.
     *
     * @return new {@code List<QuestionCategoryProfile>}.
     */
    List<QuestionCategoryProfile> findAllCategoryProfiles();

    /**
     * This method creates the new QuestionCategory entity.
     *
     * @param categoryProfile - contains information about the new question category.
     */
    QuestionCategoryProfile save(QuestionCategoryProfile categoryProfile);

    /**
     * This method returns dto {@code QuestionCategoryProfile}
     * if question category was successfully updated.
     *
     * @param categoryProfile - contains information about the new question category.
     * @param id              - put question category id here.
     * @return new {@code QuestionCategoryProfile}
     */
    QuestionCategoryProfile updateById(QuestionCategoryProfile categoryProfile, Long id);
}
