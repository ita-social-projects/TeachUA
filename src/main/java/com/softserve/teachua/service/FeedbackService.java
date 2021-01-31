package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.FeedbackResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedFeedback;
import com.softserve.teachua.dto.service.FeedbackProfile;
import com.softserve.teachua.model.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse getFeedbackProfileById(Long id);

    Feedback getFeedbackById(Long id);

    SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile);

    List<FeedbackResponse> getListOfFeedback();

    FeedbackProfile updateFeedbackProfileById(Long id,FeedbackProfile feedbackProfile);

    FeedbackResponse deleteFeedbackById(Long id);
}
