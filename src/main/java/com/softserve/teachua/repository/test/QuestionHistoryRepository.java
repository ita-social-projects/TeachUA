package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionHistory} model.
 */

@Repository
public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
}
