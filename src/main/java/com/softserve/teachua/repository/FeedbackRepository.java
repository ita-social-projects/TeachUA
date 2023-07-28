package com.softserve.teachua.repository;

import com.softserve.teachua.model.Feedback;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface to manage {@link Feedback} model.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> getAllByClubId(Long clubId);

    long countFeedbackByClubIdAndParentCommentIsNull(Long clubId);

    List<Feedback> getAllByClubIdAndParentCommentIsNull(Long clubId);

    Page<Feedback> getAllByClubIdAndParentCommentIsNullOrderByDateDesc(Long clubId, Pageable pageable);


    /**
     * Method to get average rating by club id.
     *
     * @param clubId club id
     * @return Double if club have any feedback
     */
    @Query("SELECT case  when (AVG(feedback.rate)) is null then 0.0 else AVG(feedback.rate)  end "
            + "FROM Feedback AS feedback WHERE feedback.club.id = :clubId")
    Double findAvgRating(@Param("clubId") Long clubId);

    @Query("SELECT AVG(f.rate) FROM Feedback f WHERE f.club.id = :clubId")
    Optional<Float> findAverageRateByClubId(@Param("clubId") Long clubId);

    long countByClubIdAndParentCommentIsNull(Long clubId);
}
