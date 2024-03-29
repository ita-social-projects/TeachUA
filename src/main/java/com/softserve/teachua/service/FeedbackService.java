package com.softserve.teachua.service;

import com.softserve.teachua.dto.SortAndPageDto;
import com.softserve.teachua.dto.feedback.ReplyRequest;
import com.softserve.teachua.dto.feedback.ReplyResponse;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.User;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * This interface contains all needed methods to manage feedbacks.
 */

public interface FeedbackService {
    /**
     * Method find {@link Feedback}, and convert it to object of DTO class.
     *
     * @param id - place id here.
     * @return new {@code FeedbackResponse}
     **/
    FeedbackResponse getFeedbackProfileById(Long id);

    /**
     * Method find {@link Feedback}.
     *
     * @param id - place id
     * @return Feedback
     **/
    Feedback getFeedbackById(Long id);

    /**
     * Method add and save new {@link Feedback}.
     *
     * @param feedbackProfile - put dto 'FeedbackProfile'
     * @return SuccessCreatedFeedback
     **/
    SuccessCreatedFeedback createFeedback(FeedbackProfile feedbackProfile);

    /**
     * The method returns list of {@code List<FeedbackResponse>}.
     *
     * @return new {@code List<FeedbackResponse>}
     **/
    List<FeedbackResponse> getListOfFeedback();

    /**
     * The method returns list of {@code List<FeedbackResponse>} by club id.
     *
     * @return new {@code List<FeedbackResponse>}
     **/
    List<FeedbackResponse> getAllByClubId(Long id);

    Page<FeedbackResponse> getPageByClubId(Long id, SortAndPageDto sortAndPageDto);

    /**
     * Method delete {@link Feedback} and update Club rating.
     *
     * @param id - place id
     * @return new {@code FeedbackResponse}
     **/
    FeedbackResponse deleteFeedbackById(Long id);

    /**
     * Method find {@link Feedback} by id, and update data and Club rating.
     *
     * @param id              - place id
     * @param feedbackProfile - put dto 'FeedbackProfile'
     * @return FeedbackResponse
     **/
    FeedbackResponse updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile);

    /**
     * The method validates feedbacks owner.
     */
    void validateFeedbackOwner(Long id, User user);

    ReplyResponse createReply(ReplyRequest replyRequest);

    long countByClubId(Long clubId);

    float ratingByClubId(Long clubId);
}
