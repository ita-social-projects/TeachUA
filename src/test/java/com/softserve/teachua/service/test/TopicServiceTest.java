package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.dto.test.topic.TopicProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.Topic;
import com.softserve.teachua.repository.test.TopicRepository;
import com.softserve.teachua.service.test.impl.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TopicServiceImpl topicService;

    private Topic topic;
    private TopicProfile topicProfile;

    private final Long EXISTING_TOPIC_ID = 1L;
    private final Long NOT_EXISTING_TOPIC_ID = 100L;

    private final String EXISTING_TOPIC_TITLE = "Existing topic title";
    private final String NOT_EXISTING_TOPIC_TITLE = "Not existing topic title";

    @BeforeEach
    public void setUp() {
        topic = Topic.builder()
                .id(EXISTING_TOPIC_ID)
                .title(EXISTING_TOPIC_TITLE)
                .build();
        topicProfile = TopicProfile.builder()
                .title(EXISTING_TOPIC_TITLE)
                .build();
    }

    @Test
    void findByExistingIdShouldReturnTopic() {
        when(topicRepository.findById(EXISTING_TOPIC_ID)).thenReturn(Optional.of(topic));

        Topic actual = topicService.findById(EXISTING_TOPIC_ID);
        assertEquals(topic, actual);
    }

    @Test
    void findByNotExistingIdShouldThrowNotExistException() {
        when(topicRepository.findById(NOT_EXISTING_TOPIC_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> topicService.findById(NOT_EXISTING_TOPIC_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findByNullIdShouldThrowNotExistException() {
        assertThatThrownBy(() -> topicService.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByExistingTitleShouldReturnTopic() {
        when(topicRepository.findByTitle(NOT_EXISTING_TOPIC_TITLE)).thenReturn(Optional.of(topic));

        Topic actual = topicService.findByTitle(NOT_EXISTING_TOPIC_TITLE);
        assertEquals(topic, actual);
    }

    @Test
    void findByNotExistingTitleShouldThrowNotExistException() {
        when(topicRepository.findByTitle(NOT_EXISTING_TOPIC_TITLE)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> topicService.findByTitle(NOT_EXISTING_TOPIC_TITLE))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findByNullTitleShouldThrowNotExistException() {
        assertThatThrownBy(() -> topicService.findByTitle(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllShouldReturnListOfTopics() {
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
        when(modelMapper.map(topic, TopicProfile.class)).thenReturn(topicProfile);

        List<TopicProfile> actual = topicService.findAllTopicProfiles();
        assertEquals(topicProfile, actual.get(0));
    }

    @Test
    void saveWithNewTitleShouldReturnSameTopicProfile() {
        TopicProfile newTopicProfile = TopicProfile.builder()
                .title(NOT_EXISTING_TOPIC_TITLE)
                .build();
        Topic newTopic = Topic.builder()
                .title(NOT_EXISTING_TOPIC_TITLE)
                .build();
        when(topicRepository.existsByTitle(NOT_EXISTING_TOPIC_TITLE)).thenReturn(false);
        when(modelMapper.map(newTopicProfile, Topic.class)).thenReturn(newTopic);
        when(topicRepository.save(newTopic)).thenReturn(newTopic);

        TopicProfile actual = topicService.save(newTopicProfile);
        assertEquals(newTopicProfile, actual);
    }

    @Test
    void saveTopicWithExistingTitleShouldThrowAlreadyExistException() {
        when(topicRepository.existsByTitle(EXISTING_TOPIC_TITLE)).thenReturn(true);

        assertThatThrownBy(() -> topicService.save(topicProfile))
                .isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void saveTopicWithNullTitleShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> topicService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTopicWithNotExistingTitleShouldReturnTopicProfile() {
        Topic newTopic = Topic.builder()
                .title(NOT_EXISTING_TOPIC_TITLE)
                .build();
        TopicProfile newTopicProfile = TopicProfile.builder()
                .title(NOT_EXISTING_TOPIC_TITLE)
                .build();
        when(topicRepository.existsByTitle(NOT_EXISTING_TOPIC_TITLE)).thenReturn(false);
        when(topicRepository.findById(EXISTING_TOPIC_ID)).thenReturn(Optional.of(topic));
        when(topicRepository.save(newTopic)).thenReturn(newTopic);

        TopicProfile actual = topicService.updateById(newTopicProfile, EXISTING_TOPIC_ID);
        assertEquals(newTopicProfile, actual);
    }

    @Test
    void updateTopicWithNotExistingIdShouldThrowNotExistException() {
        when(topicRepository.existsByTitle(EXISTING_TOPIC_TITLE)).thenReturn(false);
        when(topicRepository.findById(NOT_EXISTING_TOPIC_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topicService.updateById(topicProfile, NOT_EXISTING_TOPIC_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void updateTopicWithExistingTitleShouldThrowAlreadyExistException() {
        when(topicRepository.existsByTitle(EXISTING_TOPIC_TITLE)).thenReturn(true);

        assertThatThrownBy(() -> topicService.updateById(topicProfile, EXISTING_TOPIC_ID))
                .isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void updateTopicWithNullTitleShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> topicService.updateById(null, EXISTING_TOPIC_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTopicWithNullIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> topicService.updateById(topicProfile, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}