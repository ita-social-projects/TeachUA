package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.UserChallengeStatusService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public List<UserChallengeStatusGet> getAllUserChallengeStatus() {
        List<UserChallengeStatusGet> userChallengeStatusGetList =
                userChallengeStatusRepository.getAllUserChallengeStatus();
        if (userChallengeStatusGetList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_STATUS_NOT_FOUND_STATUSES);
        }
        log.debug("**/getting all UserChallengeStatusGet ={}",userChallengeStatusGetList);
        return userChallengeStatusGetList;
    }

    @Override
    public List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions() {
        log.debug("**/getting all UserChallengeStatusForOption");
        return getAllUserChallengeStatus()
                .stream()
                .map(status -> new UserChallengeStatusForOption(status.getStatusName(), status.getStatusName()))
                .collect(Collectors.toList());
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
    public UserChallengeStatusAdd addUserChallengeStatus(UserChallengeStatusAdd userChallengeStatusAdd) {
        if (isUserChallengeStatusExistsByName(userChallengeStatusAdd.getStatusName())) {
            throw new AlreadyExistException(
                    String.format(USER_CHALLENGE_STATUS_ALREADY_EXIST, userChallengeStatusAdd.getStatusName()));
        }
        UserChallengeStatus userChallengeStatus = userChallengeStatusRepository.save(
                dtoConverter.convertToEntity(userChallengeStatusAdd, new UserChallengeStatus()));
        log.debug("**/adding new userChallengeStatus = {}", userChallengeStatus);
        return dtoConverter.convertToDto(userChallengeStatus, UserChallengeStatusAdd.class);
    }

    @Override
    public UserChallengeStatusUpdate updateUserChallengeStatus(UserChallengeStatusUpdate userChallengeStatusUpdate) {
        UserChallengeStatus newUserChallengeStatus = getUserChallengeStatusById(userChallengeStatusUpdate.getId());
        newUserChallengeStatus.setStatusName(userChallengeStatusUpdate.getStatusName());
        log.debug("**/updating UserChallengeStatus by newUserChallengeStatus = {}", newUserChallengeStatus);
        return dtoConverter.convertToDto(userChallengeStatusRepository.save(newUserChallengeStatus),
                UserChallengeStatusUpdate.class);
    }

    public UserChallengeStatus getUserChallengeStatusById(Long id) {
        UserChallengeStatus userChallengeStatus = userChallengeStatusRepository
                .getUserChallengeStatusById(id)
                .orElseThrow(() -> new NotExistException(String.format(USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID, id)));
        log.debug("**/getting UserChallengeStatus by id = {}", id);
        return userChallengeStatus;
    }

    @Override
    public UserChallengeStatusDelete deleteUserChallengeStatusById(Long id) {
        UserChallengeStatus userChallengeStatus = getUserChallengeStatusById(id);
        try {
            userChallengeStatusRepository.deleteById(id);
            userChallengeStatusRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(USER_CHALLENGE_STATUS_DELETING_ERROR);
        }
        archiveModel(userChallengeStatus);
        log.debug("userChallengeStatus {} was successfully deleted", userChallengeStatus);
        return dtoConverter.convertToDto(userChallengeStatus, UserChallengeStatusDelete.class);
    }

    @Override
    public boolean isUserChallengeStatusExistsById(Long id) {
        boolean existsUserChallengeStatus = userChallengeStatusRepository.existsById(id);
        log.debug("checking existence UserChallengeStatus by userChallengeStatusId ={} result ={}",
            id, existsUserChallengeStatus);
        return existsUserChallengeStatus;
    }

    private boolean isUserChallengeStatusExistsByName(String name) {
        boolean existsUserChallengeStatus = userChallengeStatusRepository.existsByStatusName(name);
        log.debug("checking existence UserChallengeStatus by statusName ={} result ={}",
            name, existsUserChallengeStatus);
        return existsUserChallengeStatus;
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
