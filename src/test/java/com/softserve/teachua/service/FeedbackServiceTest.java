package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {
    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback feedback;
    private FeedbackProfile feedbackProfile;
    private FeedbackResponse feedbackResponse;
    private Club club;

    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 700L;
    final long EXISTING_CLUB_ID = 10L;
    final long NOT_EXISTING_CLUB_ID = 500L;

    private final String EXISTING_TEXT = "Existing feedback";
    private final String NEW_TEXT = "New feedback";

    @BeforeEach
    public void init() {
        club = Club.builder().id(EXISTING_CLUB_ID).build();
        feedback = Feedback.builder().id(EXISTING_ID).text(EXISTING_TEXT).club(club).build();
        feedbackProfile = FeedbackProfile.builder().text(NEW_TEXT).build();
        feedbackResponse = FeedbackResponse.builder().text(EXISTING_TEXT).build();
    }

    @Test
    public void getFeedbackByIdTest() {
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));

        Feedback actual = feedbackService.getFeedbackById(EXISTING_ID);
        assertEquals(actual, feedback);
    }

    @Test
    public void getFeedbackByNotExistingIdTest() {
        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            feedbackService.getFeedbackById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getAllByExistingClubId() {
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);
        final long EXISTING_CLUB_ID = 3L;
        when(feedbackRepository.getAllByClubId(EXISTING_CLUB_ID)).thenReturn(feedbackList);
        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class)).thenReturn(feedbackResponse);

        List<FeedbackResponse> actual = feedbackService.getAllByClubId(EXISTING_CLUB_ID);
        assertEquals(feedbackResponse, actual.get(0));
    }

    @Test
    public void getAllByNotExistingClubId() {
        when(feedbackRepository.getAllByClubId(NOT_EXISTING_CLUB_ID)).thenReturn(new ArrayList<>());

        List<FeedbackResponse> actual = feedbackService.getAllByClubId(NOT_EXISTING_CLUB_ID);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void addNewFeedbackTest() {
        Feedback newFeedback = Feedback.builder().id(NOT_EXISTING_ID).club(club).text(NEW_TEXT).build();
        when(dtoConverter.convertToEntity(feedbackProfile, new Feedback())).thenReturn(newFeedback);
        when(feedbackRepository.save(newFeedback)).thenReturn(newFeedback);

        final double CLUB_RATING = 5.0;
        when(feedbackRepository.findAvgRating(EXISTING_CLUB_ID)).thenReturn(CLUB_RATING);
        doNothing().when(clubRepository).updateRating(EXISTING_CLUB_ID, CLUB_RATING);
        when(dtoConverter.convertToDto(newFeedback, SuccessCreatedFeedback.class))
                .thenReturn(SuccessCreatedFeedback.builder().text(NEW_TEXT).build());

        SuccessCreatedFeedback actual = feedbackService.addFeedback(feedbackProfile);
        assertEquals(feedbackProfile.getText(), actual.getText());
    }

}
