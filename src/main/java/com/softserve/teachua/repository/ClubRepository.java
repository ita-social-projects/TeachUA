package com.softserve.teachua.repository;

import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club getById(Long id);
    Club findByName(String name);
    boolean existsByName(String name);
    boolean existsById(Long id);

    Page<Club> findAllByNameContainsAndCityNameContains(String name,
                                   String cityName,
                                   Pageable pageable);

}
