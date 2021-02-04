package com.softserve.teachua.repository;

import com.softserve.teachua.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findById(Long id);

    Optional<Station> findByName(String name);

    boolean existsByName(String name);
}
