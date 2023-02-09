package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
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
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.repository.ChallengeDurationRepository;
import com.softserve.teachua.repository.UserChallengeRepository;
import com.softserve.teachua.repository.UserChallengeStatusRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.UserChallengeService;
import com.softserve.teachua.service.UserChallengeStatusService;
import com.softserve.teachua.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserChallengeServiceImpl implements UserChallengeService {
    private final UserChallengeStatusRepository userChallengeStatusRepository;
    private final ChallengeDurationRepository challengeDurationRepository;
    private static final String USER_CHALLENGE_ALREADY_EXIST = "Ви вже зареєстровані на цьому челенджі - %s";
    private static final String USER_CHALLENGE_NOT_FOUND = "UserChallenge not found";
    private static final String CHALLENGE_NOT_FOUND = "NotFound Challenge with id - %s";
    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES =
            "ChallengeDuration not found by challengeId: %s startDate: %s endDate: %s";
    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID =
            "ChallengeDuration not found by challengeId: %s durationId: %s";
    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID =
            "ChallengeDuration not found by challengeDurationId: %s";
    private static final String DEFAULT_USER_CHALLENGE_STATUS = "ADDED";
    private static final String USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME =
            "UserChallengeStatus not found by statusName: %s";

    private final UserChallengeRepository userChallengeRepository;

    private final UserService userService;

    private final ChallengeService challengeService;

    private final UserChallengeStatusService userChallengeStatusService;

    private final DtoConverter dtoConverter;

    private final ArchiveService archiveService;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserChallengeServiceImpl(UserChallengeRepository userChallengeRepository,
                                    UserService userService,
                                    ChallengeService challengeService,
                                    UserChallengeStatusService userChallengeStatusService,
                                    DtoConverter dtoConverter,
                                    ArchiveService archiveService,
                                    ObjectMapper objectMapper,
                                    ChallengeDurationRepository challengeDurationRepository,
                                    UserChallengeStatusRepository userChallengeStatusRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.userService = userService;
        this.challengeService = challengeService;
        this.userChallengeStatusService = userChallengeStatusService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
        this.challengeDurationRepository = challengeDurationRepository;
        this.userChallengeStatusRepository = userChallengeStatusRepository;
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
    public String createUserChallengeByUser(UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate) {
        Long userId = userChallengeForUserCreateWithDate.getUserId();
        Long challengeId = userChallengeForUserCreateWithDate.getChallengeId();

        ChallengeDuration challengeDuration = challengeDurationRepository
                .getChallengeDurationByChallengeIdAndStartEndDate(
                        challengeId,
                        userChallengeForUserCreateWithDate.getStartDate(),
                        userChallengeForUserCreateWithDate.getEndDate())
                .orElseThrow(() -> new NotExistException(
                        String.format(CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES,
                                challengeId,
                                userChallengeForUserCreateWithDate.getStartDate(),
                                userChallengeForUserCreateWithDate.getEndDate())));

        return createUserChallenge(userId, challengeDuration);
    }

    @Override
    public String deleteUserChallengeForProfile(UserChallengeForProfileDelete userChallengeForProfileDelete) {
        UserChallenge userChallenge = userChallengeRepository
                .getUserChallengeByUserIdChallengeIdStartDateEndDate(userChallengeForProfileDelete.getUserIdForDelete(),
                        userChallengeForProfileDelete.getChallengeIdForDelete(),
                        userChallengeForProfileDelete.getStartChallengeDate(),
                        userChallengeForProfileDelete.getEndChallengeDate());

        return deleteUserChallenge(userChallenge);
    }

    @Override
    public List<UserChallengeForUserGetString> getListUserChallengeDurationByChallengeId(Long id) {
        if (!checkIfChallengeIsActive(id)) {
            throw new NotExistException(String.format(CHALLENGE_NOT_FOUND, id));
        }
        List<UserChallengeForUserGetLocalDate> userChallengeForUserGetLocalDateList =
                userChallengeRepository.getListUserChallengeDurationByChallengeId(id);
        if (userChallengeForUserGetLocalDateList.isEmpty()) {
            throw new NotExistException(USER_CHALLENGE_NOT_FOUND);
        }
        log.debug("getting list of userChallengeForUserGetLocalDateList {}", userChallengeForUserGetLocalDateList);
        return userChallengeForUserGetLocalDateList
                .stream()
                .filter(value -> checkIfDateBigerNowDate(value.getStartDate())
                    && checkIfDateBigerNowDate(value.getEndDate()))
                .map(value -> new UserChallengeForUserGetString(value.getStartDate().toString(),
                        value.getEndDate().toString()))
                .collect(Collectors.toList());
    }

    boolean checkIfChallengeIsActive(Long challengeId) {
        return challengeService.getChallengeById(challengeId).getIsActive();
    }

    boolean checkIfDateBigerNowDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
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
    public String createUserChallengeFromAdmin(UserChallengeForUserCreate userChallengeForUserCreate) {
        Long userId = userChallengeForUserCreate.getUserId();
        Long challengeId = userChallengeForUserCreate.getChallengeId();
        Long durationId = userChallengeForUserCreate.getDurationId();

        ChallengeDuration challengeDuration = challengeDurationRepository
                .getChallengeDurationByChallengeIdAndDurationId(challengeId, durationId)
                .orElseThrow(() -> new NotExistException(String.format(
                        CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID,
                        challengeId, durationId)));

        return createUserChallenge(userId, challengeDuration);
    }

    public String createUserChallenge(Long userId, ChallengeDuration challengeDuration) {
        User user = userService.getUserById(userId);

        UserChallengeStatus userChallengeStatus = dtoConverter
                .convertToEntity(userChallengeStatusService
                        .getUserChallengeStatusByStatusName(DEFAULT_USER_CHALLENGE_STATUS), new UserChallengeStatus());

        UserChallenge userChallenge = UserChallenge
                .builder()
                .registrationDate(LocalDate.now())
                .userChallengeStatus(userChallengeStatus)
                .user(user)
                .challengeDuration(challengeDuration)
                .build();

        if (isUserChallengeExistByUserIdChallengeId(userId, challengeDuration.getId())) {
            throw new AlreadyExistException(
                    String.format(USER_CHALLENGE_ALREADY_EXIST, challengeDuration.getChallenge().getName()));
        }

        log.debug("**/adding new userChallenge = " + userChallenge);

        userChallengeRepository.save(userChallenge);
        updateChallengeDurationUserExist(challengeDuration.getId(), true);
        return challengeDuration.getChallenge().getName();
    }

    @Override
    public Long updateUserChallengeFromAdmin(UserChallengeForAdminUpdate userChallengeForAdminUpdate) {
        UserChallenge userChallenge =
                userChallengeRepository.getUserChallengeById(userChallengeForAdminUpdate.getUserChallengeId());

        User user = userService.getUserById(userChallenge.getUser().getId());
        user.setFirstName(userChallengeForAdminUpdate.getFirstName());
        user.setLastName(userChallengeForAdminUpdate.getLastName());
        user.setEmail(userChallengeForAdminUpdate.getEmail());
        user.setPhone(userChallengeForAdminUpdate.getPhone());

        userService.updateUser(user);

        User newUser = userService.getUserById(userChallenge.getUser().getId());

        ChallengeDuration challengeDuration =
                challengeDurationRepository.getChallengeDurationById(userChallenge.getChallengeDuration().getId())
                        .orElseThrow(() -> new NotExistException(String.format(
                                CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID,
                                userChallenge.getChallengeDuration().getId())));

        UserChallengeStatus userChallengeStatus = userChallengeStatusRepository
                .getUserChallengeStatusByStatusName(userChallengeForAdminUpdate.getStatusName())
                .orElseThrow(() -> new NotExistException(String.format(USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME,
                        userChallengeForAdminUpdate.getStatusName())));

        UserChallenge newUserChallenge = new UserChallenge();
        newUserChallenge.setId(userChallengeForAdminUpdate.getUserChallengeId());
        newUserChallenge.setRegistrationDate(userChallenge.getRegistrationDate());
        newUserChallenge.setUser(newUser);
        newUserChallenge.setChallengeDuration(challengeDuration);
        newUserChallenge.setUserChallengeStatus(userChallengeStatus);

        log.debug("**/Updating userChallenge = " + newUserChallenge);

        userChallengeRepository.save(newUserChallenge);

        return newUserChallenge.getId();
    }

    public List<UserChallengeForExist> getListUserChallengeForExist() {
        List<UserChallengeForExist> userChallengeForExistList = userChallengeRepository.getListUserChallengeForExist();
        log.debug("getting list of userChallengeForExistList {}", userChallengeForExistList);
        return userChallengeForExistList;
    }

    public Boolean isUserChallengeExistByUserIdChallengeId(Long userId, Long challengeDurationId) {
        return getListUserChallengeForExist()
                .stream()
                .anyMatch(userChallenge -> (userId.equals(userChallenge.getUserId())
                    && challengeDurationId.equals(userChallenge.getChallengeDurationId())));
    }

    @Override
    public String deleteUserChallengeForAdmin(UserChallengeForAdminDelete userChallengeForAdminDelete) {
        UserChallenge userChallenge = userChallengeRepository
                .getUserChallengeByUserIdAndChallengeDurationId(userChallengeForAdminDelete.getUserId(),
                        userChallengeForAdminDelete.getChallengeId(),
                        userChallengeForAdminDelete.getDurationId());
        return deleteUserChallenge(userChallenge);
    }

    public String deleteUserChallenge(UserChallenge userChallenge) {
        if (Objects.isNull(userChallenge)) {
            return "Вже відписані від челенджу";
        } else {
            log.debug("userChallenge {} was successfully deleted", userChallenge);
            userChallengeRepository.delete(userChallenge);
            boolean checkForUpdateUserExist =
                    existChallengeDurationRegisteredUsers(userChallenge.getChallengeDuration().getId());
            if (!checkForUpdateUserExist) {
                updateChallengeDurationUserExist(userChallenge.getChallengeDuration().getId(), false);
            }
            return "Видалено";
        }
    }

    public void updateChallengeDurationUserExist(Long challengeDurationId, Boolean userExist) {
        ChallengeDuration challengeDuration = challengeDurationRepository.findById(challengeDurationId)
                .orElseThrow(() -> new NotExistException(String.format(
                        CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID, challengeDurationId)));

        challengeDuration.setUserExist(userExist);
        log.debug("**/Updating challengeDuration = " + challengeDuration);

        challengeDurationRepository.save(challengeDuration);
    }

    public boolean existChallengeDurationRegisteredUsers(Long challengeDurationId) {
        return userChallengeRepository.existChallengeDurationRegisteredUsers(challengeDurationId);
    }
}
