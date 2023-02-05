package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionCategory} model.
 */

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    Optional<QuestionCategory> findByTitle(String title);

    Boolean existsByTitle(String title);
}
