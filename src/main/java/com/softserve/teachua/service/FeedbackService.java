package com.softserve.teachua.service;

import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.model.Feedback;

import javax.servlet.http.HttpServletRequest;
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
    SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile, HttpServletRequest httpServletRequest);

    /**
     * The method returns list of  {@code List<FeedbackResponse>}.
     *
     * @return new {@code List<FeedbackResponse>}
     **/
    List<FeedbackResponse> getListOfFeedback();

    /**
     * The method returns list of  {@code List<FeedbackResponse>} by club id.
     *
     * @return new {@code List<FeedbackResponse>}
     **/
    List<FeedbackResponse> getAllByClubId(Long id);

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
    FeedbackResponse updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile,
                                               HttpServletRequest httpServletRequest);

    /**
     * The method validates feedbacks owner.
     */
    void validateFeedbackOwner(Long id, HttpServletRequest httpServletRequest);
}
