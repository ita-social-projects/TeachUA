package com.softserve.question.repository;

import com.softserve.question.model.QuestionCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionCategory} model.
 */
@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    Optional<QuestionCategory> findByTitle(String title);

    Page<QuestionCategory> findByTitleContainingIgnoreCase(Pageable pageable, String title);

    Boolean existsByTitle(String title);
}
