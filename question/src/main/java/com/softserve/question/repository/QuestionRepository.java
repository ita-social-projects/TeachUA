package com.softserve.question.repository;

import com.softserve.question.model.Question;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.model.QuestionType;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Question} model.
 */

@Repository("testQuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @NotNull Optional<Question> findById(@NotNull Long id);

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

    @Query("SELECT distinct qt.question, qt FROM QuestionTest qt JOIN FETCH qt.question.answers WHERE qt.test.id = :id")
    List<Question> findAllQuestionsByTestIdFetch(@Param("id") Long testId);

    @Query("SELECT q from testQuestion q")
    List<Question> findAllQuestions();
}
