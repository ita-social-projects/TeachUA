package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.TopicController;
import com.softserve.teachua.dto.test.topic.TopicProfile;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.Topic;
import com.softserve.teachua.repository.test.TopicRepository;
import com.softserve.teachua.service.test.TopicService;
import static com.softserve.teachua.utils.test.Messages.NO_ID_MESSAGE;
import static com.softserve.teachua.utils.test.Messages.NO_TITLE_MESSAGE;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Topic findById(Long id) {
        checkNull(id, "Topic id");
        return topicRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, "topic", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic findByTitle(String title) {
        checkNull(title, "Topic title");
        return topicRepository.findByTitle(title)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, "topic", title)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicProfile> findAllTopicProfiles() {
        return mapToDtoList(topicRepository.findAll());
    }

    @Override
    public TopicProfile save(TopicProfile topicProfile) {
        checkNull(topicProfile, "Topic");
        Topic topic = modelMapper.map(topicProfile, Topic.class);
        topicRepository.save(topic);
        log.info("**/Topic has been created. {}", topic);
        return topicProfile;
    }

    @Override
    public TopicProfile updateById(TopicProfile topicProfile, Long id) {
        checkNull(topicProfile, "Topic");
        Topic topic = findById(id);
        topic.setTitle(topicProfile.getTitle());
        topicRepository.save(topic);
        log.info("**/Topic with id '{}' has been updated. {}", id, topic);
        return topicProfile;
    }

    private List<TopicProfile> mapToDtoList(List<Topic> topics) {
        List<TopicProfile> topicProfiles = new ArrayList<>();

        for (Topic topic : topics) {
            TopicProfile topicProfile = modelMapper.map(topic, TopicProfile.class);
            Link link = linkTo(methodOn(TopicController.class)
                    .updateTopic(topicProfile, topic.getId()))
                    .withRel("update");
            topicProfile.add(link);
            topicProfiles.add(topicProfile);
        }
        return topicProfiles;
    }
}
