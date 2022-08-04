package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.GroupTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Group} model.
 */

@Repository
public interface GroupTestRepository extends JpaRepository<GroupTest, Long> {
}
