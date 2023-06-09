package com.softserve.club.repository;

import com.softserve.club.model.Station;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findById(Long id);

    Optional<Station> findByName(String name);

    List<Station> findAllByCityName(String name);

    List<Station> findAllByDistrictNameAndCityName(String district, String city);

    boolean existsByName(String name);
}
