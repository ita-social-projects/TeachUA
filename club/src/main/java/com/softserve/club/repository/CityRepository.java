package com.softserve.club.repository;

import com.softserve.club.model.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(Long id);

    List<City> findAll();

    List<City> findAllByOrderByIdAsc();

    Optional<City> findByName(String name);

    boolean existsByName(String name);
}
