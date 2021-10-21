package com.softserve.teachua.repository;

import com.softserve.teachua.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> getByIsActiveOrderBySortNumberDesc(Boolean isActive);
}
