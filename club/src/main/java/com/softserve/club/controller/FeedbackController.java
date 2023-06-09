package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.feedback.FeedbackProfile;
import com.softserve.club.dto.feedback.FeedbackResponse;
import com.softserve.club.dto.feedback.SuccessCreatedFeedback;
import com.softserve.club.service.FeedbackService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the feedbacks.
 */

@Slf4j
@RestController
//@Tag(name = "feedback", description = "the Feedback API")
//@SecurityRequirement(name = "api")
public class FeedbackController implements Api {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Use this endpoint to get Feedback by id. The controller returns {@code FeedbackResponse}.
     *
     * @param id
     *            - put id of Feedback here.
     *
     * @return {@code FeedbackResponse}.
     */
    @GetMapping("/feedback/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackProfileById(id);
    }

    /**
     * Use this endpoint to get all Feedbacks. The controller returns {@code List<FeedbackResponse>}.
     *
     * @return {@code List<FeedbackResponse>}.
     */
    @GetMapping("/feedbacks")
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackService.getListOfFeedback();
    }

    /**
     * Use this endpoint to get all Feedbacks by Club id. The controller returns {@code List<FeedbackResponse>}.
     *
     * @return {@code List<FeedbackResponse>}
     */
    @GetMapping("/feedbacks/{id}")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long id) {
        return feedbackService.getAllByClubId(id);
    }

    /**
     * Use this endpoint to create a new Feedback. The controller returns {@code SuccessCreatedFeedback}.
     *
     * @param feedbackProfile
     *            - object of DTO class.
     *
     * @return {@code SuccessCreatedFeedback}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/feedback")
    public SuccessCreatedFeedback addFeedback(@Valid @RequestBody FeedbackProfile feedbackProfile) {
        return feedbackService.addFeedback(feedbackProfile);
    }

    /**
     * Use this endpoint to update Feedback by id. The controller returns {@code FeedbackProfile}.
     *
     * @param id
     *            - put feedback id here.
     * @param feedbackProfile
     *            - put feedback information here.
     *
     * @return {@code FeedbackProfile}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/feedback/{id}")
    public FeedbackResponse updateFeedback(@PathVariable Long id, @Valid @RequestBody FeedbackProfile feedbackProfile) {
        return feedbackService.updateFeedbackProfileById(id, feedbackProfile);
    }

    /**
     * Use this endpoint to delete Feedback by id. The controller returns {@code FeedbackResponse}.
     *
     * @param id
     *            - put feedback id here.
     *
     * @return {@code FeedbackResponse}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/feedback/{id}")
    public FeedbackResponse deleteFeedbackById(@PathVariable Long id) {
        return feedbackService.deleteFeedbackById(id);
    }
}
