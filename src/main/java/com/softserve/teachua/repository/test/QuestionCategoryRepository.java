package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides an interface for managing {@link QuestionCategory} model.
 */

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    Optional<QuestionCategory> findByTitle(String title);
}