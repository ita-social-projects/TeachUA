package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Topic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Topic} model.
 */

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTitle(String title);

    Boolean existsByTitle(String title);
}
