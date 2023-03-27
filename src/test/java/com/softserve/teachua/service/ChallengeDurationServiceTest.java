package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.TestUserChallengeStatusEnum;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.repository.ChallengeDurationRepository;
import com.softserve.teachua.repository.UserChallengeRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.softserve.teachua.TestConstants.USER_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_IS_ACTIVE;
import static com.softserve.teachua.TestConstants.USER_EXIST;
import static com.softserve.teachua.TestConstants.ROLE_ID;
import static com.softserve.teachua.TestConstants.FIRST_TASK_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ACTIVE;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_FOR_ADMIN_NOT_FOUND;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_FOR_ADMIN_DURATION_STRING_NOT_FOUND;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_DELETING_ERROR;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_ALREADY_EXIST;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_DURATION_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ID;
import static com.softserve.teachua.TestConstants.CHALLENGE_ID;
import static com.softserve.teachua.TestConstants.DURATION_ID;
import static com.softserve.teachua.TestConstants.START_DATE;
import static com.softserve.teachua.TestConstants.END_DATE;
import static com.softserve.teachua.TestConstants.NUM_OF_METHOD_CALLS;
import static com.softserve.teachua.TestConstants.NUM_OF_GENERATED_ELEMENTS;
import static com.softserve.teachua.TestUtils.getChallenge;
import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getListChallengeDurationForAdmin;
import static com.softserve.teachua.TestUtils.getListChallengeDurationForAdminDurationLocalDate;
import static com.softserve.teachua.TestUtils.getListChallengeDurationForAdminDurationString;
import static com.softserve.teachua.TestUtils.getChallengeDurationSet;
import static com.softserve.teachua.TestUtils.getChallengeDurationAddSet;
import static com.softserve.teachua.TestUtils.getChallengeDuration;
import static com.softserve.teachua.TestUtils.getChallengeDurationExistUserResponseTrue;
import static com.softserve.teachua.TestUtils.getChallengeDurationDelete;
import static com.softserve.teachua.TestUtils.getChallengeDurationDeleteResponse;
import static com.softserve.teachua.TestUtils.getUserChallengeList;
import static com.softserve.teachua.TestUtils.getChallengeDurationArch;
import static com.softserve.teachua.TestUtils.getChallengeDurationForAdminDurationLocalDate;
import static com.softserve.teachua.TestUtils.getDurationEntitySet;
import static com.softserve.teachua.TestUtils.getDurationEntityResponseList;
import static com.softserve.teachua.TestUtils.getDurationEntity;
import static com.softserve.teachua.TestUtils.getUserChallengeStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationAdd;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDeleteResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationExistUserResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.archivable.ChallengeDurationArch;
import com.softserve.teachua.service.impl.ChallengeDurationServiceImpl;
import java.util.LinkedHashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ChallengeDurationServiceTest {
    @InjectMocks
    private ChallengeDurationServiceImpl underTest;
    @Mock
    private ChallengeService challengeService;
    @Mock
    private DurationEntityService durationEntityService;
    @Mock
    private UserChallengeRepository userChallengeRepository;
    @Mock
    private ChallengeDurationRepository challengeDurationRepository;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserChallengeService userChallengeService;
    private Challenge challenge;
    private ChallengeDuration challengeDuration;
    private DurationEntity durationEntity;
    private ChallengeDurationDelete challengeDurationDelete;
    private ChallengeDurationDeleteResponse challengeDurationDeleteResponse;
    private ChallengeDurationExistUserResponse challengeDurationExistUserResponse;
    private ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate;
    private ChallengeDurationArch challengeDurationArch;
    private List<ChallengeDurationForAdmin> challengeDurationForAdminList;
    private List<ChallengeDurationForAdminDurationLocalDate> challengeDurationForAdminDurationLocalDateList;
    private List<ChallengeDurationForAdminDurationString> challengeDurationForAdminDurationStringList;
    private List<ChallengeDuration> challengeDurationFromDB;
    private List<UserChallenge> userChallengeList;
    private List<DurationEntityResponse> durationEntityResponseList;
    private Set<ChallengeDuration> challengeDurationSet;
    private Set<DurationEntity> durationEntitySet;
    private Set<ChallengeDuration> challengeDurationSetFiltered;
    private Set<ChallengeDurationAdd> challengeDurationAddSet;

    @Test
    void existUser() {
        getPreparedDataForExistUser(START_DATE, END_DATE);
        when(challengeDurationRepository.existUser(START_DATE, END_DATE)).thenReturn(true);
        assertThat(underTest.existUser(challengeDurationForAdminDurationLocalDate)).isTrue();
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).existUser(START_DATE, END_DATE);
    }

    @Test
    void getListChallengeDurationForAdminSuccess() {
        getPreparedDataForGetListChallengeDurationForAdmin(NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID);
        when(challengeDurationRepository.getListChallengeDurationForAdmin())
            .thenReturn(challengeDurationForAdminList);
        assertThat(underTest.getListChallengeDurationForAdmin())
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(challengeDurationForAdminList);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).getListChallengeDurationForAdmin();
    }

    @Test
    void getListChallengeDurationForAdminException() {
        when(challengeDurationRepository.getListChallengeDurationForAdmin())
            .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListChallengeDurationForAdmin())
            .isInstanceOf(NotExistException.class)
            .hasMessage(CHALLENGE_DURATION_FOR_ADMIN_NOT_FOUND);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).getListChallengeDurationForAdmin();
    }

    @Test
    void getListDurationsSuccess() {
        getPreparedDataForGetListDurations(NUM_OF_GENERATED_ELEMENTS, START_DATE, END_DATE);
        when(challengeDurationRepository.getListDurations(CHALLENGE_ID))
            .thenReturn(challengeDurationForAdminDurationLocalDateList);
        assertThat(underTest.getListDurations(CHALLENGE_ID))
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(challengeDurationForAdminDurationStringList);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).getListDurations(CHALLENGE_ID);
    }

    @Test
    void getListDurationsException() {
        when(challengeDurationRepository.getListDurations(CHALLENGE_ID))
            .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getListDurations(CHALLENGE_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(String.format(CHALLENGE_DURATION_FOR_ADMIN_DURATION_STRING_NOT_FOUND, CHALLENGE_ID));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).getListDurations(CHALLENGE_ID);
    }

    @Test
    public void mapToChallengeDurationForAddingSuccess() {
        getPreparedDataForMapToChallengeDurationForAdding(
            NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,
            NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE);
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        assertThat(underTest.mapToChallengeDurationForAdding(CHALLENGE_ID, durationEntitySet))
            .isEqualTo(challengeDurationSet);
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    public void mapToChallengeDurationAdd() {
        getPreparedDataForMapToChallengeDurationAdd(
            NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,
            NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE);
        assertThat(underTest.mapToChallengeDurationAdd(challengeDurationSet)).isEqualTo(challengeDurationAddSet);
    }

    @Test
    public void filterChallengeDurationToAdd() {
        getPreparedDataForFilterChallengeDurationToAdd(
            NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,
            NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE);
        when(challengeDurationRepository.getChallengeDurationByChallengeId(CHALLENGE_ID))
            .thenReturn(challengeDurationFromDB);
        assertThat(underTest.filterChallengeDurationToAdd(CHALLENGE_ID, challengeDurationSet))
            .isEqualTo(challengeDurationSetFiltered);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).getChallengeDurationByChallengeId(CHALLENGE_ID);
    }

    @Test
    void createChallengeDurationSuccessWithChallengeIdDurationEntityListSuccess() {
        getPreparedDataForCreateChallengeDurationSuccessWithChallengeIdDurationEntityList(
            NUM_OF_GENERATED_ELEMENTS, CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,
            NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE);
        when(durationEntityService.createAllDurationEntityFromResponseList(durationEntityResponseList))
            .thenReturn(durationEntitySet);
        when(challengeService.getChallengeById(CHALLENGE_ID)).thenReturn(challenge);
        challengeDurationSet.forEach(duration -> when(challengeDurationRepository.save(duration))
            .thenReturn(duration));
        assertThat(underTest.createChallengeDuration(CHALLENGE_ID, durationEntityResponseList))
            .isEqualTo(challengeDurationAddSet);
        verify(durationEntityService, times(NUM_OF_METHOD_CALLS))
            .createAllDurationEntityFromResponseList(durationEntityResponseList);
        verify(challengeService, times(NUM_OF_METHOD_CALLS)).getChallengeById(CHALLENGE_ID);
    }

    @Test
    void createChallengeDurationSuccess() {
        getPreparedDataForCreateChallengeDuration(CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.save(challengeDuration)).thenReturn(challengeDuration);
        assertThat(underTest.createChallengeDuration(challengeDuration)).isEqualTo(challengeDuration);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).save(challengeDuration);
    }

    @Test
    void createChallengeDurationException() {
        getPreparedDataForCreateChallengeDuration(CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.existsChallengeDurations(challengeDuration)).thenReturn(true);
        assertThatThrownBy(() -> underTest.createChallengeDuration(challengeDuration))
            .isInstanceOf(AlreadyExistException.class)
            .hasMessage(String.format(CHALLENGE_DURATION_ALREADY_EXIST, challengeDuration));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).existsChallengeDurations(challengeDuration);
    }

    @Test
    void checkChallengeDurationExistsTest() {
        getPreparedDataForCheckChallengeDurationExists(CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID,NUM_OF_GENERATED_ELEMENTS, FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.existsChallengeDurations(challengeDuration)).thenReturn(true);
        assertThat(underTest.checkChallengeDurationExists(challengeDuration))
            .isEqualTo(challengeDurationExistUserResponse);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).existsChallengeDurations(challengeDuration);
    }

    @Test
    void deleteChallengeDurationWithChallengeDurationDeleteSuccess() {
        getPreparedDataForDeleteChallengeDurationWithChallengeDurationDelete(
            CHALLENGE_ID, START_DATE, END_DATE, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST,
            USER_CHALLENGE_STATUS_ID, USER_CHALLENGE_STATUS_ACTIVE, NUM_OF_GENERATED_ELEMENTS,
            USER_CHALLENGE_ID, START_DATE);
        when(userChallengeRepository.getListRegisteredByChallengeIdAndDates(
            CHALLENGE_ID, START_DATE, END_DATE)).thenReturn(userChallengeList);
        when(challengeDurationRepository.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_ID, START_DATE, END_DATE)).thenReturn(Optional.of(challengeDuration));
        assertThat(underTest.deleteChallengeDuration(challengeDurationDelete))
            .isEqualTo(challengeDurationDeleteResponse);
    }

    @Test
    void deleteChallengeDurationSuccess() {
        getPreparedDataForDeleteChallengeDuration(
            CHALLENGE_ID, START_DATE, END_DATE, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        assertThat(underTest.deleteChallengeDuration(challengeDuration))
            .isEqualTo(challengeDurationDeleteResponse);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).delete(challengeDuration);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).flush();
    }

    @Test
    void deleteChallengeDurationException() {
        getPreparedDataForDeleteChallengeDuration(
            CHALLENGE_ID, START_DATE, END_DATE, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        doThrow(new ValidationException()).when(challengeDurationRepository).delete(challengeDuration);
        assertThatThrownBy(() -> underTest.deleteChallengeDuration(challengeDuration))
            .isInstanceOf(DatabaseRepositoryException.class)
            .hasMessage(String.format(CHALLENGE_DURATION_DELETING_ERROR));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).delete(challengeDuration);
    }

    @Test
    void getChallengeDurationByChallengeIdAndStartEndDateSuccess() {
        getPreparedDataForGetChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_DURATION_ID, START_DATE, END_DATE)).thenReturn(Optional.of(challengeDuration));
        assertThat(underTest.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_DURATION_ID, START_DATE, END_DATE)).isNotNull().hasNoNullFieldsOrProperties()
            .isEqualTo(challengeDuration);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS))
            .getChallengeDurationByChallengeIdAndStartEndDate(
                CHALLENGE_DURATION_ID, START_DATE, END_DATE);
    }

    @Test
    void getChallengeDurationByChallengeIdAndStartEndDateException() {
        when(challengeDurationRepository.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_DURATION_ID, START_DATE, END_DATE)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getChallengeDurationByChallengeIdAndStartEndDate(
            CHALLENGE_DURATION_ID, START_DATE, END_DATE))
            .isInstanceOf(NotExistException.class)
            .hasMessage(CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES,
                CHALLENGE_DURATION_ID, START_DATE, END_DATE);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS))
            .getChallengeDurationByChallengeIdAndStartEndDate(
                CHALLENGE_DURATION_ID, START_DATE, END_DATE);
    }

    @Test
    void updateChallengeDurationUserExist() {
        getPreparedDataForUpdateChallengeDurationUserExist(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.findById(CHALLENGE_DURATION_ID))
            .thenReturn(Optional.ofNullable(challengeDuration));
        assertDoesNotThrow(() -> underTest.updateChallengeDurationUserExist(CHALLENGE_DURATION_ID, USER_EXIST));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).findById(CHALLENGE_DURATION_ID);
    }

    @Test
    void getChallengeDurationByIdSuccess() {
        getPreparedDataForGetChallengeDurationById(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.findById(CHALLENGE_DURATION_ID))
            .thenReturn(Optional.of(challengeDuration));
        assertThat(underTest.getChallengeDurationById(CHALLENGE_DURATION_ID)).isNotNull().isEqualTo(challengeDuration);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).findById(CHALLENGE_DURATION_ID);
    }

    @Test
    void getChallengeDurationByIdException() {
        when(challengeDurationRepository.findById(CHALLENGE_DURATION_ID))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getChallengeDurationById(CHALLENGE_DURATION_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(String.format(CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID, CHALLENGE_DURATION_ID));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).findById(CHALLENGE_DURATION_ID);
    }

    @Test
    void getChallengeDurationByChallengeIdAndDurationIdSuccess() {
        getPreparedDataForGetChallengeDurationByChallengeIdAndDurationId(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(challengeDurationRepository.getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID))
            .thenReturn(Optional.of(challengeDuration));
        assertThat(underTest.getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID))
            .isNotNull().isEqualTo(challengeDuration);
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS))
            .getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID);
    }

    @Test
    void getChallengeDurationByChallengeIdAndDurationIdException() {
        when(challengeDurationRepository.getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(String.format(
                CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID, CHALLENGE_ID, DURATION_ID));
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS))
            .getChallengeDurationByChallengeIdAndDurationId(CHALLENGE_ID, DURATION_ID);
    }

    @Test
    void existChallengeDurationRegisteredUsersSuccessTrue() {
        when(challengeDurationRepository.existChallengeDurationRegisteredUsers(CHALLENGE_DURATION_ID))
            .thenReturn(true);
        assertThat(underTest.existChallengeDurationRegisteredUsers(CHALLENGE_DURATION_ID)).isTrue();
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).existChallengeDurationRegisteredUsers(
            CHALLENGE_DURATION_ID);
    }

    @Test
    void existChallengeDurationRegisteredUsersSuccessFalse() {
        when(challengeDurationRepository.existChallengeDurationRegisteredUsers(CHALLENGE_DURATION_ID))
            .thenReturn(false);
        assertThat(underTest.existChallengeDurationRegisteredUsers(CHALLENGE_DURATION_ID)).isFalse();
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).existChallengeDurationRegisteredUsers(
            CHALLENGE_DURATION_ID);
    }

    @Test
    void archiveModel() {
        getPreparedDataForArchiveRestoreModel(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(dtoConverter.convertToDto(challengeDuration, ChallengeDurationArch.class))
            .thenReturn(challengeDurationArch);
        underTest.archiveModel(challengeDuration);
        verify(archiveService, times(NUM_OF_METHOD_CALLS)).saveModel(challengeDurationArch);
    }

    @Test
    void restoreModel() throws JsonProcessingException {
        getPreparedDataForArchiveRestoreModel(
            CHALLENGE_ID, USER_ID, CHALLENGE_IS_ACTIVE, ROLE_ID, NUM_OF_GENERATED_ELEMENTS,
            FIRST_TASK_ID, START_DATE, DURATION_ID, START_DATE, END_DATE, CHALLENGE_DURATION_ID, USER_EXIST);
        when(objectMapper.readValue("string", ChallengeDurationArch.class))
            .thenReturn(challengeDurationArch);
        when(dtoConverter.convertToEntity(challengeDurationArch, ChallengeDuration.builder().build()))
            .thenReturn(challengeDuration);
        underTest.restoreModel("string");
        verify(challengeDurationRepository, times(NUM_OF_METHOD_CALLS)).save(challengeDuration);
    }

    public void getPreparedDataForExistUser(LocalDate startDate, LocalDate endDate) {
        challengeDurationForAdminDurationLocalDate =
            getChallengeDurationForAdminDurationLocalDate(startDate, endDate);
    }

    public void getPreparedDataForGetListChallengeDurationForAdmin(int numOfGeneratedElements, long firstId) {
        challengeDurationForAdminList =
            getListChallengeDurationForAdmin(numOfGeneratedElements, firstId);
    }

    public void getPreparedDataForGetListDurations(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        challengeDurationForAdminDurationLocalDateList =
            getListChallengeDurationForAdminDurationLocalDate(numOfGeneratedElements, firstStartDate, firstEndDate);
        challengeDurationForAdminDurationStringList =
            getListChallengeDurationForAdminDurationString(numOfGeneratedElements, firstStartDate, firstEndDate);
    }

    public void getPreparedDataForMapToChallengeDurationAdd(
        int numOfGeneratedElements, long challengeId, long userId, boolean isActive,
        long roleId, int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        challengeDurationSet = getChallengeDurationSet(
            numOfGeneratedElements, false, challengeId, userId, isActive, roleId, numOfTasks,
            firstTaskId, firstTaskStartDate, durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDurationAddSet = getChallengeDurationAddSet(numOfGeneratedElements, firstDurationStartDate, firstDurationEndDate);
    }
    public void getPreparedDataForFilterChallengeDurationToAdd(
        int numOfGeneratedElements, long challengeId, long userId, boolean isActive,
        long roleId, int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        challengeDurationSet = getChallengeDurationSet(
            numOfGeneratedElements, false, challengeId, userId, isActive, roleId, numOfTasks,
            firstTaskId, firstTaskStartDate, durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDurationFromDB = challengeDurationSet.stream()
            .filter(cd -> cd.getDurationEntity().getId() <= challengeDurationSet.size() / 2)
            .collect(Collectors.toList());
        challengeDurationSetFiltered = challengeDurationSet.stream()
            .filter(cd -> cd.getDurationEntity().getId() > challengeDurationSet.size() / 2)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public void getPreparedDataForMapToChallengeDurationForAdding(
        int numOfGeneratedElements, long challengeId, long userId, boolean isActive,
        long roleId, int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntitySet = getDurationEntitySet(numOfGeneratedElements, durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDurationSet = getChallengeDurationSet(
            numOfGeneratedElements, false, challengeId, userId, isActive, roleId, numOfTasks,
            firstTaskId, firstTaskStartDate, durationId, firstDurationStartDate, firstDurationEndDate);
    }

    public void getPreparedDataForCreateChallengeDurationSuccessWithChallengeIdDurationEntityList(
        int numOfGeneratedElements, long challengeId, long userId, boolean isActive,
        long roleId, int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        durationEntityResponseList = getDurationEntityResponseList(
            numOfGeneratedElements, firstDurationStartDate, firstDurationEndDate);
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntitySet = getDurationEntitySet(numOfGeneratedElements, durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDurationSet = getChallengeDurationSet(
            numOfGeneratedElements, false, challengeId, userId, isActive, roleId, numOfTasks,
            firstTaskId, firstTaskStartDate, durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDurationAddSet = getChallengeDurationAddSet(numOfGeneratedElements, firstDurationStartDate, firstDurationEndDate);
    }

    public void getPreparedDataForCreateChallengeDuration(
        long challengeId, long userId, boolean isActive, long roleId, int numOfTasks, long firstTaskId,
        LocalDate firstTaskStartDate, long durationId, LocalDate firstDurationStartDate, LocalDate firstDurationEndDate,
        long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
    }

    public void getPreparedDataForCheckChallengeDurationExists(
        long challengeId, long userId, boolean isActive, long roleId, int numOfTasks, long firstTaskId,
        LocalDate firstTaskStartDate, long durationId, LocalDate firstDurationStartDate, LocalDate firstDurationEndDate,
        long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
        challengeDurationExistUserResponse = getChallengeDurationExistUserResponseTrue();
    }

    public void getPreparedDataForDeleteChallengeDurationWithChallengeDurationDelete(
        long challengeId, LocalDate startDate, LocalDate endDate, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist,
        long userChallengeStatusId, TestUserChallengeStatusEnum testUserChallengeStatusEnum, int numOfGeneratedElements, long userChallengeId, LocalDate registrationDate) {
        challengeDurationDelete = getChallengeDurationDelete(challengeId, startDate, endDate);
        challengeDurationDeleteResponse = getChallengeDurationDeleteResponse(startDate, endDate);
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
        User user = getUser(userId, roleId);
        UserChallengeStatus userChallengeStatus =
            getUserChallengeStatus(userChallengeStatusId, testUserChallengeStatusEnum);
        userChallengeList = getUserChallengeList(numOfGeneratedElements, userChallengeId, registrationDate, user,
            challengeDuration, userChallengeStatus);
    }

    public void getPreparedDataForDeleteChallengeDuration(
        long challengeId, LocalDate startDate, LocalDate endDate, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
        challengeDurationDeleteResponse = getChallengeDurationDeleteResponse(startDate, endDate);
    }

    public void getPreparedDataForGetChallengeDurationByChallengeIdAndStartEndDate(
        long challengeId, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
    }

    public void getPreparedDataForUpdateChallengeDurationUserExist(
        long challengeId, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
    }

    public void getPreparedDataForGetChallengeDurationById(
        long challengeId, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
    }

    public void getPreparedDataForGetChallengeDurationByChallengeIdAndDurationId(
        long challengeId, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
    }

    public void getPreparedDataForArchiveRestoreModel(
        long challengeId, long userId, boolean isActive, long roleId,
        int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate, long challengeDurationId, boolean userExist) {
        challenge = getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId, firstTaskStartDate);
        durationEntity = getDurationEntity(durationId, firstDurationStartDate, firstDurationEndDate);
        challengeDuration = getChallengeDuration(challengeDurationId, userExist, challenge, durationEntity);
        challengeDurationArch = getChallengeDurationArch(challengeDurationId, userExist, challengeId, durationId);
    }
}