package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Topic;

import java.util.NoSuchElementException;

/**
 * This interface contains all methods needed to manage test topics.
 */
public interface TopicService {
    /**
     * This method returns a Topic entity found by title.
     * @param title - put topic title.
     * @return new {@code Topic}.
     * @throws NoSuchElementException if topic item does not exist.
     */
    Topic findByTitle(String title);
}
