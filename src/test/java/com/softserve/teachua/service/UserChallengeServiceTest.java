package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.TestUserChallengeStatusEnum;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.user_challenge.UserChallengeCreateResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeDeleteResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeUpdateResponse;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminDelete;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetByChallengeIdDurationId;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminUpdate;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForUserCreate;
import com.softserve.teachua.dto.user_challenge.exist.UserChallengeForExist;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileDelete;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserCreateWithDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetLocalDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetString;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.model.archivable.UserChallengeArch;
import com.softserve.teachua.repository.UserChallengeRepository;
import com.softserve.teachua.service.impl.UserChallengeServiceImpl;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.softserve.teachua.TestConstants.CHALLENGE_NAME;
import static com.softserve.teachua.TestConstants.CHALLENGE_IS_NOT_ACTIVE;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_ALREADY_EXIST;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_NOT_FOUND;
import static com.softserve.teachua.TestConstants.CHALLENGE_NOT_FOUND;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_DELETING_ERROR;
import static com.softserve.teachua.TestConstants.NUM_OF_GENERATED_ELEMENTS;
import static com.softserve.teachua.TestConstants.NUM_OF_METHOD_CALLS;
import static com.softserve.teachua.TestConstants.USER_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_ID;
import static com.softserve.teachua.TestConstants.DURATION_ENTITY_ID;
import static com.softserve.teachua.TestConstants.ROLE_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_IS_ACTIVE;
import static com.softserve.teachua.TestConstants.START_DATE;
import static com.softserve.teachua.TestConstants.END_DATE;
import static com.softserve.teachua.TestConstants.USER_EXIST;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ACTIVE;
import static com.softserve.teachua.TestUtils.getUserChallengeForProfileGetList;
import static com.softserve.teachua.TestUtils.getDurationEntity;
import static com.softserve.teachua.TestUtils.getChallenge;
import static com.softserve.teachua.TestUtils.getChallengeDuration;
import static com.softserve.teachua.TestUtils.getUserChallengeForUserCreateWithDate;
import static com.softserve.teachua.TestUtils.getUserChallengeCreateResponse;
import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserChallengeStatus;
import static com.softserve.teachua.TestUtils.getUserChallenge;
import static com.softserve.teachua.TestUtils.getUserChallengeForProfileDelete;
import static com.softserve.teachua.TestUtils.getUserChallengeDeleteResponse;
import static com.softserve.teachua.TestUtils.getUserChallengeForUserGetLocalDateList;
import static com.softserve.teachua.TestUtils.getUserChallengeForUserGetStringList;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminGetList;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminGetChallengeDurationList;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminRegisteredUserList;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminGetByChallengeIdDurationId;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminNotRegisteredUserList;
import static com.softserve.teachua.TestUtils.getUserChallengeForUserCreate;
import static com.softserve.teachua.TestUtils.getUserChallengeForExistList;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminUpdate;
import static com.softserve.teachua.TestUtils.getUserChallengeUpdateResponse;
import static com.softserve.teachua.TestUtils.getUserChallengeForAdminDelete;
import static com.softserve.teachua.TestUtils.getUserChallengeArch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserChallengeServiceTest {

    @InjectMocks
    private UserChallengeServiceImpl underTest;
    @Mock
    private UserChallengeRepository userChallengeRepository;
    @Mock
    private ChallengeDurationService challengeDurationService;
    @Mock
    private UserChallengeStatusService userChallengeStatusService;
    @Mock
    private ChallengeService challengeService;
    @Mock
    private UserService userService;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private ObjectMapper objectMapper;
    private ChallengeDuration challengeDuration;
    private DurationEntity durationEntity;
    private Challenge challenge;
    private UserChallengeStatus userChallengeStatus;
    private UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate;
    private UserChallengeCreateResponse userChallengeCreateResponse;
    private UserChallengeForProfileDelete userChallengeForProfileDelete;
    private UserChallengeDeleteResponse userChallengeDeleteResponse;
    private User user;
    private UserChallenge userChallenge;
    private UserChallengeForUserCreate userChallengeForUserCreate;
    private UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId;
    private UserChallengeForAdminUpdate userChallengeForAdminUpdate;
    private UserChallengeUpdateResponse userChallengeUpdateResponse;
    private UserChallengeForAdminDelete userChallengeForAdminDelete;
    private UserChallengeArch userChallengeArch;
    private List<UserChallengeForProfileGet> userChallengeForProfileGetList;
    private List<UserChallengeForUserGetLocalDate> userChallengeForUserGetLocalDateList;
    private List<UserChallengeForUserGetString> userChallengeForUserGetStringList;
    private List<UserChallengeForAdminGet> userChallengeForAdminGetList;
    private List<UserChallengeForAdminGetChallengeDuration> userChallengeForAdminGetChallengeDurationList;
    private List<UserChallengeForAdminRegisteredUser> userChallengeForAdminRegisteredUserList;
    private List<UserChallengeForAdminNotRegisteredUser> userChallengeForAdminNotRegisteredUserList;
    private List<UserChallengeForExist> userChallengeForExistList;

    @Test
    void getUserChallengeForProfileByUserIdSuccess() {
        getPreparedDataForGetUserChallengeForProfileByUserId(NUM_OF_GENERATED_ELEMENTS, USER_CHALLENGE_ID, CHALLENGE_ID,
            START_DATE, START_DATE, END_DATE);
        when(userChallengeRepository.getUserChallengeForProfileByUserId(USER_ID))
            .thenReturn(userChallengeForProfileGetList);
        assertThat(underTest.getUserChallengeForProfileByUserId(USER_ID))
            .isNotNull()
            .isEqualTo(userChallengeForProfileGetList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeForProfileByUserId(USER_ID);
    }

    @Test
    void getUserChallengeForProfileByUserIdException() {
        when(userChallengeRepository.getUserChallengeForProfileByUserId(USER_ID)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getUserChallengeForProfileByUserId(USER_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeForProfileByUserId(USER_ID);
    }

    @Test
    void createUserChallengeByUser() {
        getPreparedDataForCreateUserChallengeByUser(CHALLENGE_DURATION_ID, USER_EXIST, CHALLENGE_ID, CHALLENGE_NAME,
            CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, USER_ID);
        when(challengeDurationService.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_ID, START_DATE, END_DATE)).thenReturn(challengeDuration);
        assertThat(underTest.createUserChallengeByUser(userChallengeForUserCreateWithDate))
            .isEqualTo(userChallengeCreateResponse);
    }

    @Test
    void getUserChallengeByUserIdChallengeIdStartDateEndDate() {
        getPreparedDataForGetUserChallengeByUserIdChallengeIdStartDateEndDate(CHALLENGE_DURATION_ID, USER_EXIST,
            CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, USER_ID,
            USER_CHALLENGE_ID, ROLE_ID, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE);
        when(userChallengeRepository.getUserChallengeByUserIdChallengeIdStartDateEndDate(
            USER_ID, CHALLENGE_ID, START_DATE, END_DATE)).thenReturn(userChallenge);
        assertThat(underTest.getUserChallengeByUserIdChallengeIdStartDateEndDate(
            USER_ID, CHALLENGE_ID, START_DATE, END_DATE))
            .isNotNull()
            .isEqualTo(userChallenge);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeByUserIdChallengeIdStartDateEndDate(
            USER_ID, CHALLENGE_ID, START_DATE, END_DATE);
    }

    @Test
    void deleteUserChallengeForProfile() {
        getPreparedDataForDeleteUserChallengeForProfile(CHALLENGE_DURATION_ID, USER_EXIST,
            CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, USER_ID,
            USER_CHALLENGE_ID, ROLE_ID, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE);
        when(underTest.getUserChallengeByUserIdChallengeIdStartDateEndDate(
            USER_ID, CHALLENGE_ID, START_DATE, END_DATE)).thenReturn(userChallenge);
        assertThat(underTest.deleteUserChallengeForProfile(userChallengeForProfileDelete))
            .isEqualTo(userChallengeDeleteResponse);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).delete(userChallenge);
    }

    @Test
    void getListUserChallengeDurationByChallengeIdSuccessUserActive() {
        getPreparedDataForGetListUserChallengeDurationByChallengeIdSuccessUserActive(NUM_OF_GENERATED_ELEMENTS,
            CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE, START_DATE, END_DATE);
        when(userChallengeRepository.getListUserChallengeDurationByChallengeId(CHALLENGE_ID))
            .thenReturn(userChallengeForUserGetLocalDateList);
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        assertThat(underTest.getListUserChallengeDurationByChallengeId(CHALLENGE_ID))
            .isNotNull()
            .isEqualTo(userChallengeForUserGetStringList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListUserChallengeDurationByChallengeId(CHALLENGE_ID);
    }

    @Test
    void getListUserChallengeDurationByChallengeIdExceptionUserNonActive() {
        getPreparedDataForGetListUserChallengeDurationByChallengeIdExceptionUserNonActive(
            CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_NOT_ACTIVE);
        when(challengeService.getChallengeById(CHALLENGE_ID))
            .thenReturn(challenge);
        assertThatThrownBy(() -> underTest.getListUserChallengeDurationByChallengeId(CHALLENGE_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(String.format(CHALLENGE_NOT_FOUND, CHALLENGE_ID));
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    void getListUserChallengeDurationByChallengeIdExceptionEmpty() {
        getPreparedDataForGetListUserChallengeDurationByChallengeIdExceptionEmpty(
            CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE);
        when(userChallengeRepository.getListUserChallengeDurationByChallengeId(CHALLENGE_ID))
            .thenReturn(Collections.emptyList());
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        assertThatThrownBy(() -> underTest.getListUserChallengeDurationByChallengeId(CHALLENGE_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListUserChallengeDurationByChallengeId(CHALLENGE_ID);
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    void checkIfChallengeIsActiveTrue() {
        getPreparedDataForCheckIfChallengeIsActiveTrue(CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE);
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        assertThat(underTest.checkIfChallengeIsActive(CHALLENGE_ID)).isTrue();
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    void checkIfChallengeIsActiveFalse() {
        getPreparedDataForCheckIfChallengeIsActiveFalse(CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_NOT_ACTIVE);
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        assertThat(underTest.checkIfChallengeIsActive(CHALLENGE_ID)).isFalse();
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    void checkIfDateAfterNowDateTrue() {
        assertThat(underTest.checkIfDateAfterNowDate(LocalDate.MAX)).isTrue();
    }

    @Test
    void checkIfDateAfterNowDateFalse() {
        assertThat(underTest.checkIfDateAfterNowDate(LocalDate.MIN)).isFalse();
    }

    @Test
    void getListUserChallengeForAdminSuccess() {
        getPreparedDataForGetListUserChallengeForAdmin(
            NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE);
        when(userChallengeRepository.getListUserChallengeForAdmin())
            .thenReturn(userChallengeForAdminGetList);
        assertThat(underTest.getListUserChallengeForAdmin())
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeForAdminGetList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListUserChallengeForAdmin();
    }

    @Test
    void getListUserChallengeForAdminException() {
        when(userChallengeRepository.getListUserChallengeForAdmin()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListUserChallengeForAdmin())
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListUserChallengeForAdmin();
    }

    @Test
    void getListChallengeDurationForAdminSuccess() {
        getPreparedDataForGetListChallengeDurationForAdminSuccess(NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID,
            DURATION_ENTITY_ID, USER_EXIST, START_DATE, END_DATE);
        when(userChallengeRepository.getListChallengeDurationForAdmin(CHALLENGE_ID))
            .thenReturn(userChallengeForAdminGetChallengeDurationList);
        assertThat(underTest.getListChallengeDurationForAdmin(CHALLENGE_ID))
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeForAdminGetChallengeDurationList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListChallengeDurationForAdmin(CHALLENGE_ID);
    }

    @Test
    void getListChallengeDurationForAdminException() {
        when(userChallengeRepository.getListChallengeDurationForAdmin(CHALLENGE_ID))
            .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListChallengeDurationForAdmin(CHALLENGE_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListChallengeDurationForAdmin(CHALLENGE_ID);
    }

    @Test
    void getListRegisteredUsersByChallengeIdChallengeDurationIdSuccess() {
        getPreparedDataForGetListRegisteredUsersByChallengeIdChallengeDurationIdSuccess(
            NUM_OF_GENERATED_ELEMENTS, USER_CHALLENGE_ID, USER_ID, START_DATE, CHALLENGE_ID, CHALLENGE_DURATION_ID);
        when(userChallengeRepository.getListRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID,
            CHALLENGE_DURATION_ID))
            .thenReturn(userChallengeForAdminRegisteredUserList);
        assertThat(underTest.getListRegisteredUsersByChallengeIdChallengeDurationId(
            userChallengeForAdminGetByChallengeIdDurationId))
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeForAdminRegisteredUserList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
    }

    @Test
    void getListRegisteredUsersByChallengeIdChallengeDurationIdException() {
        getPreparedDataForGetListRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
        when(userChallengeRepository.getListRegisteredUsersByChallengeIdChallengeDurationId(
            CHALLENGE_ID, CHALLENGE_DURATION_ID)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListRegisteredUsersByChallengeIdChallengeDurationId(
            userChallengeForAdminGetByChallengeIdDurationId))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
    }

    @Test
    void getListNotRegisteredUsersByChallengeIdChallengeDurationIdSuccess() {
        getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdSuccess(
            CHALLENGE_ID, CHALLENGE_DURATION_ID, NUM_OF_GENERATED_ELEMENTS, USER_ID);
        when(userChallengeRepository.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            CHALLENGE_ID, CHALLENGE_DURATION_ID)).thenReturn(userChallengeForAdminNotRegisteredUserList);
        assertThat(underTest.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            userChallengeForAdminGetByChallengeIdDurationId))
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeForAdminNotRegisteredUserList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListNotRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
    }

    @Test
    void getListNotRegisteredUsersByChallengeIdChallengeDurationIdIfEmptySuccess() {
        getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdIfEmptySuccess(
            CHALLENGE_ID, CHALLENGE_DURATION_ID, NUM_OF_GENERATED_ELEMENTS, USER_ID);
        when(userChallengeRepository.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            CHALLENGE_ID, CHALLENGE_DURATION_ID)).thenReturn(Collections.emptyList());
        when(userChallengeRepository.getListAllNotRegisteredUsers())
            .thenReturn(userChallengeForAdminNotRegisteredUserList);
        assertThat(underTest.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            userChallengeForAdminGetByChallengeIdDurationId))
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeForAdminNotRegisteredUserList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListNotRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
    }


    @Test
    void getListNotRegisteredUsersByChallengeIdChallengeDurationIdException() {
        getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdException(
            CHALLENGE_ID, CHALLENGE_DURATION_ID);
        when(userChallengeRepository.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            CHALLENGE_ID, CHALLENGE_DURATION_ID)).thenReturn(Collections.emptyList());
        when(userChallengeRepository.getListAllNotRegisteredUsers()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            userChallengeForAdminGetByChallengeIdDurationId))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_NOT_FOUND);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS))
            .getListNotRegisteredUsersByChallengeIdChallengeDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListAllNotRegisteredUsers();
    }

    @Test
    void createUserChallengeFromAdmin() {
        getPreparedDataForCreateUserChallengeFromAdmin(CHALLENGE_DURATION_ID, USER_EXIST, CHALLENGE_ID, CHALLENGE_NAME,
            CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, USER_ID, DURATION_ENTITY_ID);
        when(challengeDurationService.getChallengeDurationByChallengeIdAndDurationId(
            CHALLENGE_ID, CHALLENGE_DURATION_ID)).thenReturn(challengeDuration);
        assertThat(underTest.createUserChallengeFromAdmin(userChallengeForUserCreate)).isEqualTo(
            userChallengeCreateResponse);
        verify(challengeDurationService, times(NUM_OF_METHOD_CALLS))
            .getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, CHALLENGE_DURATION_ID);
    }

    @Test
    void createUserChallengeException() {
        getPreparedDataForCreateUserChallengeException(CHALLENGE_DURATION_ID, USER_EXIST, CHALLENGE_ID, CHALLENGE_NAME,
            CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, NUM_OF_GENERATED_ELEMENTS, USER_ID);
        when(underTest.getListUserChallengeForExist()).thenReturn(userChallengeForExistList);
        assertThatThrownBy(() -> underTest.createUserChallenge(USER_ID, challengeDuration))
            .isInstanceOf(AlreadyExistException.class)
            .hasMessage(String.format(USER_CHALLENGE_ALREADY_EXIST, challengeDuration.getChallenge().getName()));
    }

    @Test
    void updateUserChallengeFromAdmin() {
        getPreparedDataForUpdateUserChallengeFromAdmin(USER_ID, ROLE_ID, USER_CHALLENGE_ID, CHALLENGE_DURATION_ID,
            USER_EXIST, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE, CHALLENGE_ID, CHALLENGE_NAME,
            CHALLENGE_IS_ACTIVE, DURATION_ENTITY_ID, START_DATE, END_DATE, NUM_OF_GENERATED_ELEMENTS);
        when(userService.getUserById(USER_ID)).thenReturn(user);
        when(userChallengeRepository.getUserChallengeById(USER_CHALLENGE_ID)).thenReturn(userChallenge);
        when(challengeDurationService.getChallengeDurationById(CHALLENGE_DURATION_ID)).thenReturn(challengeDuration);
        when(userChallengeRepository.save(userChallenge)).thenReturn(userChallenge);
        assertThat(underTest.updateUserChallengeFromAdmin(userChallengeForAdminUpdate))
            .isEqualTo(userChallengeUpdateResponse);
        verify(userService, times(NUM_OF_METHOD_CALLS)).getUserById(USER_ID);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeById(USER_CHALLENGE_ID);
        verify(challengeDurationService, times(NUM_OF_METHOD_CALLS)).getChallengeDurationById(CHALLENGE_DURATION_ID);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).save(userChallenge);

    }

    @Test
    void updateUserByIdAndUserChallengeData() {
        getPreparedDataForUpdateUserByIdAndUserChallengeData(USER_ID, ROLE_ID, USER_CHALLENGE_ID);
        when(userService.getUserById(USER_ID)).thenReturn(user);
        assertThat(underTest.updateUserByIdAndUserChallengeData(USER_ID, userChallengeForAdminUpdate))
            .isEqualTo(user);
        verify(userService, times(NUM_OF_METHOD_CALLS)).getUserById(USER_ID);
    }

    @Test
    void getListUserChallengeForExistSuccess() {
        getPreparedDataForGetListUserChallengeForExistSuccess(
            NUM_OF_GENERATED_ELEMENTS, USER_ID, CHALLENGE_DURATION_ID);
        when(userChallengeRepository.getListUserChallengeForExist()).thenReturn(userChallengeForExistList);
        assertThat(underTest.getListUserChallengeForExist())
            .isNotNull()
            .isEqualTo(userChallengeForExistList);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListUserChallengeForExist();
    }

    @Test
    void isUserChallengeExistByUserIdChallengeDurationIdTrue() {
        getPreparedDataForIsUserChallengeExistByUserIdChallengeDurationIdTrue(
            NUM_OF_GENERATED_ELEMENTS, USER_ID, CHALLENGE_DURATION_ID);
        when(underTest.getListUserChallengeForExist()).thenReturn(userChallengeForExistList);
        assertThat(underTest.isUserChallengeExistByUserIdChallengeDurationId(USER_ID, CHALLENGE_DURATION_ID)).isTrue();
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListUserChallengeForExist();
    }

    @Test
    void isUserChallengeExistByUserIdChallengeDurationIdFalse() {
        when(underTest.getListUserChallengeForExist()).thenReturn(Collections.emptyList());
        assertThat(underTest.isUserChallengeExistByUserIdChallengeDurationId(USER_ID, CHALLENGE_DURATION_ID)).isFalse();
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).getListUserChallengeForExist();
    }

    @Test
    void deleteUserChallengeForAdmin() {
        getPreparedDataForDeleteUserChallengeForAdmin(
            USER_ID, ROLE_ID, CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE,DURATION_ENTITY_ID, START_DATE,
            END_DATE, CHALLENGE_DURATION_ID, USER_EXIST, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE,
            USER_CHALLENGE_ID);
        when(userChallengeRepository.getUserChallengeByUserIdAndChallengeDurationId(
            USER_ID, CHALLENGE_ID, DURATION_ENTITY_ID)).thenReturn(userChallenge);
        assertThat(underTest.deleteUserChallengeForAdmin(userChallengeForAdminDelete))
            .isEqualTo(userChallengeDeleteResponse);
    }

    @Test
    void deleteUserChallengeException() {
        getPreparedDataForDeleteUserChallengeException(
            USER_ID, ROLE_ID, CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE,DURATION_ENTITY_ID, START_DATE,
            END_DATE, CHALLENGE_DURATION_ID, USER_EXIST, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE,
            USER_CHALLENGE_ID);
        doThrow(new ValidationException(USER_CHALLENGE_DELETING_ERROR))
            .when(userChallengeRepository).delete(userChallenge);
        assertThatThrownBy(() -> underTest.deleteUserChallenge(userChallenge))
            .isInstanceOf(DatabaseRepositoryException.class)
            .hasMessage(USER_CHALLENGE_DELETING_ERROR);
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).delete(userChallenge);
    }

    @Test
    void archiveModel() {
        getPreparedDataForArchiveRestoreModel(
            USER_ID, ROLE_ID, CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE,DURATION_ENTITY_ID, START_DATE,
            END_DATE, CHALLENGE_DURATION_ID, USER_EXIST, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE,
            USER_CHALLENGE_ID);
        when(dtoConverter.convertToDto(userChallenge, UserChallengeArch.class)).thenReturn(userChallengeArch);
        underTest.archiveModel(userChallenge);
        verify(archiveService, times(NUM_OF_METHOD_CALLS)).saveModel(userChallengeArch);
    }

    @Test
    void restoreModel() throws JsonProcessingException {
        getPreparedDataForArchiveRestoreModel(
            USER_ID, ROLE_ID, CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_IS_ACTIVE,DURATION_ENTITY_ID, START_DATE,
            END_DATE, CHALLENGE_DURATION_ID, USER_EXIST, USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE,
            USER_CHALLENGE_ID);
        when(objectMapper.readValue("string", UserChallengeArch.class)).thenReturn(userChallengeArch);
        when(dtoConverter.convertToEntity(userChallengeArch, new UserChallenge())).thenReturn(userChallenge);
        underTest.restoreModel("string");
        verify(userChallengeRepository, times(NUM_OF_METHOD_CALLS)).save(userChallenge);
    }
    public void getPreparedDataForGetUserChallengeForProfileByUserId(
        int numOfGeneratedElements, long firstId, long firstChallengeId, LocalDate registrationChallengeDate,
        LocalDate startChallengeDate, LocalDate endChallengeDate) {
        userChallengeForProfileGetList =
            getUserChallengeForProfileGetList(numOfGeneratedElements, firstId, firstChallengeId,
                registrationChallengeDate, startChallengeDate, endChallengeDate);
    }

    public void getPreparedDataForCreateUserChallengeByUser(
        long challengeDurationId, boolean userExist, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long userId) {
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeForUserCreateWithDate =
            getUserChallengeForUserCreateWithDate(userId, challengeId, startDate, endDate);
        userChallengeCreateResponse = getUserChallengeCreateResponse(challengeName, startDate, endDate);
    }

    public void getPreparedDataForGetUserChallengeByUserIdChallengeIdStartDateEndDate(
        long challengeDurationId, boolean userExist, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long userId,
        long userChallengeId, long roleId, long userChallengeStatusId,
        TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        user = getUser(userId, roleId);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
    }

    public void getPreparedDataForDeleteUserChallengeForProfile(
        long challengeDurationId, boolean userExist, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long userId,
        long userChallengeId, long roleId, long userChallengeStatusId,
        TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        user = getUser(userId, roleId);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
        userChallengeForProfileDelete = getUserChallengeForProfileDelete(userId, challengeId, startDate, endDate);
        userChallengeDeleteResponse = getUserChallengeDeleteResponse(challengeName, startDate, endDate);
    }

    public void getPreparedDataForGetListUserChallengeDurationByChallengeIdSuccessUserActive(
        int numOfGeneratedElements, long challengeId, String challengeName, boolean isChallengeActive,
        LocalDate startDate, LocalDate endDate) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        userChallengeForUserGetLocalDateList =
            getUserChallengeForUserGetLocalDateList(numOfGeneratedElements, startDate, endDate);
        userChallengeForUserGetStringList =
            getUserChallengeForUserGetStringList(numOfGeneratedElements, startDate, endDate);
    }

    public void getPreparedDataForGetListUserChallengeDurationByChallengeIdExceptionUserNonActive(
        long challengeId, String challengeName, boolean isChallengeActive) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
    }

    public void getPreparedDataForGetListUserChallengeDurationByChallengeIdExceptionEmpty(
        long challengeId, String challengeName, boolean isChallengeActive) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
    }

    public void getPreparedDataForCheckIfChallengeIsActiveTrue(
        long challengeId, String challengeName, boolean isChallengeActive) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
    }

    public void getPreparedDataForCheckIfChallengeIsActiveFalse(
        long challengeId, String challengeName, boolean isChallengeActive) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
    }

    public void getPreparedDataForGetListUserChallengeForAdmin(
        int numOfGeneratedElements, long challengeId, String challengeName, boolean isChallengeActive) {
        userChallengeForAdminGetList =
            getUserChallengeForAdminGetList(numOfGeneratedElements, challengeId, challengeName, isChallengeActive);
    }

    public void getPreparedDataForGetListChallengeDurationForAdminSuccess(
        int numOfGeneratedElements, long challengeId, long durationId, boolean userExist,
        LocalDate startDate, LocalDate endDate) {
        userChallengeForAdminGetChallengeDurationList =
            getUserChallengeForAdminGetChallengeDurationList(numOfGeneratedElements, challengeId, durationId, userExist,
                startDate, endDate);
    }

    public void getPreparedDataForGetListRegisteredUsersByChallengeIdChallengeDurationIdSuccess(
        int numOfGeneratedElements, long userChallengeId, long userId,
        LocalDate registrationChallengeDate, long challengeId, long durationId) {
        userChallengeForAdminRegisteredUserList = getUserChallengeForAdminRegisteredUserList(numOfGeneratedElements,
            userChallengeId, userId, registrationChallengeDate);
        userChallengeForAdminGetByChallengeIdDurationId =
            getUserChallengeForAdminGetByChallengeIdDurationId(challengeId, durationId);

    }

    public void getPreparedDataForGetListRegisteredUsersByChallengeIdChallengeDurationId(
        long challengeId, long durationId) {
        userChallengeForAdminGetByChallengeIdDurationId =
            getUserChallengeForAdminGetByChallengeIdDurationId(challengeId, durationId);
    }

    public void getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdSuccess(
        long challengeId, long durationId, int numOfGeneratedElements, long userId) {
        userChallengeForAdminGetByChallengeIdDurationId =
            getUserChallengeForAdminGetByChallengeIdDurationId(challengeId, durationId);
        userChallengeForAdminNotRegisteredUserList =
            getUserChallengeForAdminNotRegisteredUserList(numOfGeneratedElements, userId);
    }

    public void getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdIfEmptySuccess(
        long challengeId, long durationId, int numOfGeneratedElements, long userId) {
        userChallengeForAdminGetByChallengeIdDurationId =
            getUserChallengeForAdminGetByChallengeIdDurationId(challengeId, durationId);
        userChallengeForAdminNotRegisteredUserList =
            getUserChallengeForAdminNotRegisteredUserList(numOfGeneratedElements, userId);
    }

    public void getPreparedDataForGetListNotRegisteredUsersByChallengeIdChallengeDurationIdException(
        long challengeId, long durationId) {
        userChallengeForAdminGetByChallengeIdDurationId =
            getUserChallengeForAdminGetByChallengeIdDurationId(challengeId, durationId);
    }

    public void getPreparedDataForCreateUserChallengeFromAdmin(
        long challengeDurationId, boolean userExist, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long userId, long durationId) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeForUserCreate = getUserChallengeForUserCreate(userId, challengeId, durationId);
        userChallengeCreateResponse = getUserChallengeCreateResponse(challengeName, startDate, endDate);
    }

    public void getPreparedDataForCreateUserChallengeException(
        long challengeDurationId, boolean userExist, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, int numOfGeneratedElements,
        long userId) {
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeForExistList = getUserChallengeForExistList(numOfGeneratedElements, userId, challengeDurationId);
    }

    public void getPreparedDataForUpdateUserChallengeFromAdmin(
        long userId, long roleId, long userChallengeId, long challengeDurationId, boolean userExist,
        long userChallengeStatusId, TestUserChallengeStatusEnum testUserChallengeStatusEnum, long challengeId,
        String challengeName, boolean isChallengeActive, long durationEntityId, LocalDate startDate,
        LocalDate endDate, int numOfGeneratedElements) {
        user = getUser(userId, roleId);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
        userChallengeForAdminUpdate = getUserChallengeForAdminUpdate(userChallengeId, userId);
        userChallengeUpdateResponse =
            getUserChallengeUpdateResponse(userChallengeId, challengeName, startDate, endDate);
        userChallengeForExistList = getUserChallengeForExistList(numOfGeneratedElements, userId, challengeDurationId);
    }

    public void getPreparedDataForUpdateUserByIdAndUserChallengeData(
        long userId, long roleId, long userChallengeId) {
        user = getUser(userId, roleId);
        userChallengeForAdminUpdate = getUserChallengeForAdminUpdate(userChallengeId, userId);
    }

    public void getPreparedDataForGetListUserChallengeForExistSuccess(
        int numOfGeneratedElements, long userId, long challengeDurationId) {
        userChallengeForExistList = getUserChallengeForExistList(numOfGeneratedElements, userId, challengeDurationId);
    }

    public void getPreparedDataForIsUserChallengeExistByUserIdChallengeDurationIdTrue(
        int numOfGeneratedElements, long userId, long challengeDurationId) {
        userChallengeForExistList = getUserChallengeForExistList(numOfGeneratedElements, userId, challengeDurationId);
    }

    public void getPreparedDataForDeleteUserChallengeForAdmin(
        long userId, long roleId, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long challengeDurationId, boolean userExist,
        long userChallengeStatusId, TestUserChallengeStatusEnum testUserChallengeStatusEnum, long userChallengeId) {
        user = getUser(userId, roleId);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
        userChallengeForAdminDelete = getUserChallengeForAdminDelete(userId, challengeId, durationEntityId);
        userChallengeDeleteResponse = getUserChallengeDeleteResponse(challengeName, startDate, endDate);
    }

    public void getPreparedDataForDeleteUserChallengeException(
        long userId, long roleId, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long challengeDurationId, boolean userExist,
        long userChallengeStatusId, TestUserChallengeStatusEnum testUserChallengeStatusEnum, long userChallengeId) {
        user = getUser(userId, roleId);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
    }

    public void getPreparedDataForArchiveRestoreModel(
        long userId, long roleId, long challengeId, String challengeName, boolean isChallengeActive,
        long durationEntityId, LocalDate startDate, LocalDate endDate, long challengeDurationId, boolean userExist,
        long userChallengeStatusId, TestUserChallengeStatusEnum testUserChallengeStatusEnum, long userChallengeId) {
        user = getUser(userId, roleId);
        challenge = getChallenge(challengeId, challengeName, isChallengeActive);
        durationEntity = getDurationEntity(durationEntityId, startDate, endDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, durationEntity, challenge);
        userChallengeStatus = getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallenge = getUserChallenge(userChallengeId, user, challengeDuration, userChallengeStatus);
        userChallengeArch =
            getUserChallengeArch(userChallengeId, startDate, userId, challengeDurationId, userChallengeStatusId);
    }
}