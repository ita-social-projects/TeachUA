package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionTest} model.
 */

@Repository
public interface QuestionTestRepository extends JpaRepository<QuestionTest, Long> {
}
