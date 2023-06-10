package com.softserve.question.repository;

import com.softserve.question.model.QuestionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionTest} model.
 */

@Repository
public interface QuestionTestRepository extends JpaRepository<QuestionTest, Long> {
}
