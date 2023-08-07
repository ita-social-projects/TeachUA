package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.SortAndPageDto;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.ReplyRequest;
import com.softserve.teachua.dto.feedback.ReplyResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "feedback", description = "the Feedback API")
@SecurityRequirement(name = "api")
public class FeedbackController implements Api {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Use this endpoint to get Feedback by id. The controller returns {@code FeedbackResponse}.
     *
     * @param id - put id of Feedback here.
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
    public ResponseEntity<Page<FeedbackResponse>> getAllFeedbackByClubId(
            @PathVariable Long id,
            @Valid SortAndPageDto sortAndPageDto) {
        Page<FeedbackResponse> feedbackPage = feedbackService.getPageByClubId(id, sortAndPageDto);
        return ResponseEntity.ok(feedbackPage);
    }

    @GetMapping("/feedbacks/count/{clubId}")
    public ResponseEntity<Long> countForClub(@PathVariable Long clubId) {
        long count = feedbackService.countByClubId(clubId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/feedbacks/rating/{clubId}")
    public ResponseEntity<Float> ratingForClub(@PathVariable Long clubId) {
        Float count = feedbackService.ratingByClubId(clubId);

        return ResponseEntity.ok(count);
    }

    /**
     * Use this endpoint to create a new Feedback. The controller returns {@code SuccessCreatedFeedback}.
     *
     * @param feedbackProfile - object of DTO class.
     * @return {@code SuccessCreatedFeedback}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/feedback")
    public SuccessCreatedFeedback addFeedback(@Valid @RequestBody FeedbackProfile feedbackProfile) {
        return feedbackService.createFeedback(feedbackProfile);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/feedback/{id}/reply")
    public ResponseEntity<ReplyResponse> addReply(
            @PathVariable Long id,
            @Valid @RequestBody ReplyRequest replyRequest) {
        replyRequest.setParentCommentId(id);
        log.debug("got request to create new reply {}", replyRequest);

        ReplyResponse reply = feedbackService.createReply(replyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    /**
     * Use this endpoint to update Feedback by id. The controller returns {@code FeedbackProfile}.
     *
     * @param id              - put feedback id here.
     * @param feedbackProfile - put feedback information here.
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
     * @param id - put feedback id here.
     * @return {@code FeedbackResponse}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/feedback/{id}")
    public FeedbackResponse deleteFeedbackById(@PathVariable Long id) {
        return feedbackService.deleteFeedbackById(id);
    }
}
