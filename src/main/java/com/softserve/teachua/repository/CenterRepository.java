package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    Center findByName(String name);

    Center getById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}
