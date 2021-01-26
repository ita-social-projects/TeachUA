package com.softserve.teachua.repository;

import com.softserve.teachua.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club getById(Long id);

    Club findByName(String name);
}
