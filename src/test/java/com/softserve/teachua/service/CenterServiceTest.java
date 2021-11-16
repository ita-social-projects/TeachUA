package com.softserve.teachua.service;

import com.softserve.teachua.converter.CenterToCenterResponseConverter;
import com.softserve.teachua.converter.CoordinatesConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.impl.CenterServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CenterServiceTest {

    private static final Long CORRECT_CENTER_ID = 1L;
    private static final Long WRONG_CENTER_ID = 1000L;
    private  static  final  Long CORRECT_LOCATION_ID = 1l;
    private  static  final  Long CLUB_ID = 1l;
    private static final List<Long> CLUBS_ID = new LinkedList<>();
    private static final Set<Club> CLUBS = new HashSet<>();
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
    private CoordinatesConverter coordinatesConverter;

    @InjectMocks
    private CenterServiceImpl centerService;
    private Center correctCenter;
    private CenterResponse correctCenterResponse;
    private CenterProfile centerProfile;
    private Center createCenter;
    private SuccessCreatedCenter successCreatedCenter;
    private HttpServletRequest httpServletRequest;
    private Club club;

    @BeforeAll
    public static void setUp() {
        CLUBS_ID.add(1L);
//        CLUBS_ID.add(2L);
        CLUBS.add(Club.builder().id(1L).build());
//        CLUBS.add(Club.builder().id(2L).build());
    }

    @BeforeEach
    public void setMocks() {

        correctCenter = Center.builder()
                .id(CORRECT_CENTER_ID)
                .name("Correct center")
                .contacts("Some contacts")
                .urlBackgroundPicture("URL to picture")
                .description("Description")
                .urlWeb("URL to picture")
                .urlWeb("URL to picture")
                .build();
        correctCenterResponse = CenterResponse.builder()
                .id(CORRECT_CENTER_ID)
                .name("Correct center")
                .phones("Some contacts")
                .urlBackgroundPicture("URL to picture")
                .description("Description")
                .urlWeb("URL to picture")
                .urlWeb("URL to picture")
                .socialLinks("Some links")
                .email("Email")
                .build();
        centerProfile = CenterProfile.builder()
                .name("Create center")
                .userId(1L)
                .clubsId(CLUBS_ID)
                .build();
        createCenter = Center.builder()
                .id(1L)
                .name(centerProfile.getName())
                .user(User.builder().id(1L).build())
                .clubs(CLUBS)
                .build();
        successCreatedCenter = SuccessCreatedCenter.builder()
                .id(1L)
                .name(centerProfile.getName())
                .build();
        club = Club.builder()
                .id(CLUB_ID).build();
    }

    @Test
    public void getCenterByCorrectIdShouldReturnCorrectCenter() {
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        Center actual = centerService.getCenterById(CORRECT_CENTER_ID);
        assertThat(actual).isEqualTo(correctCenter);
    }

    @Test
    public void getCenterByWrongIdShouldThrowNotExistException() {
        when(centerRepository.findById(WRONG_CENTER_ID)).thenReturn(Optional.empty());
        NotExistException exception = assertThrows(NotExistException.class, () -> centerService.getCenterById(WRONG_CENTER_ID));
        assertTrue(exception.getMessage().contains(WRONG_CENTER_ID.toString()));
    }

    @Test
    public void getCenterResponseByCorrectIdShouldReturnCorrectCenterResponse() {
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        when(centerToCenterResponseConverter.convertToCenterResponse(correctCenter))
                .thenReturn(correctCenterResponse);
        CenterResponse actual = centerService.getCenterByProfileId(CORRECT_CENTER_ID);
        assertThat(actual).isEqualTo(correctCenterResponse);
    }

    private void setAddCenterMocks() {
        when(centerRepository.save(any(Center.class)))
                .thenReturn(createCenter);
        when(dtoConverter.convertToEntity(any(CenterProfile.class), any(Center.class)))
                .thenReturn(new Center());
        when(dtoConverter.convertToDto(createCenter, SuccessCreatedCenter.class)).thenReturn(successCreatedCenter);
        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(new User());
        when(clubRepository.findById(1l)).thenReturn(Optional.of(club));
    }

    @Test
    public void addCenterShouldReturnSuccessCreatedCenterWithUserAndWithoutLocations() {
        setAddCenterMocks();
        assertThat(centerService.addCenterRequest(centerProfile, httpServletRequest)).isEqualTo(successCreatedCenter);
    }

    @Test
    public void addCenterShouldReturnSuccessCreatedCenterWithUserAndLocations() {
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
        assertThat(centerService.addCenterRequest(centerProfile, httpServletRequest)).isEqualTo(successCreatedCenter);
    }

    @Test
    public void addCenterShouldThrowAlreadyExistExceptionWhenCenterExist() {
        when(centerRepository.existsByName(centerProfile.getName())).thenReturn(true);
        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(User.builder().build());
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, ()
                -> centerService.addCenterRequest(centerProfile, httpServletRequest));
        assertThat(exception.getMessage()).contains(centerProfile.getName());
    }

    @Test
    public void deleteCenterShouldReturnCenterResponse() {
        when(clubRepository.findClubsByCenter(correctCenter)).thenReturn(Collections.emptyList());
        when(locationRepository.findLocationsByCenter(correctCenter)).thenReturn(Collections.emptyList());
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        when(dtoConverter.convertToDto(correctCenter, CenterResponse.class)).thenReturn(correctCenterResponse);
        assertThat(centerService.deleteCenterById(CORRECT_CENTER_ID)).isEqualTo(correctCenterResponse);
    }

    @Test
    public void updateCenterShouldThrowAlreadyExistException() {
        when(centerRepository.existsByName(centerProfile.getName())).thenReturn(true);
        when(centerRepository.findById(CORRECT_CENTER_ID)).thenReturn(Optional.of(correctCenter));
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> centerService.updateCenter(1L, centerProfile));
        assertThat(exception.getMessage()).contains(centerProfile.getName());
    }


}
