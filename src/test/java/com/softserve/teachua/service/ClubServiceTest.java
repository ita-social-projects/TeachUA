package com.softserve.teachua.service;

import static com.softserve.teachua.TestUtils.getUser;
import com.softserve.teachua.constants.Days;
import com.softserve.teachua.converter.ClubToClubResponseConverter;
import com.softserve.teachua.converter.ContactsStringConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubOwnerProfile;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.WorkTime;
import com.softserve.teachua.model.archivable.ClubArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ComplaintRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.repository.WorkTimeRepository;
import com.softserve.teachua.service.impl.ClubServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.util.Lists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {
    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 500L;
    private final String EXISTING_NAME = "Existing Club Name";
    private final String NOT_EXISTING_NAME = "Not Existing Club Name";
    private final String NEW_NAME = "New Club Name";
    private final String CONTACTS = "123456789";
    private final String DESCRIPTION = "Description Text";
    private final WorkTime workTime =WorkTime.builder().day(Days.MONDAY).startTime("13:23").endTime("14:23").build();
    private final List<String> CATEGORIES = Arrays.asList("Category1", "Category2");
    private final Set<Category> CATEGORIES_SET = Sets.newSet(Category.builder().name("Category1").build(),
            Category.builder().name("Category2").build());
    private final List<WorkTime> WORK_TIMES_SET =  Arrays.asList(workTime);
    private final Long USER_EXISTING_ID = 1L;
    private final Long USER_NOT_EXISTING_ID = 100L;
    private final String USER_EXISTING_NAME = "User Existing Name";
    private final String USER_EXISTING_LASTNAME = "User Existing LastName";
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private ClubToClubResponseConverter clubToClubResponseConverter;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private WorkTimeRepository workTimeRepository;
    @Mock
    private FileUploadService fileUploadService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private LocationService locationService;
    @Mock
    private WorkTimeService workTimeService;
    @Mock
    private ContactsStringConverter contactsStringConverter;
    @Mock
    private ComplaintRepository complaintRepository;
    @InjectMocks
    private ClubServiceImpl clubService;
    private Club club;
    private ClubProfile clubProfile;
    private ClubResponse clubResponse;
    private User user;
    private ClubArch clubArch;

    @BeforeEach
    public void setUp() {
        user = User.builder().id(USER_EXISTING_ID).firstName(USER_EXISTING_NAME).lastName(USER_EXISTING_LASTNAME)
                .build();
        club = Club.builder().id(EXISTING_ID).name(NEW_NAME).user(user).categories(CATEGORIES_SET)
                .feedbacks(Sets.newSet()).locations(Sets.newSet()).workTimes(Sets.newSet()).urlGallery(Lists.list()).build();
        clubProfile = ClubProfile.builder().name(NEW_NAME).description(DESCRIPTION).contacts(CONTACTS)
                .categoriesName(CATEGORIES).workTimes(WORK_TIMES_SET).build();
        clubResponse = ClubResponse.builder().id(EXISTING_ID).name(NEW_NAME).build();
        clubArch = ClubArch.builder().id(EXISTING_ID).name(NEW_NAME).build();
    }

    @Test
    void getClubProfileByExistingIdShouldReturnCorrectClubResponce() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);

        ClubResponse actual = clubService.getClubProfileById(EXISTING_ID);
        assertNotNull(actual);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void getClubProfileByNotExistingIdShouldThrowNotExistException() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubProfileById(NOT_EXISTING_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubByExistingIdShouldReturnCorrectClub() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubById(EXISTING_ID);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingIdShouldThrowNotExistException() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubById(NOT_EXISTING_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubByNameShouldReturnCorrectClub() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubByName(EXISTING_NAME);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingNameShouldThrowNotExistException() {
        when(clubRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubByName(NOT_EXISTING_NAME)).isInstanceOf(NotExistException.class);
    }

    @Test
    void updateClubByExistingIdShouldReturnCorrectSuccessUpdatedClub() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(clubRepository.save(any())).thenReturn(club);
        when(dtoConverter.convertToEntity(
                ClubResponse.builder().name(NEW_NAME).categories(Collections.emptySet()).workTimes(Collections.emptySet()).build(), club))
                .thenReturn(Club.builder().name(NEW_NAME).build());
        when(dtoConverter.convertToDto(club, SuccessUpdatedClub.class))
                .thenReturn(SuccessUpdatedClub.builder().name(NEW_NAME).build());
        when(userService.getAuthenticatedUser()).thenReturn(user);

        SuccessUpdatedClub actual = clubService.updateClub(EXISTING_ID,
                ClubResponse.builder().name(NEW_NAME).categories(Collections.emptySet()).workTimes(Collections.emptySet()).build());
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void updateClubByNotExistingIdShouldThrowNotExistException() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        ClubResponse build = ClubResponse.builder().name(NEW_NAME).build();

        assertThatThrownBy(() -> clubService.updateClub(NOT_EXISTING_ID,
                build)).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubProfileByExistingNameShouldReturnCorrectClubResponse() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);

        ClubResponse actual = clubService.getClubProfileByName(EXISTING_NAME);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void getClubProfileByNotExistingNameShouldThrowNotExistException() {
        when(clubRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubProfileByName(NOT_EXISTING_NAME)).isInstanceOf(
                NotExistException.class);
    }

    @Test
    void deleteClubByExistingIdShouldReturnCorrectClubResponse() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        doNothing().when(clubRepository).deleteById(EXISTING_ID);
        when(complaintRepository.getAllByClubId(EXISTING_ID)).thenReturn(Collections.emptyList());
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(dtoConverter.convertToDto(club, ClubArch.class)).thenReturn(clubArch);
        when(archiveService.saveModel(clubArch)).thenReturn(Archive.builder().build());
        ClubResponse actual = clubService.deleteClubById(EXISTING_ID);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void deleteClubByNotExistingIdShouldThrowNotExistException() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.deleteClubById(NOT_EXISTING_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    void addClubShouldReturnSuccessCreatedClub() {
        Club newClub = Club.builder().name(NEW_NAME).build();

        when(clubRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(clubProfile, new Club())).thenReturn(newClub);
        when(clubRepository.save(any())).thenReturn(newClub);
        when(dtoConverter.convertToDto(newClub, SuccessCreatedClub.class))
                .thenReturn(SuccessCreatedClub.builder().name(NEW_NAME).build());
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(dtoConverter.convertToEntity(workTime, new WorkTime())).thenReturn(workTime);

        SuccessCreatedClub actual = clubService.addClub(clubProfile);
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void addClubIfExistShouldThrowAlreadyExistException() {
        when(clubRepository.existsByName(NEW_NAME)).thenReturn(true);
        assertThatThrownBy(() -> clubService.addClub(clubProfile)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void getListOfClubsShouldReturnListOfClubResponses() {
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);
        when(clubRepository.findAll()).thenReturn(Collections.singletonList(club));

        List<ClubResponse> actual = clubService.getListOfClubs();
        assertEquals(clubResponse.getName(), actual.get(0).getName());
    }

    @Test
    void changeClubOwner() {
        User axpectedUser = getUser();
        ClubOwnerProfile clubOwnerProfile = ClubOwnerProfile.builder()
                .id(club.getId())
                .user(axpectedUser)
                .build();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(clubRepository.findById(club.getId())).thenReturn(Optional.of(club));

        clubService.changeClubOwner(user.getId(), clubOwnerProfile);

        assertEquals(axpectedUser, club.getUser());
    }
}
