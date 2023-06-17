package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.topic.TopicProfile;
import com.softserve.question.service.TopicService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing topics.
 */
@RestController
@RequestMapping("/api/v1/question/topic")
public class TopicController implements Api {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Use this endpoint to get all topics.
     * The controller returns a list of topic DTOs {@code List<TopicProfile>}.
     *
     * @return new {@code List<TopicProfile>}.
     */
    @GetMapping("/all")
    public List<TopicProfile> getTopics() {
        return topicService.findAllTopicProfiles();
    }

    /**
     * Use this endpoint to create the test topic.
     *
     * @param topicProfile - put information about the topic here.
     */
    @PostMapping
    public void createTopic(@Valid @RequestBody TopicProfile topicProfile) {
        topicService.save(topicProfile);
    }

    /**
     * User this endpoint to update the test topic.
     *
     * @param topicProfile - put information about the topic here.
     * @param id           - put topic id here.
     */
    @PutMapping(path = "/{id}")
    public TopicProfile updateTopic(@Valid @RequestBody TopicProfile topicProfile,
                                    @PathVariable Long id) {
        return topicService.updateById(topicProfile, id);
    }
}
