package com.softserve.question.service;

import com.softserve.question.dto.topic.TopicProfile;
import com.softserve.question.model.Topic;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This interface contains all methods needed to manage test topics.
 */
public interface TopicService {
    /**
     * Find a Topic entity by given id.
     *
     * @param id a Topic id
     * @return the Topic entity found by id
     */
    Topic findById(Long id);

    /**
     * Find a Topic entity by title.
     *
     * @param title topic title
     * @return a Topic entity found by title
     * @throws NoSuchElementException if topic item does not exist
     */
    Topic findByTitle(String title);

    /**
     * Find all Topic profiles.
     *
     * @return a list of all topics.
     */
    List<TopicProfile> findAllTopicProfiles();

    /**
     * Create a new Topic entity.
     *
     * @param topicProfile contains information about the new topic.
     */
    TopicProfile save(TopicProfile topicProfile);

    /**
     * Update Topic by given id and TopicProfile.
     *
     * @param topicProfile contains information about the new topic.
     * @param id topic id
     * @return dto {@code TopicProfile} if the topic was successfully updated
     */
    TopicProfile updateById(TopicProfile topicProfile, Long id);
}
