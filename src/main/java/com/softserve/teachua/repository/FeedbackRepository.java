package com.softserve.teachua.repository;

import com.softserve.teachua.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback getById(Long id);
    boolean existsById(Long id);
}
