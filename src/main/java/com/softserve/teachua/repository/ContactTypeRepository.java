package com.softserve.teachua.repository;

import com.softserve.teachua.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
    boolean existsByName(String name);

    Optional<ContactType> findByName(String name);
}
