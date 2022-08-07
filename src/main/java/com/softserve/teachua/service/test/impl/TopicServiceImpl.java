package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Topic;
import com.softserve.teachua.repository.test.TopicRepository;
import com.softserve.teachua.service.test.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.softserve.teachua.utils.NullValidator.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    public Topic findByTitle(String title) {
        checkNull(title, "Topic title");
        return topicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no topic with title '%s'", title)));
    }
}
