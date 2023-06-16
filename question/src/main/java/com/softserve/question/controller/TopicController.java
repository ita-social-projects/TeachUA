package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.topic.TopicProfile;
import com.softserve.question.service.TopicService;
import jakarta.validation.Valid;
import java.util.List;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing topics.
 */
@RestController
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
    @GetMapping(path = "/topics", produces = APPLICATION_JSON_VALUE)
    public List<TopicProfile> getTopics() {
        return topicService.findAllTopicProfiles();
    }

    /**
     * Use this endpoint to create the test topic.
     *
     * @param topicProfile - put information about the topic here.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(path = "/topics", consumes = APPLICATION_JSON_VALUE)
    public void createTopic(@Valid @RequestBody TopicProfile topicProfile) {
        topicService.save(topicProfile);
    }

    /**
     * User this endpoint to update the test topic.
     *
     * @param topicProfile - put information about the topic here.
     * @param id           - put topic id here.
     */
    @ResponseStatus(value = NO_CONTENT)
    @PutMapping(path = "/topics/{id}", consumes = APPLICATION_JSON_VALUE)
    public TopicProfile updateTopic(@Valid @RequestBody TopicProfile topicProfile,
                                    @PathVariable Long id) {
        return topicService.updateById(topicProfile, id);
    }
}
