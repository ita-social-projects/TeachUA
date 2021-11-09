package com.softserve.teachua.service;

import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.model.Feedback;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FeedbackService {

    FeedbackResponse getFeedbackProfileById(Long id);

    Feedback getFeedbackById(Long id);

    SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile, HttpServletRequest httpServletRequest);

    List<FeedbackResponse> getListOfFeedback();

    List<FeedbackResponse> getAllByClubId(Long id);

    FeedbackProfile updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile);

    FeedbackResponse deleteFeedbackById(Long id);
}
