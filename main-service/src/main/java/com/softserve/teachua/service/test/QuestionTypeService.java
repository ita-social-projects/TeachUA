package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question_type.QuestionTypeProfile;
import com.softserve.teachua.dto.test.question_type.QuestionTypeResponse;
import com.softserve.teachua.model.test.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * This interface contains all methods needed to manage types of questions.
 */
public interface QuestionTypeService {
    /**
     * Returns a list of all question types.
     *
     * @return {@code List<QuestionType>}
     */
    List<QuestionType> findAll();

    @Transactional(readOnly = true)
    QuestionType findById(Long id);

    /**
     * Find a QuestionType entity by title.
     *
     * @param title QuestionType title
     * @return a QuestionType entity found by title
     */
    QuestionType findByTitle(String title);

    /**
     * This method a page of question types searched by query and configured by pageable object.
     * @param pageable pageable object that configures page number, page size and sorting
     * @param title search query for question title, can be blank for all questions
     * @return new {@code Page<QuestionTypeResponse>}
     */
    Page<QuestionTypeResponse> searchAllQuestionCategoriesPageable(Pageable pageable, String title);

    /**
     * This method creates a new QuestionType entity.
     *
     * @param typeProfile - contains information about the new question type.
     * @return new {@code QuestionTypeProfile}
     */
    QuestionTypeProfile save(QuestionTypeProfile typeProfile);

    /**
     * This method updates QuestionType entity by id.
     * @param typeProfile update question type
     * @param id updated question id
     * @return new {@code QuestionTypeProfile}
     */
    QuestionTypeProfile updateById(QuestionTypeProfile typeProfile, Long id);

    /**
     * Delete a QuestionType entry by a given id.
     * @param id question type id
     */
    void deleteById(Long id);
}
