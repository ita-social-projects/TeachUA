package com.softserve.teachua.service;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private UserService userService;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequestWrapper httpServletRequest;

    @Mock
    private ClubService clubService;

    @Mock
    private Role role;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;


    private Feedback feedback;
    private Feedback updFeedback;
    private FeedbackProfile feedbackProfile;
    private FeedbackResponse updFeedbackResponse;
    private FeedbackResponse feedbackResponse;
    private User user;
    private Club club;

    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 2L;
    private final long EXISTING_CLUB_ID = 10L;
    private final long NOT_EXISTING_CLUB_ID = 20L;
    private final long FEEDBACK_COUNT = 1;
    private final long EXISTING_USER_ID= 100L;
    private final double CLUB_RATING = 4.0;;
    private final String EXISTING_TEXT = "Existing feedback";
    private final String NEW_TEXT = "New feedback";
    private final Long USER_ID = 12L;
    private final String USER_EMAIL = "test@gmail.com";
    private final Integer ROLE_ID = 3;

     @BeforeEach
    public void setMocks() {
        club = Club.builder().id(EXISTING_CLUB_ID).build();
        role = Role.builder().id(ROLE_ID).name(RoleData.ADMIN.getDBRoleName()).build();
        user = User.builder().id(EXISTING_USER_ID).email(USER_EMAIL).role(role).build();
        feedback = Feedback.builder().id(EXISTING_ID).text(EXISTING_TEXT).club(club).user(user).build();
        feedbackProfile = FeedbackProfile.builder().id(EXISTING_ID).text(NEW_TEXT).userId(USER_ID).clubId(club.getId()).build();
        feedbackResponse = FeedbackResponse.builder().text(EXISTING_TEXT).user(user).id(EXISTING_ID).build();
        updFeedback = Feedback.builder().id(EXISTING_ID).text(NEW_TEXT).club(club).user(user).build();
        updFeedbackResponse = FeedbackResponse.builder().text(NEW_TEXT).user(user).club(club).id(EXISTING_ID).build();
    }

    @Test
    public void getFeedbackByCorrectIdShouldReturnCorrectFeedback() {
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

        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(user);

        when(clubRepository.existsById(EXISTING_CLUB_ID)).thenReturn(true);
        when(userRepository.existsById(EXISTING_USER_ID)).thenReturn(true);
        when(dtoConverter.convertToEntity(feedbackProfile, new Feedback())).thenReturn(feedback);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        when(feedbackRepository.findAvgRating(EXISTING_CLUB_ID)).thenReturn(CLUB_RATING);
        doNothing().when(clubRepository).updateRating(EXISTING_CLUB_ID, CLUB_RATING, FEEDBACK_COUNT);
        when(dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class))
                .thenReturn(SuccessCreatedFeedback.builder().text(NEW_TEXT).build());

        SuccessCreatedFeedback actual = feedbackService.addFeedback(feedbackProfile,httpServletRequest);
        assertEquals(feedbackProfile.getText(), actual.getText());
    }

    @Test
    public void updateFeedbackProfileByExistingIdShouldReturnFeedbackProfile(){
        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(user);
        when(feedbackRepository.findById(EXISTING_ID)).thenReturn(Optional.of(feedback));
        when(dtoConverter.convertToEntity(feedbackProfile, feedback)).thenReturn(updFeedback);
        when(dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .thenReturn(feedbackResponse);
        when(feedbackRepository.save(updFeedback)).thenReturn(updFeedback);
        when(dtoConverter.convertToDto(updFeedback, FeedbackResponse.class))
                .thenReturn(updFeedbackResponse);

        when(clubService.updateRatingEditFeedback(feedbackResponse, updFeedbackResponse)).thenReturn(null);

        assertThat(feedbackService.updateFeedbackProfileById(EXISTING_ID, feedbackProfile, httpServletRequest))
                .isEqualTo(updFeedbackResponse);
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
