package com.softserve.teachua.repository;

import com.softserve.teachua.model.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface to manage {@link Message} model.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findAllBySenderIdOrderByDateDesc(Long id);

    Optional<List<Message>> findAllByRecipientIdOrderByDateDesc(Long id);

    List<Message> findAllBySenderIdAndIsActive(Long senderId, boolean isActive, Sort sort);

    List<Message> findAllByRecipientIdAndIsActive(Long senderId, boolean isActive, Sort sort);

    Optional<Message> findByIdAndIsActive(Long id, boolean isActive);
}
