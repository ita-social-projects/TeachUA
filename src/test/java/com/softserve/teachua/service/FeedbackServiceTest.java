package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
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

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock UserService userService;

    @Mock ClubService clubService;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback feedback;
    private FeedbackProfile feedbackProfile;
    private FeedbackResponse feedbackResponse;
    private Club club;
    private User user;

    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 700L;
    private final long EXISTING_CLUB_ID = 10L;
    private final long NOT_EXISTING_CLUB_ID = 500L;
    private final double CLUB_RATING = 5.0;
    private final String EXISTING_TEXT = "Existing feedback";
    private final String NEW_TEXT = "New feedback";
    private final Long USER_ID = 12L;
    private final String USER_EMAIL = "test@gmail.com";

    @BeforeEach
    public void setMocks() {
        user = User.builder().id(USER_ID).email(USER_EMAIL).build();
        club = Club.builder().id(EXISTING_CLUB_ID).build();
        feedback = Feedback.builder().id(EXISTING_ID).text(EXISTING_TEXT).club(club).user(user).build();
        feedbackProfile = FeedbackProfile.builder().id(EXISTING_ID).text(NEW_TEXT).userId(USER_ID).build();
        feedbackResponse = FeedbackResponse.builder().text(EXISTING_TEXT).user(user).build();
    }

    @Test
    public void getFeedbackByCorrectIdShouldReturnCorrectFeedBack() {
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));
        Feedback actual = feedbackService.getFeedbackById(EXISTING_ID);
        assertEquals(actual, feedback);
    }

    @Test
    public void getFeedbackByNotExistingIdTestShouldReturnNotExistException() {
        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            feedbackService.getFeedbackById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getAllByExistingClubIdShouldReturnListOfFeedbackResponse() {
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);
        //final long EXISTING_CLUB_ID = 3L;
        when(feedbackRepository.getAllByClubId(EXISTING_CLUB_ID)).thenReturn(feedbackList);
        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class)).thenReturn(feedbackResponse);

        List<FeedbackResponse> actual = feedbackService.getAllByClubId(EXISTING_CLUB_ID);
        assertEquals(feedbackResponse, actual.get(0));
    }

    @Test
    public void getAllByNotExistingClubIdShouldReturnEmpty() {
        when(feedbackRepository.getAllByClubId(NOT_EXISTING_CLUB_ID)).thenReturn(new ArrayList<>());

        List<FeedbackResponse> actual = feedbackService.getAllByClubId(NOT_EXISTING_CLUB_ID);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getFeedbackProfileByExistingIdShouldReturnFeedbackResponse(){
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));
        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .thenReturn(feedbackResponse);

        FeedbackResponse actual = feedbackService.getFeedbackProfileById(EXISTING_ID);
        assertEquals(actual.getText(), feedbackResponse.getText());
    }

    @Test
    public void getFeedbackProfileByNotExistingIdShouldReturnNotExistException(){
        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            feedbackService.getFeedbackProfileById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getListOfFeedbackTestShouldReturnListOfFeedbackResponse(){
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feedback);

        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class)).thenReturn(feedbackResponse);
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        List<FeedbackResponse> actual = feedbackService.getListOfFeedback();

        assertEquals(feedbackResponse, actual.get(0));
    }

    @Test
    public void addNewFeedbackShouldReturnSuccessCreatedFeedback() {
        Feedback newFeedback = Feedback.builder().id(NOT_EXISTING_ID).club(club).text(NEW_TEXT).build();
        when(clubRepository.existsById(EXISTING_CLUB_ID)).thenReturn(true);
        when(dtoConverter.convertToEntity(feedbackProfile, new Feedback())).thenReturn(newFeedback);
        when(feedbackRepository.save(newFeedback)).thenReturn(newFeedback);

        when(feedbackRepository.findAvgRating(EXISTING_CLUB_ID)).thenReturn(CLUB_RATING);
        doNothing().when(clubService).updateRatingNewFeedback(feedbackResponse);
        when(dtoConverter.convertToDto(newFeedback, SuccessCreatedFeedback.class))
                .thenReturn(SuccessCreatedFeedback.builder().text(NEW_TEXT).build());

        SuccessCreatedFeedback actual = feedbackService.addFeedback(feedbackProfile);
        assertEquals(feedbackProfile.getText(), actual.getText());
    }

    @Test
    public void updateFeedbackProfileByExistingIdShouldReturnFeedbackProfile(){
        Feedback updFeedback = Feedback.builder().id(EXISTING_ID).club(club).user(user).text(NEW_TEXT).build();
        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(user);
        feedbackProfile.setClubId(club.getId());
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));
        when(dtoConverter.convertToEntity(feedbackProfile, feedback)).thenReturn(updFeedback);
        when(feedbackRepository.save(updFeedback)).thenReturn(updFeedback);

//        when(feedbackRepository.findAvgRating(EXISTING_CLUB_ID)).thenReturn(CLUB_RATING);
//        doNothing().when(clubRepository).updateRating(EXISTING_CLUB_ID, CLUB_RATING);
        when(dtoConverter.convertToDto(updFeedback, FeedbackResponse.class))
                .thenReturn(FeedbackResponse.builder().id(EXISTING_ID).text(NEW_TEXT).club(club).user(user).build());

        FeedbackResponse actual = feedbackService.updateFeedbackProfileById(EXISTING_ID, feedbackProfile, httpServletRequest);

        assertEquals(updFeedback.getText(), actual.getText());
        assertEquals(updFeedback.getId(), actual.getId());
    }

    @Test
    public void updateFeedbackProfileByNotExistingIdShouldReturnNotExistException(){
        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        feedbackProfile.setClubId(club.getId());
        assertThatThrownBy(() -> {
            feedbackService.updateFeedbackProfileById(NOT_EXISTING_ID, feedbackProfile, httpServletRequest);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void deleteFeedbackByExistingIdShouldReturnFeedbackResponse(){
        feedbackResponse.setId(EXISTING_ID);

        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));

        when(archiveService.saveModel(feedback)).thenReturn(feedback);

//        when(feedbackRepository.findAvgRating(EXISTING_CLUB_ID)).thenReturn(CLUB_RATING);
        when(clubService.updateRatingDeleteFeedback(feedbackResponse)).thenReturn(SuccessUpdatedClub.builder().build());

        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .thenReturn(feedbackResponse);

        FeedbackResponse actual = feedbackService.deleteFeedbackById(EXISTING_ID);

        assertEquals(feedback.getId(), actual.getId());
    }

    @Test
    public void deleteFeedbackByNotExistingIdShouldReturnNotExistException(){
        feedbackResponse.setId(NOT_EXISTING_ID);

        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            feedbackService.deleteFeedbackById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getOptionalFeedbackByExistingIdShouldReturnOptionalOfFeedback(){
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));
        Optional<Feedback> actual = feedbackRepository.findById(EXISTING_ID);
        assertEquals(Optional.of(feedback), actual);
    }

    @Test
    public void getOptionalFeedbackByNotExistingIdShouldReturnEmpty(){
        when(feedbackRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());
        Optional<Feedback> actual = feedbackRepository.findById(NOT_EXISTING_ID);
        assertEquals(Optional.empty(), actual);
    }

}
