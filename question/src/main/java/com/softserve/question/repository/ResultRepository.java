package com.softserve.question.repository;

import com.softserve.question.model.Result;
import com.softserve.question.model.Test;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Result} model.
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findResultsByUserIdAndTest(Long userId, Test test);

    List<Result> findResultsByUserId(Long userId);
}
