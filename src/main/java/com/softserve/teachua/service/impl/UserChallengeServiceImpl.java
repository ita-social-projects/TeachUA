package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.model.archivable.UserChallengeArch;
import com.softserve.teachua.repository.UserChallengeRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeDurationService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.UserChallengeService;
import com.softserve.teachua.service.UserChallengeStatusService;
import com.softserve.teachua.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserChallengeServiceImpl implements UserChallengeService, ArchiveMark<UserChallenge> {
    private static final String USER_CHALLENGE_ALREADY_EXIST = "Ви вже зареєстровані на цьому челенджі - %s";
    private static final String USER_CHALLENGE_NOT_FOUND = "UserChallenge not found";
    private static final String CHALLENGE_NOT_FOUND = "NotFound Challenge with id - %s";
    private static final String DEFAULT_USER_CHALLENGE_STATUS = "ADDED";
    private static final String USER_CHALLENGE_DELETING_ERROR =
        "Can't delete UserChallenge cause of relationship";
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeDurationService challengeDurationService;
    private final UserChallengeStatusService userChallengeStatusService;
    private final ChallengeService challengeService;
    private final UserService userService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserChallengeServiceImpl(UserChallengeRepository userChallengeRepository,
                                    @Lazy ChallengeDurationService challengeDurationService,
                                    UserChallengeStatusService userChallengeStatusService,
                                    ChallengeService challengeService, UserService userService,
                                    DtoConverter dtoConverter, ArchiveService archiveService,
                                    ObjectMapper objectMapper) {
        this.userChallengeRepository = userChallengeRepository;
        this.challengeDurationService = challengeDurationService;
        this.userChallengeStatusService = userChallengeStatusService;
        this.challengeService = challengeService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<UserChallengeForProfileGet> getUserChallengeForProfileByUserId(Long id) {
        List<UserChallengeForProfileGet> userChallengeForProfileGetList =
            userChallengeRepository.getUserChallengeForProfileByUserId(id);
        if (userChallengeForProfileGetList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeList {}", userChallengeForProfileGetList);
        return userChallengeForProfileGetList;
    }

    @Override
    public UserChallengeCreateResponse createUserChallengeByUser(
        UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate) {
        Long userId = userChallengeForUserCreateWithDate.getUserId();
        Long challengeId = userChallengeForUserCreateWithDate.getChallengeId();

        ChallengeDuration challengeDuration = challengeDurationService.getChallengeDurationByChallengeIdAndStartEndDate(
            challengeId,
            userChallengeForUserCreateWithDate.getStartDate(),
            userChallengeForUserCreateWithDate.getEndDate());

        return createUserChallenge(userId, challengeDuration);
    }

    @Override
    public UserChallenge getUserChallengeByUserIdChallengeIdStartDateEndDate(
        Long userId, Long challengeId, LocalDate startDate, LocalDate endDate) {
        UserChallenge userChallenge = userChallengeRepository
            .getUserChallengeByUserIdChallengeIdStartDateEndDate(userId, challengeId, startDate, endDate);
        log.debug("getting UserChallenge {} by userId ={} challengeId ={} startDate ={} endDate ={}",
            userChallenge, userId, challengeId, startDate, endDate);
        return userChallenge;
    }

    @Override
    public UserChallengeDeleteResponse deleteUserChallengeForProfile(
        UserChallengeForProfileDelete userChallengeForProfileDelete) {
        UserChallenge userChallenge = getUserChallengeByUserIdChallengeIdStartDateEndDate(
            userChallengeForProfileDelete.getUserIdForDelete(), userChallengeForProfileDelete.getChallengeIdForDelete(),
            userChallengeForProfileDelete.getStartChallengeDate(), userChallengeForProfileDelete.getEndChallengeDate());

        return deleteUserChallenge(userChallenge);
    }

    @Override
    public List<UserChallengeForUserGetString> getListUserChallengeDurationByChallengeId(Long id) {
        boolean isChallengeActive = checkIfChallengeIsActive(id);
        if (!isChallengeActive) {
            throw new NotExistException(String.format(CHALLENGE_NOT_FOUND, id));
        }
        List<UserChallengeForUserGetLocalDate> userChallengeForUserGetLocalDateList =
            userChallengeRepository.getListUserChallengeDurationByChallengeId(id);
        if (userChallengeForUserGetLocalDateList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeForUserGetLocalDateList {}", userChallengeForUserGetLocalDateList);
        return filterNonActiveUserChallengeForUserGetString(userChallengeForUserGetLocalDateList);
    }

    @Override
    public List<UserChallengeForUserGetString> filterNonActiveUserChallengeForUserGetString(
        List<UserChallengeForUserGetLocalDate> userChallengeForUserGetLocalDateList) {
        List<UserChallengeForUserGetString> userChallengeForUserGetStringList = userChallengeForUserGetLocalDateList
            .stream()
            .filter(value -> checkIfDateAfterNowDate(value.getStartDate())
                && checkIfDateAfterNowDate(value.getEndDate()))
            .map(value -> UserChallengeForUserGetString
                .builder()
                .startDate(value.getStartDate().toString())
                .endDate(value.getEndDate().toString())
                .build())
            .collect(Collectors.toList());
        log.debug("filtering UserChallengeForUserGetString {} getting only active", userChallengeForUserGetStringList);
        return userChallengeForUserGetStringList;
    }

    @Override
    public boolean checkIfChallengeIsActive(Long challengeId) {
        boolean isChallengeActive = challengeService.getChallengeById(challengeId).getIsActive();
        log.debug("checking id Challenge with id ={} is active", challengeId);
        return isChallengeActive;
    }

    @Override
    public boolean checkIfDateAfterNowDate(LocalDate date) {
        LocalDate now = LocalDate.now();
        boolean isDateAfterNow = date.isAfter(now);
        log.debug("checking date ={} is after now ={}", date, now);
        return isDateAfterNow;
    }

    @Override
    public List<UserChallengeForAdminGet> getListUserChallengeForAdmin() {
        List<UserChallengeForAdminGet> userChallengeForAdminGetList =
            userChallengeRepository.getListUserChallengeForAdmin();
        if (userChallengeForAdminGetList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeForAdminGetList {}", userChallengeForAdminGetList);
        return userChallengeForAdminGetList;
    }

    @Override
    public List<UserChallengeForAdminGetChallengeDuration> getListChallengeDurationForAdmin(Long id) {
        List<UserChallengeForAdminGetChallengeDuration> userChallengeForAdminGetChallengeDurationList =
            userChallengeRepository.getListChallengeDurationForAdmin(id);
        if (userChallengeForAdminGetChallengeDurationList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeForAdminGetChallengeDurationList {}",
            userChallengeForAdminGetChallengeDurationList);
        return userChallengeForAdminGetChallengeDurationList;
    }

    @Override
    public List<UserChallengeForAdminRegisteredUser> getListRegisteredUsersByChallengeIdChallengeDurationId(
        UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId) {
        List<UserChallengeForAdminRegisteredUser> userChallengeForAdminRegisteredUserList =
            userChallengeRepository.getListRegisteredUsersByChallengeIdChallengeDurationId(
                userChallengeForAdminGetByChallengeIdDurationId.getChallengeId(),
                userChallengeForAdminGetByChallengeIdDurationId.getDurationId());
        if (userChallengeForAdminRegisteredUserList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeForAdminRegisteredUserList {}",
            userChallengeForAdminRegisteredUserList);
        return userChallengeForAdminRegisteredUserList;
    }

    @Override
    public List<UserChallengeForAdminNotRegisteredUser> getListNotRegisteredUsersByChallengeIdChallengeDurationId(
        UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId) {
        List<UserChallengeForAdminNotRegisteredUser> userChallengeForAdminNotRegisteredUserList =
            userChallengeRepository
                .getListNotRegisteredUsersByChallengeIdChallengeDurationId(
                    userChallengeForAdminGetByChallengeIdDurationId.getChallengeId(),
                    userChallengeForAdminGetByChallengeIdDurationId.getDurationId());

        if (userChallengeForAdminNotRegisteredUserList.isEmpty()) {
            userChallengeForAdminNotRegisteredUserList = userChallengeRepository.getListAllNotRegisteredUsers();

            if (userChallengeForAdminNotRegisteredUserList.isEmpty()) {
                throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
            }
            log.debug("getting list of userChallengeForAdminNotRegisteredUserList {}",
                userChallengeForAdminNotRegisteredUserList);
        }
        return userChallengeForAdminNotRegisteredUserList;
    }

    @Override
    public UserChallengeCreateResponse createUserChallengeFromAdmin(
        UserChallengeForUserCreate userChallengeForUserCreate) {
        Long userId = userChallengeForUserCreate.getUserId();
        Long challengeId = userChallengeForUserCreate.getChallengeId();
        Long durationId = userChallengeForUserCreate.getDurationId();

        ChallengeDuration challengeDuration =
            challengeDurationService.getChallengeDurationByChallengeIdAndDurationId(challengeId, durationId);

        return createUserChallenge(userId, challengeDuration);
    }


    @Override
    public UserChallengeCreateResponse createUserChallenge(Long userId, ChallengeDuration challengeDuration) {
        User user = userService.getUserById(userId);

        UserChallengeStatus userChallengeStatus = dtoConverter.convertToEntity(userChallengeStatusService
            .getUserChallengeStatusByStatusName(DEFAULT_USER_CHALLENGE_STATUS), new UserChallengeStatus());

        UserChallenge userChallenge = UserChallenge
            .builder()
            .registrationDate(LocalDate.now())
            .userChallengeStatus(userChallengeStatus)
            .user(user)
            .challengeDuration(challengeDuration)
            .build();
        boolean existsUserChallenge = isUserChallengeExistByUserIdChallengeDurationId(
            userId, challengeDuration.getId());
        if (existsUserChallenge) {
            throw new AlreadyExistException(
                String.format(USER_CHALLENGE_ALREADY_EXIST, challengeDuration.getChallenge().getName()));
        }
        userChallengeRepository.save(userChallenge);
        challengeDurationService.updateChallengeDurationUserExist(challengeDuration.getId(), true);
        UserChallengeCreateResponse userChallengeCreateResponse =
            UserChallengeCreateResponse
                .builder()
                .challengeName(userChallenge.getChallengeDuration().getChallenge().getName())
                .startDate(userChallenge.getChallengeDuration().getDurationEntity().getStartDate())
                .endDate(userChallenge.getChallengeDuration().getDurationEntity().getEndDate())
                .build();

        log.debug("**/adding new userChallenge = " + userChallenge);
        return userChallengeCreateResponse;
    }

    @Override
    public UserChallengeUpdateResponse updateUserChallengeFromAdmin(
        UserChallengeForAdminUpdate userChallengeForAdminUpdate) {
        UserChallenge userChallenge = userChallengeRepository.getUserChallengeById(
            userChallengeForAdminUpdate.getUserChallengeId());

        User newUser = updateUserByIdAndUserChallengeData(userChallenge.getUser().getId(), userChallengeForAdminUpdate);

        ChallengeDuration challengeDuration =
            challengeDurationService.getChallengeDurationById(userChallenge.getChallengeDuration().getId());

        UserChallengeStatus userChallengeStatus = dtoConverter.convertToEntity(
            userChallengeStatusService.getUserChallengeStatusByStatusName(
                userChallengeForAdminUpdate.getStatusName()), new UserChallengeStatus());

        UserChallenge newUserChallenge = new UserChallenge();
        newUserChallenge.setId(userChallengeForAdminUpdate.getUserChallengeId());
        newUserChallenge.setRegistrationDate(userChallenge.getRegistrationDate());
        newUserChallenge.setUser(newUser);
        newUserChallenge.setChallengeDuration(challengeDuration);
        newUserChallenge.setUserChallengeStatus(userChallengeStatus);

        UserChallengeUpdateResponse userChallengeCreateResponse =
            UserChallengeUpdateResponse
                .builder()
                .userChallengeId(newUserChallenge.getId())
                .challengeName(newUserChallenge.getChallengeDuration().getChallenge().getName())
                .startChallengeDate(newUserChallenge.getChallengeDuration().getDurationEntity().getStartDate())
                .endChallengeDate(newUserChallenge.getChallengeDuration().getDurationEntity().getEndDate())
                .build();

        log.debug("**/Updating userChallenge = {}", newUserChallenge);
        userChallengeRepository.save(newUserChallenge);
        return userChallengeCreateResponse;
    }

    @Override
    public User updateUserByIdAndUserChallengeData(
        Long userId, UserChallengeForAdminUpdate userChallengeForAdminUpdate) {
        User user = userService.getUserById(userId);
        log.debug("updating User ={}", user);
        user.setFirstName(userChallengeForAdminUpdate.getFirstName());
        user.setLastName(userChallengeForAdminUpdate.getLastName());
        user.setEmail(userChallengeForAdminUpdate.getEmail());
        user.setPhone(userChallengeForAdminUpdate.getPhone());

        userService.updateUser(user);
        log.debug("updating newUser ={} ", user);
        return user;
    }

    @Override
    public List<UserChallengeForExist> getListUserChallengeForExist() {
        List<UserChallengeForExist> userChallengeForExistList = userChallengeRepository.getListUserChallengeForExist();
        log.debug("getting list of userChallengeForExistList {}", userChallengeForExistList);
        return userChallengeForExistList;
    }

    @Override
    public boolean isUserChallengeExistByUserIdChallengeDurationId(Long userId, Long challengeDurationId) {
        boolean existsUserChallenge = getListUserChallengeForExist()
            .stream()
            .anyMatch(userChallenge -> (userId.equals(userChallenge.getUserId())
                && challengeDurationId.equals(userChallenge.getChallengeDurationId())));
        log.debug("checking UserChallenge existence by userId ={} challengeDurationId ={} and result is ={}",
            userId, challengeDurationId, existsUserChallenge);
        return existsUserChallenge;
    }

    @Override
    public UserChallengeDeleteResponse deleteUserChallengeForAdmin(
        UserChallengeForAdminDelete userChallengeForAdminDelete) {
        UserChallenge userChallenge = userChallengeRepository
            .getUserChallengeByUserIdAndChallengeDurationId(
                userChallengeForAdminDelete.getUserId(),
                userChallengeForAdminDelete.getChallengeId(),
                userChallengeForAdminDelete.getDurationId());
        return deleteUserChallenge(userChallenge);
    }

    @Override
    public UserChallengeDeleteResponse deleteUserChallenge(UserChallenge userChallenge) {
        UserChallengeDeleteResponse response;
        try {
            userChallengeRepository.delete(userChallenge);
            userChallengeRepository.flush();
            boolean checkForUpdateUserExist =
                challengeDurationService.existChallengeDurationRegisteredUsers(
                    userChallenge.getChallengeDuration().getId());
            if (!checkForUpdateUserExist) {
                challengeDurationService.updateChallengeDurationUserExist(userChallenge.getChallengeDuration().getId(),
                    false);
            }
        } catch (ValidationException e) {
            throw new DatabaseRepositoryException(USER_CHALLENGE_DELETING_ERROR);
        }
        archiveModel(userChallenge);
        response = UserChallengeDeleteResponse
            .builder()
            .challengeName(userChallenge.getChallengeDuration().getChallenge().getName())
            .startChallengeDate(userChallenge.getChallengeDuration().getDurationEntity().getStartDate())
            .endChallengeDate(userChallenge.getChallengeDuration().getDurationEntity().getEndDate())
            .build();
        log.debug("deleting UserChallenge {} was successfully deleted", userChallenge);
        return response;
    }

    @Override
    public void archiveModel(UserChallenge userChallenge) {
        UserChallengeArch userChallengeArch =
            dtoConverter.convertToDto(userChallenge, UserChallengeArch.class);
        archiveService.saveModel(userChallengeArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        UserChallengeArch userChallengeArch =
            objectMapper.readValue(archiveObject, UserChallengeArch.class);
        userChallengeRepository.save(dtoConverter.convertToEntity(userChallengeArch, UserChallenge.builder().build()));
    }
}
