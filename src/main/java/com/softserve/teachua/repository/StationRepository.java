package com.softserve.teachua.repository;

import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findById(Long id);

    Optional<Station> findByName(String name);

    List<Station> findAllByCityName(String name);

//    List<Station> findAllByDistrictNameAndCityName(String district,String city);
    boolean existsByName(String name);
}
