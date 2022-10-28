package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides an interface for managing {@link QuestionType} model.
 */

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
    Optional<QuestionType> findByTitle(String title);
}
