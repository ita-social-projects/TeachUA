package com.softserve.teachua.repository;

import com.softserve.teachua.model.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MessengerRepository extends JpaRepository<Messenger, Integer> {
    Optional<Messenger> findByUserName(String name);
    Optional<Messenger> findByAccessKey(String accessKey);
    boolean existsByUserName(String name);
    boolean existsByAccessKey(String accessKey);
}
