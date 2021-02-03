package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByName(String name);
    Optional<Center> findById(Long id);
    boolean existsByName(String name);
}
