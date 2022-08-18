package com.softserve.teachua.repository;

import com.softserve.teachua.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides an interface to manage {@link Feedback} model.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findById(Long id);

    List<Feedback> getAllByClubId(Long clubId);

    void deleteById(Long id);

    /**
     * Method to get average rating by club id.
     *
     * @param clubId
     * @return Double if club have any feedback
     */
    @Query("SELECT case  when (AVG(feedback.rate)) is null then 0.0 else AVG(feedback.rate)  end "
            + "FROM Feedback AS feedback WHERE feedback.club.id = :clubId")
    Double findAvgRating(@Param("clubId") Long clubId);
}
