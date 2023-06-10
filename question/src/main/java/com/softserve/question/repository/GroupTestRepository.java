package com.softserve.question.repository;

import com.softserve.question.model.GroupTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Group} model.
 */

@Repository
public interface GroupTestRepository extends JpaRepository<GroupTest, Long> {
}
