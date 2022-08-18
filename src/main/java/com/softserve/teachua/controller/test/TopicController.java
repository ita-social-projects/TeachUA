package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.topic.TopicProfile;
import com.softserve.teachua.service.test.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller is for managing topics.
 * */

@RequiredArgsConstructor
@RestController
public class TopicController implements Api {
    private final TopicService topicService;

    /**
     * Use this endpoint to get all topics.
     * The controller returns a list of topic DTOs {@code List<TopicProfile>}.
     *
     * @return new {@code List<TopicProfile>}.
     */
    @GetMapping(path = "/topics", produces = APPLICATION_JSON_VALUE)
    public List<TopicProfile>  getTopics() {
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
    @PutMapping(path = "/topics/{id}", consumes = APPLICATION_JSON_VALUE)
    public TopicProfile updateTopic(@Valid @RequestBody TopicProfile topicProfile,
                                    @PathVariable Long id) {
        return topicService.updateById(topicProfile, id);
    }
}
