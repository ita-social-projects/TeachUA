package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusExist;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.model.archivable.UserChallengeStatusArch;
import com.softserve.teachua.repository.UserChallengeStatusRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.UserChallengeStatusService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserChallengeStatusServiceImpl implements UserChallengeStatusService, ArchiveMark<UserChallengeStatus> {
    private static final String USER_CHALLENGE_STATUS_ALREADY_EXIST =
        "UserChallengeStatus already exist with statusName: %s";
    private static final String USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID = "UserChallengeStatus not found by id: %s";
    private static final String USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME =
        "UserChallengeStatus not found by statusName: %s";
    private static final String USER_CHALLENGE_STATUS_NOT_FOUND_STATUSES = "UserChallengeStatus not found";
    private static final String USER_CHALLENGE_STATUS_DELETING_ERROR =
        "Can't delete userChallengeStatus cause of relationship";
    private final UserChallengeStatusRepository userChallengeStatusRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserChallengeStatusServiceImpl(UserChallengeStatusRepository userChallengeStatusRepository,
                                          ArchiveService archiveService, DtoConverter dtoConverter,
                                          ObjectMapper objectMapper) {
        this.userChallengeStatusRepository = userChallengeStatusRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserChallengeStatus getUserChallengeStatusById(Long id) {
        UserChallengeStatus userChallengeStatus = userChallengeStatusRepository
            .getUserChallengeStatusById(id)
            .orElseThrow(() -> new NotExistException(String.format(USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID, id)));
        log.debug("**/getting UserChallengeStatus by id = {}", id);
        return userChallengeStatus;
    }

    @Override
    public UserChallengeStatusGet getUserChallengeStatusByStatusName(String statusName) {
        UserChallengeStatusGet userChallengeStatusGet = dtoConverter.convertToDto(userChallengeStatusRepository
                .getUserChallengeStatusByStatusName(statusName)
                .orElseThrow(() -> new NotExistException(
                    String.format(USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME, statusName))),
            UserChallengeStatusGet.class);
        log.debug("**/getting UserChallengeStatusGet by statusName = {}", statusName);
        return userChallengeStatusGet;
    }

    @Override
    public List<UserChallengeStatusGet> getAllUserChallengeStatus() {
        List<UserChallengeStatusGet> userChallengeStatusGetList =
            userChallengeStatusRepository.getAllUserChallengeStatus();
        log.debug("**/getting all UserChallengeStatusGet ={}", userChallengeStatusGetList);
        return userChallengeStatusGetList;
    }

    @Override
    public List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions() {
        List<UserChallengeStatusGet> userChallengeStatusGetList = getAllUserChallengeStatus();
        List<UserChallengeStatusForOption> userChallengeStatusForOptionList =
            mapUserChallengeStatusGetToOptions(userChallengeStatusGetList);
        log.debug("**/getting all UserChallengeStatusForOption");
        return userChallengeStatusForOptionList;
    }

    @Override
    public UserChallengeStatusExist isUserChallengeStatusExistsById(Long id) {
        boolean existsUserChallengeStatus = userChallengeStatusRepository.existsById(id);
        UserChallengeStatusExist userChallengeStatusExist =
            UserChallengeStatusExist.builder().userExist(existsUserChallengeStatus).build();
        log.debug("checking existence UserChallengeStatus by userChallengeStatusId ={} result ={}",
            id, userChallengeStatusExist);
        return userChallengeStatusExist;
    }

    @Override
    public UserChallengeStatusExist isUserChallengeStatusExistsByName(String name) {
        boolean existsUserChallengeStatus = userChallengeStatusRepository.existsByStatusName(name);
        UserChallengeStatusExist userChallengeStatusExist =
            UserChallengeStatusExist.builder().userExist(existsUserChallengeStatus).build();
        log.debug("checking existence UserChallengeStatus by statusName ={} result ={}",
            name, userChallengeStatusExist);
        return userChallengeStatusExist;
    }

    @Override
    public List<UserChallengeStatusForOption> mapUserChallengeStatusGetToOptions(
        List<UserChallengeStatusGet> userChallengeStatusGetList) {
        List<UserChallengeStatusForOption> userChallengeStatusForOptionList =
            userChallengeStatusGetList
            .stream()
            .map(status -> new UserChallengeStatusForOption(status.getStatusName(), status.getStatusName()))
            .collect(Collectors.toList());
        log.debug("**/mapping UserChallengeStatusGet ={} to UserChallengeStatusForOption ={}",
            userChallengeStatusGetList, userChallengeStatusForOptionList);
        return userChallengeStatusForOptionList;
    }

    @Override
    public UserChallengeStatusAdd addUserChallengeStatus(UserChallengeStatusAdd userChallengeStatusAdd) {
        boolean isUserStatusExist = isUserChallengeStatusExistsByName(
            userChallengeStatusAdd.getStatusName()).isUserExist();
        if (isUserStatusExist) {
            throw new AlreadyExistException(
                String.format(USER_CHALLENGE_STATUS_ALREADY_EXIST, userChallengeStatusAdd.getStatusName()));
        }
        UserChallengeStatus userChallengeStatus = userChallengeStatusRepository.save(
            dtoConverter.convertToEntity(userChallengeStatusAdd, new UserChallengeStatus()));
        UserChallengeStatusAdd userChallengeStatusAddResponse = UserChallengeStatusAdd.builder().statusName(
            userChallengeStatusAdd.getStatusName()).build();
        log.debug("**/adding new userChallengeStatus = {}", userChallengeStatus);
        return userChallengeStatusAddResponse;
    }

    @Override
    public UserChallengeStatusUpdate updateUserChallengeStatus(UserChallengeStatusUpdate userChallengeStatusUpdate) {
        UserChallengeStatus newUserChallengeStatus = getUserChallengeStatusById(userChallengeStatusUpdate.getId());
        newUserChallengeStatus.setStatusName(userChallengeStatusUpdate.getStatusName());
        log.debug("**/updating UserChallengeStatus by newUserChallengeStatus = {}", newUserChallengeStatus);
        return dtoConverter.convertToDto(userChallengeStatusRepository.save(newUserChallengeStatus),
            UserChallengeStatusUpdate.class);
    }

    @Override
    public UserChallengeStatusDelete deleteUserChallengeStatusById(Long id) {
        UserChallengeStatus userChallengeStatus = getUserChallengeStatusById(id);
        try {
            userChallengeStatusRepository.deleteById(id);
            userChallengeStatusRepository.flush();
        } catch (ValidationException e) {
            throw new DatabaseRepositoryException(USER_CHALLENGE_STATUS_DELETING_ERROR);
        }
        archiveModel(userChallengeStatus);
        UserChallengeStatusDelete userChallengeStatusDelete =
            UserChallengeStatusDelete.builder().id(userChallengeStatus.getId()).statusName(
            userChallengeStatus.getStatusName()).build();
        log.debug("userChallengeStatus {} was successfully deleted", userChallengeStatus);
        return userChallengeStatusDelete;
    }

    @Override
    public void archiveModel(UserChallengeStatus userChallengeStatus) {
        UserChallengeStatusArch userChallengeStatusArch =
            dtoConverter.convertToDto(userChallengeStatus, UserChallengeStatusArch.class);
        archiveService.saveModel(userChallengeStatusArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        UserChallengeStatusArch userChallengeStatusArch =
            objectMapper.readValue(archiveObject, UserChallengeStatusArch.class);
        userChallengeStatusRepository.save(
            dtoConverter.convertToEntity(userChallengeStatusArch, UserChallengeStatus.builder().build()));
    }
}
