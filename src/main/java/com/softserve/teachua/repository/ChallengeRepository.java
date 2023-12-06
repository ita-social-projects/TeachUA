package com.softserve.teachua.repository;

import com.softserve.teachua.model.Challenge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> getByIsActiveOrderBySortNumberDesc(Boolean isActive);

    List<Challenge> findByName(String name);
}
