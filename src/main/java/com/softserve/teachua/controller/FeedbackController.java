package com.softserve.teachua.controller;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FeedbackController implements Api {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * The method to get Feedback by id
     *
     * @param id of Feedback
     * @return FeedbackResponse
     */
    @GetMapping("/feedback/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackProfileById(id);
    }

    /**
     * The method to get all Feedbacks
     *
     * @return List of FeedbackResponse
     */
    @GetMapping("/feedbacks")
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackService.getListOfFeedback();
    }

    /**
     * The method to get all Feedbacks by Club id
     *
     * @return List of FeedbackResponse
     */
    @GetMapping("/feedbacks/{id}")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long id) {
        return feedbackService.getAllByClubId(id);
    }

    /**
     * The method to create a new Feedback
     *
     * @param feedbackProfile - object of DTO class
     * @return SuccessCreatedFeedback
     */
    @PostMapping("/feedback")
    public SuccessCreatedFeedback addFeedback(
            @Valid
            @RequestBody FeedbackProfile feedbackProfile) {
        return feedbackService.addFeedback(feedbackProfile);
    }

    /**
     * The method to update Feedback
     *
     * @param id
     * @param feedbackProfile
     * @return FeedbackProfile
     */
    @PutMapping("/feedback/{id}")
    public FeedbackResponse updateFeedback(
            @PathVariable Long id,
            @Valid
            @RequestBody FeedbackProfile feedbackProfile,
            HttpServletRequest httpServletRequest) {
        return feedbackService.updateFeedbackProfileById(id, feedbackProfile, httpServletRequest);
    }

    /**
     * The method to delete Feedback
     *
     * @param id
     * @return FeedbackResponse
     */
    @DeleteMapping("/feedback/{id}")
    public FeedbackResponse deleteFeedbackById(@PathVariable Long id) {
        return feedbackService.deleteFeedbackById(id);
    }

}
