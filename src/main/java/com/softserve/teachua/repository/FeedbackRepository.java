package com.softserve.teachua.repository;

import com.softserve.teachua.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback getById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    @Query("SELECT AVG(feedback.rate) FROM Feedback AS feedback WHERE feedback.club.id = :clubId")
    Double findAvgRating(@Param("clubId") Long clubId);
}
