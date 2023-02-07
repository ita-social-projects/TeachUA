package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.QuestionType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Question} model.
 */

@Repository("testQuestionRepository")
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    Optional<Question> findById(Long id);

    Page<Question> findByTitleContainingIgnoreCase(Pageable pageable, String query);

    Page<Question> findByTitleContainingIgnoreCaseAndQuestionType(
            Pageable pageable,
            String query,
            QuestionType type
    );

    Page<Question> findByTitleContainingIgnoreCaseAndQuestionCategory(
            Pageable pageable,
            String query,
            QuestionCategory category
    );

    Page<Question> findByTitleContainingIgnoreCaseAndQuestionTypeAndQuestionCategory(
            Pageable pageable,
            String query,
            QuestionType type,
            QuestionCategory category
    );

    Optional<Question> findByTitle(String title);

    boolean existsByQuestionCategoryId(Long categoryId);

    boolean existsByQuestionTypeId(Long typeId);

    @Query("SELECT qt.question FROM QuestionTest qt WHERE qt.test.id = :id")
    List<Question> findQuestionsByTestId(@Param("id") Long testId);

    @Query("SELECT distinct qt.question FROM QuestionTest qt JOIN FETCH qt.question.answers WHERE qt.test.id = :id")
    List<Question> findAllQuestionsByTestIdFetch(@Param("id") Long testId);

    @Query("SELECT q from testQuestion q")
    List<Question> findAllQuestions();
}
