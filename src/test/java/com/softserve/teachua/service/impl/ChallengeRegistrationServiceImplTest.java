package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationResponse;
import com.softserve.teachua.dto.challenge_registration.FullChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UnapprovedChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationResponse;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeRegistration;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ChallengeRegistrationRepository;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChallengeRegistrationServiceImplTest {
    @InjectMocks
    private ChallengeRegistrationServiceImpl challengeRegistrationService;

    @Mock
    private ChallengeRegistrationRepository challengeRegistrationRepository;

    @Mock
    private ChildService childService;

    @Mock
    private UserService userService;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private DtoConverter dtoConverter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerChildren() {
        var request = new ChildrenChallengeRegistrationRequest(Arrays.asList(1L, 2L), 1L, "Comment");
        Challenge challenge = new Challenge();
        ChallengeRegistration challengeRegistration = new ChallengeRegistration();
        ChildrenChallengeRegistrationResponse response = new ChildrenChallengeRegistrationResponse();

        when(challengeService.getChallengeById(anyLong())).thenReturn(challenge);
        when(childService.getById(anyLong())).thenReturn(new Child());
        when(challengeRegistrationRepository.save(any())).thenReturn(challengeRegistration);
        when(dtoConverter.convertToDto(
                any(ChallengeRegistration.class), any(ChildrenChallengeRegistrationResponse.class))
        ).thenReturn(response);

        List<ChildrenChallengeRegistrationResponse> result = challengeRegistrationService.register(request);

        verify(challengeService, times(1)).getChallengeById(anyLong());
        verify(childService, times(2)).getById(anyLong());
        verify(challengeRegistrationRepository, times(2)).save(any());
        verify(dtoConverter, times(2)).convertToDto(any(ChallengeRegistration.class), any(ChildrenChallengeRegistrationResponse.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void registerUser() {
        UserChallengeRegistrationRequest request = new UserChallengeRegistrationRequest(1L, 2L, "Comment");
        User user = new User();
        Challenge challenge = new Challenge();
        ChallengeRegistration challengeRegistration = new ChallengeRegistration();
        UserChallengeRegistrationResponse response = new UserChallengeRegistrationResponse();

        when(challengeService.getChallengeById(anyLong())).thenReturn(challenge);
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(challengeRegistrationRepository.save(any())).thenReturn(challengeRegistration);
        when(dtoConverter.convertToDto(any(ChallengeRegistration.class), any(UserChallengeRegistrationResponse.class))).thenReturn(response);

        UserChallengeRegistrationResponse result = challengeRegistrationService.register(request);

        verify(challengeService, times(1)).getChallengeById(anyLong());
        verify(userService, times(1)).getUserById(anyLong());
        verify(challengeRegistrationRepository, times(1)).save(any());
        verify(dtoConverter, times(1)).convertToDto(any(ChallengeRegistration.class), any(UserChallengeRegistrationResponse.class));

        assertNotNull(result);
    }

    @Test
    void getAllUnapprovedByManagerId() {
        // Given
        Long managerId = 1L;
        List<ChallengeRegistration> challengeRegistrations = List.of(new ChallengeRegistration(), new ChallengeRegistration());
        UnapprovedChallengeRegistration dto = new UnapprovedChallengeRegistration();

        when(challengeRegistrationRepository.findAllUnapprovedByManagerId(managerId)).thenReturn(challengeRegistrations);
        when(dtoConverter.convertToDto(any(ChallengeRegistration.class), any(UnapprovedChallengeRegistration.class))).thenReturn(dto);

        // When
        List<UnapprovedChallengeRegistration> result = challengeRegistrationService.getAllUnapprovedByManagerId(managerId);

        // Then
        verify(challengeRegistrationRepository, times(1)).findAllUnapprovedByManagerId(managerId);
        verify(dtoConverter, times(2)).convertToDto(any(ChallengeRegistration.class), any(UnapprovedChallengeRegistration.class));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto));
    }

    @Test
    void approve() {
        Long challengeRegistrationId = 1L;

        challengeRegistrationService.approve(challengeRegistrationId);

        verify(challengeRegistrationRepository, times(1)).approveChallengeRegistration(challengeRegistrationId);
    }

    @Test
    void existsActiveRegistrationForChildren() {
        Long challengeId = 1L;
        Long childId = 1L;

        when(challengeRegistrationRepository.existsActiveRegistrationByChildIdAndChallengeId(challengeId, childId)).thenReturn(true);

        boolean result = challengeRegistrationService.existsActiveRegistrationForChildren(challengeId, childId);

        verify(challengeRegistrationRepository, times(1)).existsActiveRegistrationByChildIdAndChallengeId(challengeId, childId);

        assertTrue(result);
    }

    @Test
    void getChildrenForCurrentUserAndCheckIsDisabledByChallengeId() {
        Long challengeId = 1L;
        Child child1 = new Child();
        child1.setId(1L);
        Child child2 = new Child();
        child2.setId(2L);
        User user = new User();
        user.setChildren(Set.of(child1, child2));
        ChildResponse childResponse = new ChildResponse();

        when(userService.getAuthenticatedUserWithChildren()).thenReturn(user);
        when(dtoConverter.convertToDto(any(Child.class), any(ChildResponse.class))).thenReturn(childResponse);
        when(challengeRegistrationRepository.existsActiveRegistrationByChildIdAndChallengeId(anyLong(), anyLong())).thenReturn(false);

        List<ChildResponse> result = challengeRegistrationService.getChildrenForCurrentUserAndCheckIsDisabledByChallengeId(challengeId);

        verify(userService, times(1)).getAuthenticatedUserWithChildren();
        verify(dtoConverter, times(2)).convertToDto(any(Child.class), any(ChildResponse.class));
        verify(challengeRegistrationRepository, times(2)).existsActiveRegistrationByChildIdAndChallengeId(anyLong(), anyLong());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getApplicationsForUserAndChildrenByUserId() {
        Long userId = 1L;
        List<ChallengeRegistration> registrations = Arrays.asList(new ChallengeRegistration(), new ChallengeRegistration());
        FullChallengeRegistration fullChallengeRegistration = new FullChallengeRegistration();

        when(challengeRegistrationRepository.findRegistrationsByUserIdOrChildParentId(userId)).thenReturn(registrations);
        when(dtoConverter.convertToDto(any(ChallengeRegistration.class), any(FullChallengeRegistration.class))).thenReturn(fullChallengeRegistration);

        List<FullChallengeRegistration> result = challengeRegistrationService.getApplicationsForUserAndChildrenByUserId(userId);

        verify(challengeRegistrationRepository, times(1)).findRegistrationsByUserIdOrChildParentId(userId);
        verify(dtoConverter, times(2)).convertToDto(any(ChallengeRegistration.class), any(FullChallengeRegistration.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void cancel() {
        Long challengeRegistrationId = 1L;

        challengeRegistrationService.cancel(challengeRegistrationId);

        verify(challengeRegistrationRepository, times(1)).cancelChallengeRegistration(challengeRegistrationId);
    }

    @Test
    void getAllChallengeRegistrationsByManagerId() {
        Long managerId = 1L;
        List<ChallengeRegistration> challengeRegistrations = List.of(new ChallengeRegistration(), new ChallengeRegistration());
        FullChallengeRegistration dto = new FullChallengeRegistration();

        when(challengeRegistrationRepository.findByChallenge_User_Id(managerId)).thenReturn(challengeRegistrations);
        when(dtoConverter.convertToDto(any(ChallengeRegistration.class), any(FullChallengeRegistration.class))).thenReturn(dto);

        List<FullChallengeRegistration> result = challengeRegistrationService.getAllChallengeRegistrationsByManagerId(managerId);

        verify(challengeRegistrationRepository, times(1)).findByChallenge_User_Id(managerId);
        verify(dtoConverter, times(2)).convertToDto(any(ChallengeRegistration.class), any(FullChallengeRegistration.class));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto));
    }

    @Test
    void isUserAlreadyRegisteredToChallenge() {
        when(challengeRegistrationRepository
                .existsByChallenge_IdAndUser_Id(anyLong(), anyLong())).thenReturn(true);

        boolean result = challengeRegistrationService.isUserAlreadyRegisteredToChallenge(anyLong(), anyLong());

        verify(challengeRegistrationRepository, times(1))
                .existsByChallenge_IdAndUser_Id(anyLong(), anyLong());

        assertTrue(result);
    }
}
