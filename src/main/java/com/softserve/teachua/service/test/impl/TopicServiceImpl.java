package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.Topic;
import com.softserve.teachua.repository.test.TopicRepository;
import com.softserve.teachua.service.test.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.softserve.teachua.utils.test.NullValidator.*;
import static com.softserve.teachua.utils.test.Messages.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Transactional(readOnly = true)
    public Topic findByTitle(String title) {
        checkNull(title, "Topic title");
        return topicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format(NO_TITLE_MESSAGE, "topic", title)));
    }
}
