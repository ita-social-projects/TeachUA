package com.softserve.teachua.controller;


import com.softserve.teachua.dto.controller.FeedbackResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedFeedback;
import com.softserve.teachua.dto.service.FeedbackProfile;
import com.softserve.teachua.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FeedbackController {
    FeedbackService feedbackService;

    @Autowired
    FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedback/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id){
        return feedbackService.getFeedbackProfileById(id);
    }

    @GetMapping("/feedbacks")
    public List<FeedbackResponse> getAllFeedback(){
        return feedbackService.getListOfFeedback();
    }

    @PostMapping("/feedback")
    public SuccessCreatedFeedback addFeedback(@RequestBody FeedbackProfile feedbackProfile){
        return feedbackService.addFeedback(feedbackProfile);
    }

    @PutMapping("/feedback/{id}")
    public FeedbackProfile updateFeedback(@PathVariable Long id, @RequestBody FeedbackProfile feedbackProfile)
    {
        return feedbackService.updateFeedbackProfileById(id,feedbackProfile);
    }

    @DeleteMapping("/feedback/{id}")
    public FeedbackResponse deleteFeedbackById(@PathVariable Long id){
        return feedbackService.deleteFeedbackById(id);
    }

}
