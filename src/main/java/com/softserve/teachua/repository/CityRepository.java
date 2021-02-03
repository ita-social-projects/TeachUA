package com.softserve.teachua.repository;

import com.softserve.teachua.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(Long id);

    Optional<City> findByName(String name);

    boolean existsByName(String name);
}
