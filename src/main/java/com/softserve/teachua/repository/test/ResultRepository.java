package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Result;
import com.softserve.teachua.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides an interface for managing {@link Result} model.
 */

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findResultsByUserAndTest(User user, Test test);
    List<Result> findResultsByUserId(Long userId);
}