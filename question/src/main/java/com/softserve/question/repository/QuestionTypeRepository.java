package com.softserve.question.repository;

import com.softserve.question.model.QuestionType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionType} model.
 */
@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
    Optional<QuestionType> findByTitle(String title);

    Page<QuestionType> findByTitleContainingIgnoreCase(Pageable pageable, String title);

    boolean existsByTitle(String title);
}
