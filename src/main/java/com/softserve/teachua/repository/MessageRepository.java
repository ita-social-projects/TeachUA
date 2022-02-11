package com.softserve.teachua.repository;

import com.softserve.teachua.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface to manage {@link Message} model.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
