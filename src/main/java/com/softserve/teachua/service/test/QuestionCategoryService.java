package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question_category.QuestionCategoryProfile;
import com.softserve.teachua.dto.test.question_category.QuestionCategoryResponse;
import com.softserve.teachua.model.test.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * This method a page of question categories searched by query and configured by pageable object.
     * @param pageable pageable object that configures page number, page size and sorting
     * @param title search query for question title, can be blank for all questions
     * @return new {@code Page<QuestionCategoryResponse>}
     */
    Page<QuestionCategoryResponse> searchAllQuestionCategoriesPageable(Pageable pageable, String title);

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
     * @param categoryProfile - contains information about the new question category.
     * @param id question category id
     * @return dto {@code QuestionCategoryProfile} if question category was successfully updated.
     */
    QuestionCategoryProfile updateById(QuestionCategoryProfile categoryProfile, Long id);

    /**
     * Delete a QuestionCategory entry by a given id.
     * @param id question category id
     */
    void deleteById(Long id);
}
