package com.softserve.club.repository;

import com.softserve.club.model.ContactType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
    boolean existsByName(String name);

    Optional<ContactType> findByName(String name);
}
