package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.TestUserChallengeStatusEnum;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.model.archivable.UserChallengeStatusArch;
import com.softserve.teachua.repository.UserChallengeStatusRepository;
import com.softserve.teachua.service.impl.UserChallengeStatusServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ID;
import static com.softserve.teachua.TestConstants.NUM_OF_METHOD_CALLS;
import static com.softserve.teachua.TestConstants.ADDED;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_NOT_FOUND_STATUSES;
import static com.softserve.teachua.TestConstants.EXISTING_STATUS_NAME;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_DELETING_ERROR;
import static com.softserve.teachua.TestConstants.USER_CHALLENGE_STATUS_ALREADY_EXIST;
import static com.softserve.teachua.TestUtils.getUserChallengeStatus;
import static com.softserve.teachua.TestUtils.getUserChallengeStatusGet;
import static com.softserve.teachua.TestUtils.getUserChallengeStatusAdd;
import static com.softserve.teachua.TestUtils.getUserChallengeStatusUpdate;
import static com.softserve.teachua.TestUtils.getUserChallengeStatusDelete;
import static com.softserve.teachua.TestUtils.getUserChallengeStatusArch;
import static com.softserve.teachua.TestUtils.getAllUserChallengeStatus;
import static com.softserve.teachua.TestUtils.getAllUserChallengeStatusForOptions;
import static com.softserve.teachua.TestConstants.NUM_OF_GENERATED_ELEMENTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserChallengeStatusServiceTest {

    @InjectMocks
    private UserChallengeStatusServiceImpl underTest;
    @Mock
    private UserChallengeStatusRepository userChallengeStatusRepository;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ObjectMapper objectMapper;
    private UserChallengeStatus userChallengeStatus;
    private UserChallengeStatusGet userChallengeStatusGet;
    private UserChallengeStatusAdd userChallengeStatusAdd;
    private List<UserChallengeStatusGet> userChallengeStatusGetList;
    private UserChallengeStatusDelete userChallengeStatusDelete;
    private UserChallengeStatusArch userChallengeStatusArch;
    private UserChallengeStatusUpdate userChallengeStatusUpdate;
    private List<UserChallengeStatusForOption> userChallengeStatusForOptionList;

    @Test
    void getUserChallengeStatusByIdSuccess() {
        getPreparedDataForGetUserChallengeStatusById(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.of(userChallengeStatus));
        assertThat(underTest.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .isNotNull()
            .isEqualTo(userChallengeStatus);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void getUserChallengeStatusByIdException() {
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID, USER_CHALLENGE_STATUS_ID);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void getUserChallengeStatusByStatusNameSuccess() {
        getPreparedDataForGetUserChallengeStatusByStatusName(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusByStatusName(ADDED.name()))
            .thenReturn(Optional.of(userChallengeStatus));
        when(dtoConverter.convertToDto(userChallengeStatus, UserChallengeStatusGet.class))
            .thenReturn(userChallengeStatusGet);
        assertThat(underTest.getUserChallengeStatusByStatusName(EXISTING_STATUS_NAME))
            .isNotNull()
            .isEqualTo(userChallengeStatusGet);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusByStatusName(EXISTING_STATUS_NAME);
    }

    @Test
    void getUserChallengeStatusByStatusNameException() {
        when(userChallengeStatusRepository.getUserChallengeStatusByStatusName(EXISTING_STATUS_NAME))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getUserChallengeStatusByStatusName(EXISTING_STATUS_NAME))
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME, EXISTING_STATUS_NAME);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusByStatusName(EXISTING_STATUS_NAME);
    }

    @Test
    void getAllUserChallengeStatusSuccess() {
        getPreparedDataForGetAllUserChallengeStatus(NUM_OF_GENERATED_ELEMENTS, USER_CHALLENGE_STATUS_ID);
        when(userChallengeStatusRepository.getAllUserChallengeStatus())
            .thenReturn(userChallengeStatusGetList);
        assertThat(underTest.getAllUserChallengeStatus())
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeStatusGetList);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).getAllUserChallengeStatus();
    }

    @Test
    void getAllUserChallengeStatusException() {
        when(userChallengeStatusRepository.getAllUserChallengeStatus()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> underTest.getAllUserChallengeStatus())
            .isInstanceOf(NotExistException.class)
            .hasMessage(USER_CHALLENGE_STATUS_NOT_FOUND_STATUSES);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).getAllUserChallengeStatus();
    }

    @Test
    void getAllUserChallengeStatusForOptionsSuccess() {
        getPreparedDataForGetAllUserChallengeStatusForOptions(NUM_OF_GENERATED_ELEMENTS, USER_CHALLENGE_STATUS_ID);
        when(userChallengeStatusRepository.getAllUserChallengeStatus())
            .thenReturn(userChallengeStatusGetList);
        assertThat(underTest.getAllUserChallengeStatusForOptions())
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(userChallengeStatusForOptionList);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).getAllUserChallengeStatus();
    }

    @Test
    void isUserChallengeStatusExistsByIdTrue() {
        when(userChallengeStatusRepository.existsById(USER_CHALLENGE_STATUS_ID)).thenReturn(true);
        assertThat(underTest.isUserChallengeStatusExistsById(USER_CHALLENGE_STATUS_ID).isUserExist()).isTrue();
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).existsById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void isUserChallengeStatusExistsByIdFalse() {
        when(userChallengeStatusRepository.existsById(USER_CHALLENGE_STATUS_ID)).thenReturn(false);
        assertThat(underTest.isUserChallengeStatusExistsById(USER_CHALLENGE_STATUS_ID).isUserExist()).isFalse();
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).existsById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void isUserChallengeStatusExistsByNameTrue() {
        when(userChallengeStatusRepository.existsByStatusName(EXISTING_STATUS_NAME)).thenReturn(true);
        assertThat(underTest.isUserChallengeStatusExistsByName(EXISTING_STATUS_NAME).isUserExist()).isTrue();
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).existsByStatusName(EXISTING_STATUS_NAME);
    }

    @Test
    void isUserChallengeStatusExistsByNameFalse() {
        when(userChallengeStatusRepository.existsByStatusName(EXISTING_STATUS_NAME)).thenReturn(false);
        assertThat(underTest.isUserChallengeStatusExistsByName(EXISTING_STATUS_NAME).isUserExist()).isFalse();
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).existsByStatusName(EXISTING_STATUS_NAME);
    }

    @Test
    void mapUserChallengeStatusGetToOptions() {
        getPreparedDataForMapUserChallengeStatusGetToOptions(NUM_OF_GENERATED_ELEMENTS, USER_CHALLENGE_STATUS_ID);
        assertThat(underTest.mapUserChallengeStatusGetToOptions(userChallengeStatusGetList))
            .isEqualTo(userChallengeStatusForOptionList);
    }

    @Test
    void addUserChallengeStatusSuccess() {
        getPreparedDataForAddUserChallengeStatus(USER_CHALLENGE_STATUS_ID, ADDED);
        when(dtoConverter.convertToEntity(userChallengeStatusAdd, new UserChallengeStatus()))
            .thenReturn(userChallengeStatus);
        when(userChallengeStatusRepository.save(userChallengeStatus)).thenReturn(userChallengeStatus);
        assertThat(underTest.addUserChallengeStatus(userChallengeStatusAdd)).isEqualTo(userChallengeStatusAdd);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).save(userChallengeStatus);
    }

    @Test
    void addUserChallengeStatusException() {
        getPreparedDataForAddUserChallengeStatus(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.existsByStatusName(ADDED.name())).thenReturn(true);
        assertThatThrownBy(() -> underTest.addUserChallengeStatus(userChallengeStatusAdd))
            .isInstanceOf(AlreadyExistException.class)
            .hasMessage(String.format(USER_CHALLENGE_STATUS_ALREADY_EXIST, userChallengeStatusAdd.getStatusName()));
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).existsByStatusName(ADDED.name());
    }

    @Test
    void updateUserChallengeStatusSuccess() {
        getPreparedDataForUpdateUserChallengeStatus(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.of(userChallengeStatus));
        when(userChallengeStatusRepository.save(userChallengeStatus)).thenReturn(userChallengeStatus);
        when(dtoConverter.convertToDto(userChallengeStatus,
            UserChallengeStatusUpdate.class)).thenReturn(userChallengeStatusUpdate);
        assertThat(underTest.updateUserChallengeStatus(userChallengeStatusUpdate)).isEqualTo(userChallengeStatusUpdate);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).save(userChallengeStatus);
    }

    @Test
    void updateUserChallengeStatusException() {
        getPreparedDataForUpdateUserChallengeStatus(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.updateUserChallengeStatus(userChallengeStatusUpdate))
            .isInstanceOf(NotExistException.class);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void deleteUserChallengeStatusByIdSuccess() {
        getPreparedDataForDeleteUserChallengeStatusById(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.of(userChallengeStatus));
        assertThat(underTest.deleteUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .isEqualTo(userChallengeStatusDelete);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).deleteById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void deleteUserChallengeStatusByIdException() {
        getPreparedDataForDeleteUserChallengeStatusById(USER_CHALLENGE_STATUS_ID, ADDED);
        when(userChallengeStatusRepository.getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .thenReturn(Optional.ofNullable(userChallengeStatus));
        doThrow(new ValidationException()).when(userChallengeStatusRepository).deleteById(USER_CHALLENGE_STATUS_ID);
        assertThatThrownBy(() -> underTest.deleteUserChallengeStatusById(USER_CHALLENGE_STATUS_ID))
            .isInstanceOf(DatabaseRepositoryException.class)
            .hasMessage(USER_CHALLENGE_STATUS_DELETING_ERROR);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS))
            .getUserChallengeStatusById(USER_CHALLENGE_STATUS_ID);
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).deleteById(USER_CHALLENGE_STATUS_ID);
    }

    @Test
    void archiveModel() {
        getPreparedDataForArchiveRestoreModel(USER_CHALLENGE_STATUS_ID, ADDED);
        when(dtoConverter.convertToDto(userChallengeStatus, UserChallengeStatusArch.class))
            .thenReturn(userChallengeStatusArch);
        underTest.archiveModel(userChallengeStatus);
        verify(archiveService, times(NUM_OF_METHOD_CALLS)).saveModel(userChallengeStatusArch);
    }

    @Test
    void restoreModel() throws JsonProcessingException {
        getPreparedDataForArchiveRestoreModel(USER_CHALLENGE_STATUS_ID, ADDED);
        when(objectMapper.readValue("string", UserChallengeStatusArch.class))
            .thenReturn(userChallengeStatusArch);
        when(dtoConverter.convertToEntity(userChallengeStatusArch, UserChallengeStatus.builder().build()))
            .thenReturn(userChallengeStatus);
        underTest.restoreModel("string");
        verify(userChallengeStatusRepository, times(NUM_OF_METHOD_CALLS)).save(userChallengeStatus);
    }

    public void getPreparedDataForGetUserChallengeStatusById(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
    }

    public void getPreparedDataForGetUserChallengeStatusByStatusName(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
        userChallengeStatusGet = getUserChallengeStatusGet(id, testUserChallengeStatusEnum);
    }

    public void getPreparedDataForGetAllUserChallengeStatus(
        int numOfGeneratedElements, long firstId) {
        userChallengeStatusGetList = getAllUserChallengeStatus(numOfGeneratedElements, firstId);
    }

    public void getPreparedDataForGetAllUserChallengeStatusForOptions(
        int numOfGeneratedElements, long firstId) {
        userChallengeStatusGetList = getAllUserChallengeStatus(numOfGeneratedElements, firstId);
        userChallengeStatusForOptionList = getAllUserChallengeStatusForOptions(userChallengeStatusGetList);
    }

    public void getPreparedDataForMapUserChallengeStatusGetToOptions(
        int numOfGeneratedElements, long firstId) {
        userChallengeStatusGetList = getAllUserChallengeStatus(numOfGeneratedElements, firstId);
        userChallengeStatusForOptionList = getAllUserChallengeStatusForOptions(userChallengeStatusGetList);
    }

    public void getPreparedDataForAddUserChallengeStatus(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
        userChallengeStatusAdd = getUserChallengeStatusAdd(testUserChallengeStatusEnum);
    }

    public void getPreparedDataForUpdateUserChallengeStatus(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
        userChallengeStatusUpdate = getUserChallengeStatusUpdate(id, testUserChallengeStatusEnum);
    }

    public void getPreparedDataForDeleteUserChallengeStatusById(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
        userChallengeStatusDelete = getUserChallengeStatusDelete(id, testUserChallengeStatusEnum);
    }

    public void getPreparedDataForArchiveRestoreModel(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        userChallengeStatus = getUserChallengeStatus(id, testUserChallengeStatusEnum);
        userChallengeStatusArch = getUserChallengeStatusArch(id, testUserChallengeStatusEnum);
    }
}