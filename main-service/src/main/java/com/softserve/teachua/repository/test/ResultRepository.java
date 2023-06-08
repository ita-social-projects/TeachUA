package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Result;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Result} model.
 */

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    //todo
    //List<Result> findResultsByUserAndTest(User user, Test test);

    List<Result> findResultsByUserId(Long userId);
}
