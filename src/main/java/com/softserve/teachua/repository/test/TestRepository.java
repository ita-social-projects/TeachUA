package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides an interface for managing {@link Test} model.
 */

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByTitle(String title);

    @Query("SELECT t FROM Test t where t.active = true")
    List<Test> findActiveTests();

    @Query("SELECT t FROM Test t where t.archived = true")
    List<Test> findArchivedTests();

    @Query("SELECT t FROM Test t where t.archived = false")
    List<Test> findUnarchivedTests();
}
