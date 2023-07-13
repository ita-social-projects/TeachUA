package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.dto.club_registration.ClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationResponse;
import com.softserve.teachua.dto.club_registration.FullClubRegistration;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationResponse;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.ClubRegistration;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRegistrationRepository;
import com.softserve.teachua.service.impl.ClubRegistrationServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class ClubRegistrationServiceImplTest {
    @InjectMocks
    private ClubRegistrationServiceImpl clubRegistrationService;

    @Mock
    private ClubRegistrationRepository clubRegistrationRepository;

    @Mock
    private ChildService childService;

    @Mock
    private UserService userService;

    @Mock
    private ClubService clubService;

    @Mock
    private DtoConverter dtoConverter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClubRegistration() {
        ClubRegistrationRequest request = new ClubRegistrationRequest(Arrays.asList(1L, 2L), 1L, "Comment");
        Club club = new Club();
        ClubRegistration clubRegistration = new ClubRegistration();
        ClubRegistrationResponse response = new ClubRegistrationResponse();

        when(clubService.getClubById(anyLong())).thenReturn(club);
        when(childService.getById(anyLong())).thenReturn(new Child());
        when(clubRegistrationRepository.save(any())).thenReturn(clubRegistration);
        when(dtoConverter.convertToDto(
                any(ClubRegistration.class), any(ClubRegistrationResponse.class))
        ).thenReturn(response);

        List<ClubRegistrationResponse> result = clubRegistrationService.create(request);

        verify(clubService, times(1)).getClubById(anyLong());
        verify(childService, times(2)).getById(anyLong());
        verify(clubRegistrationRepository, times(2)).save(any());
        verify(dtoConverter, times(2)).convertToDto(any(ClubRegistration.class), any(ClubRegistrationResponse.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void create_UserClubRegistration() {
        UserClubRegistrationRequest request = new UserClubRegistrationRequest(1L, 2L, "Comment");
        User user = new User();
        Club club = new Club();
        ClubRegistration clubRegistration = new ClubRegistration();
        UserClubRegistrationResponse response = new UserClubRegistrationResponse();

        when(clubService.getClubById(anyLong())).thenReturn(club);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(clubRegistrationRepository.save(any())).thenReturn(clubRegistration);
        when(dtoConverter.convertToDto(any(ClubRegistration.class), any(UserClubRegistrationResponse.class))).thenReturn(response);

        UserClubRegistrationResponse result = clubRegistrationService.create(request);

        verify(clubService, times(1)).getClubById(anyLong());
        verify(userService, times(1)).getUserById(anyLong());
        verify(clubRegistrationRepository, times(1)).save(any());
        verify(dtoConverter, times(1)).convertToDto(any(ClubRegistration.class), any(UserClubRegistrationResponse.class));

        assertNotNull(result);
    }


    @Test
    void approveTest() {
        Long clubRegistrationId = 1L;

        clubRegistrationService.approve(clubRegistrationId);

        verify(clubRegistrationRepository, times(1)).approveClubRegistration(clubRegistrationId);
    }

    @Test
    void cancelTest() {
        Long clubRegistrationId = 1L;

        clubRegistrationService.cancel(clubRegistrationId);

        verify(clubRegistrationRepository, times(1)).cancelClubRegistration(clubRegistrationId);
    }

    @Test
    void existsActiveRegistrationTest() {
        Long clubId = 1L;
        Long childId = 1L;

        when(clubRegistrationRepository.existsActiveRegistration(clubId, childId)).thenReturn(true);

        boolean result = clubRegistrationService.existsActiveRegistration(clubId, childId);

        verify(clubRegistrationRepository, times(1)).existsActiveRegistration(clubId, childId);

        assertTrue(result);
    }

    @Test
    void getChildrenForCurrentUserAndCheckIsDisabledByClubIdTest() {
        Long clubId = 1L;
        Child child1 = new Child();
        child1.setId(1L);
        Child child2 = new Child();
        child2.setId(2L);
        User user = new User();
        user.setChildren(Set.of(child1, child2));
        ChildResponse childResponse = new ChildResponse();

        when(userService.getAuthenticatedUserWithChildren()).thenReturn(user);
        when(dtoConverter.convertToDto(any(Child.class), any(ChildResponse.class))).thenReturn(childResponse);
        when(clubRegistrationRepository.existsActiveRegistration(anyLong(), anyLong())).thenReturn(false);

        List<ChildResponse> result = clubRegistrationService.getChildrenForCurrentUserAndCheckIsDisabledByClubId(clubId);

        verify(userService, times(1)).getAuthenticatedUserWithChildren();
        verify(dtoConverter, times(2)).convertToDto(any(Child.class), any(ChildResponse.class));
        verify(clubRegistrationRepository, times(2)).existsActiveRegistration(anyLong(), anyLong());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getApplicationsByUserIdTest() {
        Long userId = 1L;
        List<ClubRegistration> registrations = Arrays.asList(new ClubRegistration(), new ClubRegistration());
        FullClubRegistration fullClubRegistration = new FullClubRegistration();

        when(clubRegistrationRepository.findRegistrationsByUserIdOrChildParentId(userId)).thenReturn(registrations);
        when(dtoConverter.convertToDto(any(ClubRegistration.class), any(FullClubRegistration.class))).thenReturn(fullClubRegistration);

        List<FullClubRegistration> result = clubRegistrationService.getApplicationsByUserId(userId);

        verify(clubRegistrationRepository, times(1)).findRegistrationsByUserIdOrChildParentId(userId);
        verify(dtoConverter, times(2)).convertToDto(any(ClubRegistration.class), any(FullClubRegistration.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
