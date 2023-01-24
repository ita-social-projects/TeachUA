package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.topic.TopicProfile;
import com.softserve.teachua.model.test.Topic;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This interface contains all methods needed to manage test topics.
 */
public interface TopicService {
    /**
     * This method returns Topic entity found by id.
     * @param id - put Topic id.
     * @return new {@code Topic}
     */
    Topic findById(Long id);

    /**
     * This method returns a Topic entity found by title.
     * @param title - put topic title.
     * @return new {@code Topic}.
     * @throws NoSuchElementException if topic item does not exist.
     */
    Topic findByTitle(String title);

    /**
     * This method returns list of all topics.
     * @return new {@code List<TopicProfile>}.
     */
    List<TopicProfile> findAllTopicProfiles();

    /**
     * This method creates new Topic entity.
     * @param topicProfile - contains information about the new topic.
     */
    TopicProfile save(TopicProfile topicProfile);

    /**
     * This method returns dto {@code TopicProfile} if topic was successfully updated.
     * @param topicProfile - contains information about the new topic.
     * @param id - put topic id here.
     * @return new {@code TopicProfile}
     */
    TopicProfile updateById(TopicProfile topicProfile, Long id);
}
