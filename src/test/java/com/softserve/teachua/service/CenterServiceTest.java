package com.softserve.teachua.service;

import com.softserve.teachua.TestUtils;
import com.softserve.teachua.converter.CenterToCenterResponseConverter;
import com.softserve.teachua.converter.CoordinatesConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.CenterArch;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.service.impl.CenterServiceImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CenterServiceTest {
    private static final Long CORRECT_CENTER_ID = 1L;
    private static final Long WRONG_CENTER_ID = 1000L;
    private static final List<Long> CLUBS_ID = new LinkedList<>();
    private static final Set<Club> CLUBS = new HashSet<>();
    private static final Long CORRECT_LOCATION_ID = 1L;
    private static final Long CLUB_ID = 1L;
    private static final Long USER_ID = 72L;
    private static final String USER_EMAIL = "user@gmail.com";

    @Mock
    private CenterRepository centerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ClubService clubService;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private CityService cityService;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private CenterToCenterResponseConverter centerToCenterResponseConverter;
    @Mock
    private UserService userService;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private LocationService locationService;
    @Mock
    private CoordinatesConverter coordinatesConverter;
    @InjectMocks
    private CenterServiceImpl centerService;
    private Center correctCenter;
    private CenterResponse correctCenterResponse;
    private CenterProfile centerProfile;
    private Center createCenter;
    private SuccessCreatedCenter successCreatedCenter;
    private Club club;
    private User user;
    private CenterArch centerArch;

    @BeforeAll
    static void setUp() {
        CLUBS_ID.add(1L);
        CLUBS.add(Club.builder().id(1L).build());
    }

    @BeforeEach
    void setMocks() {
        correctCenter = Center.builder().id(CORRECT_CENTER_ID).name("Correct center").contacts("Some contacts")
                .urlBackgroundPicture("URL to picture").description("Description").urlWeb("URL to picture")
                .urlWeb("URL to picture").locations(Sets.newSet()).clubs(CLUBS).build();
        correctCenterResponse = CenterResponse.builder().id(CORRECT_CENTER_ID).name("Correct center")
                .phones("Some contacts").urlBackgroundPicture("URL to picture").description("Description")
                .urlWeb("URL to picture").urlWeb("URL to picture").socialLinks("Some links").email("Email").build();
        centerProfile = CenterProfile.builder().name("Create center").userId(1L).clubsId(CLUBS_ID).build();
        createCenter = Center.builder().id(1L).name(centerProfile.getName()).user(User.builder().id(1L).build())
                .clubs(CLUBS).build();
        successCreatedCenter = SuccessCreatedCenter.builder().id(1L).name(centerProfile.getName()).build();
        club = Club.builder().id(CLUB_ID).center(correctCenter).build();

        user = User.builder().id(USER_ID).email(USER_EMAIL).build();

        centerArch = CenterArch.builder().name("Create center").userId(1L).build();
        centerService.setClubService(clubService);
    }

    @Test
    void getCenterByCorrectIdShouldReturnCorrectCenter() {
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        Center actual = centerService.getCenterById(CORRECT_CENTER_ID);
        assertThat(actual).isEqualTo(correctCenter);
    }

    @Test
    void getCenterByWrongIdShouldThrowNotExistException() {
        when(centerRepository.findById(WRONG_CENTER_ID)).thenReturn(Optional.empty());
        NotExistException exception = assertThrows(NotExistException.class,
                () -> centerService.getCenterById(WRONG_CENTER_ID));
        assertTrue(exception.getMessage().contains(WRONG_CENTER_ID.toString()));
    }

    @Test
    void getCenterResponseByCorrectIdShouldReturnCorrectCenterResponse() {
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        when(centerToCenterResponseConverter.convertToCenterResponse(correctCenter)).thenReturn(correctCenterResponse);
        CenterResponse actual = centerService.getCenterProfileById(CORRECT_CENTER_ID);
        assertThat(actual).isEqualTo(correctCenterResponse);
    }

    private void setAddCenterMocks() {
        when(centerRepository.save(any(Center.class))).thenReturn(createCenter);
        when(dtoConverter.convertToEntity(any(CenterProfile.class), any(Center.class))).thenReturn(new Center());
        when(dtoConverter.convertToDto(createCenter, SuccessCreatedCenter.class)).thenReturn(successCreatedCenter);
        when(clubService.getClubById(club.getId())).thenReturn(club);
        when(userDetailsService.getUserPrincipal()).thenReturn(TestUtils.getUserPrincipal(user));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    }

    private void setUpdateCenterMocks() {
        LocationProfile locationProfile = LocationProfile.builder().build();
        Location location = Location.builder().id(1L).center(createCenter).build();
        Set<Location> locations = new HashSet<>();
        locations.add(location);
        centerProfile.setLocations(Collections.singletonList(locationProfile));

        when(centerRepository.save(any(Center.class))).thenReturn(createCenter);
        when(dtoConverter.convertToEntity(any(CenterProfile.class), any(Center.class))).thenReturn(new Center());
        when(clubService.getClubById(club.getId())).thenReturn(club);
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        when(dtoConverter.convertToDto(createCenter, CenterProfile.class)).thenReturn(centerProfile);
    }

    @Test
    void addCenterShouldReturnSuccessCreatedCenterWithUserAndWithoutLocations() {
        setAddCenterMocks();
        assertThat(centerService.addCenterRequest(centerProfile)).isEqualTo(successCreatedCenter);
    }

    @Test
    void addCenterShouldReturnSuccessCreatedCenterWithUserAndLocations() {
        LocationProfile locationProfile = LocationProfile.builder().build();
        Location location = Location.builder().id(1L).center(createCenter).build();
        Set<Location> locations = new HashSet<>();
        locations.add(location);
        centerProfile.setLocations(Collections.singletonList(locationProfile));
        successCreatedCenter.setLocations(Collections.singletonList(locationProfile));
        createCenter.setLocations(locations);
        setAddCenterMocks();
        when(dtoConverter.convertToEntity(locationProfile, new Location())).thenReturn(location);
        when(locationRepository.save(location)).thenReturn(location);
        when(cityService.getCityByName(null)).thenReturn(null);
        when(clubService.getClubById(1L)).thenReturn(club);
        assertThat(centerService.addCenterRequest(centerProfile)).isEqualTo(successCreatedCenter);
    }

    @Test
    void addCenterShouldThrowAlreadyExistExceptionWhenCenterExist() {
        when(centerRepository.existsByName(centerProfile.getName())).thenReturn(true);

        assertThatThrownBy(() -> centerService.addCenter(centerProfile)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void testUpdateRatingNewFeedbackWhenCenterIsNull() {
        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .club(Club.builder().id(1L).center(null).build())
                .rate(4.5f)
                .build();
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(feedbackResponse.getClub()));
        centerService.updateRatingNewFeedback(feedbackResponse);
        verify(centerRepository, never()).save(any(Center.class));
    }

    @Test
    void testUpdateRatingNewFeedbackWhenCenterIsNotNull() {
        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .club(Club.builder().id(1L).center(correctCenter).build())
                .rate(4.5f)
                .build();
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(feedbackResponse.getClub()));
        centerService.updateRatingNewFeedback(feedbackResponse);
        assertEquals(correctCenter.getRating(), 4.5f);
        assertEquals(correctCenter.getFeedbackCount(), 1);
    }

    @Test
    void deleteCenterShouldReturnCenterResponse() {
        when(clubRepository.findClubsByCenter(correctCenter)).thenReturn(Collections.emptyList());
        when(locationRepository.findLocationsByCenter(correctCenter)).thenReturn(Collections.emptyList());
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        when(dtoConverter.convertToDto(correctCenter, CenterResponse.class)).thenReturn(correctCenterResponse);
        when(dtoConverter.convertToDto(correctCenter, CenterArch.class)).thenReturn(centerArch);
        when(archiveService.saveModel(centerArch)).thenReturn(Archive.builder().build());
        assertThat(centerService.deleteCenterById(CORRECT_CENTER_ID)).isEqualTo(correctCenterResponse);
    }

    @Test
    void updateCenterShouldReturnCenterProfile() {
        setUpdateCenterMocks();

        assertThat(centerService.updateCenter(CORRECT_CENTER_ID, centerProfile)).isEqualTo(centerProfile);
    }

    @Test
    void updateCenterByWrongIdShouldThrowNotExistException() {
        when(centerRepository.findById(WRONG_CENTER_ID)).thenReturn(Optional.empty());
        NotExistException exception = assertThrows(NotExistException.class,
                () -> centerService.updateCenter(WRONG_CENTER_ID, centerProfile));
        assertTrue(exception.getMessage().contains(WRONG_CENTER_ID.toString()));
    }
}
