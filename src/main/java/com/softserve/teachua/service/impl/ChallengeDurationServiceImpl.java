package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationAdd;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDeleteResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationExistUserResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.archivable.ChallengeDurationArch;
import com.softserve.teachua.repository.ChallengeDurationRepository;
import com.softserve.teachua.repository.UserChallengeRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeDurationService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.DurationEntityService;
import com.softserve.teachua.service.UserChallengeService;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ChallengeDurationServiceImpl implements ChallengeDurationService, ArchiveMark<ChallengeDuration> {
    private static final String CHALLENGE_DURATION_FOR_ADMIN_NOT_FOUND =
        "ChallengeDurationForAdmin not found";
    private static final String CHALLENGE_DURATION_FOR_ADMIN_DURATION_STRING_NOT_FOUND =
        "ChallengeDurationForAdminDurationString not found by challengeId: %s";
    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES =
        "ChallengeDuration not found by challengeId: %s startDate: %s endDate: %s";
    private static final String CHALLENGE_DURATION_DELETING_ERROR =
        "Can't delete ChallengeDuration cause of relationship";
    private static final boolean DEFAULT_USER_EXIST_BOOLEAN = false;
    private static final String CHALLENGE_DURATION_ALREADY_EXIST =
        "Already exist ChallengeDuration %s";

    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID =
        "ChallengeDuration not found by challengeId: %s durationId: %s";
    private static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID =
        "ChallengeDuration not found by challengeDurationId: %s";
    private final ChallengeDurationRepository challengeDurationRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengeService userChallengeService;
    private final DurationEntityService durationEntityService;
    private final ChallengeService challengeService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    public ChallengeDurationServiceImpl(ChallengeDurationRepository challengeDurationRepository,
                                        UserChallengeService userChallengeService, ChallengeService challengeService,
                                        DtoConverter dtoConverter,
                                        UserChallengeRepository userChallengeRepository,
                                        DurationEntityService durationEntityService, ArchiveService archiveService,
                                        ObjectMapper objectMapper) {
        this.challengeDurationRepository = challengeDurationRepository;
        this.userChallengeService = userChallengeService;
        this.challengeService = challengeService;
        this.dtoConverter = dtoConverter;
        this.userChallengeRepository = userChallengeRepository;
        this.durationEntityService = durationEntityService;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Boolean existUser(ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate) {
        boolean existUser = challengeDurationRepository.existUser(
            challengeDurationForAdminDurationLocalDate.getStartDate(),
            challengeDurationForAdminDurationLocalDate.getEndDate());
        log.debug("**/checking User existence result ={}", existUser);
        return existUser;
    }

    @Override
    public List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin() {
        List<ChallengeDurationForAdmin> challengeDurationForAdminList =
            challengeDurationRepository.getListChallengeDurationForAdmin();
        if (challengeDurationForAdminList.isEmpty()) {
            throw new NotExistException(CHALLENGE_DURATION_FOR_ADMIN_NOT_FOUND);
        }
        log.debug("**/getting ChallengeDurationForAdmin");
        return challengeDurationForAdminList;
    }

    @Override
    public List<ChallengeDurationForAdminDurationString> getListDurations(Long id) {
        List<ChallengeDurationForAdminDurationLocalDate> challengeDurationForAdminDurationLocalDateList =
            challengeDurationRepository.getListDurations(id);
        if (challengeDurationForAdminDurationLocalDateList.isEmpty()) {
            throw new NotExistException(String.format(CHALLENGE_DURATION_FOR_ADMIN_DURATION_STRING_NOT_FOUND, id));
        }
        log.debug("**/getting challengeDurationForAdminDurationLocalDateList");
        return challengeDurationForAdminDurationLocalDateList
            .stream()
            .map(value -> new ChallengeDurationForAdminDurationString(value.getStartDate().toString(),
                value.getEndDate().toString()))
            .collect(Collectors.toList());
    }

    public Set<ChallengeDuration> mapToChallengeDurationForAdding(
        Long challengeId, Set<DurationEntity> durationEntitySet) {
        Challenge challenge = challengeService.getChallengeById(challengeId);
        Set<ChallengeDuration> resultChallengeDurationSet =
            durationEntitySet.stream().map(duration -> ChallengeDuration
                    .builder()
                    .id(0L)
                    .userExist(DEFAULT_USER_EXIST_BOOLEAN)
                    .challenge(challenge)
                    .durationEntity(duration)
                    .build())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/mapping resultChallengeDurationSet = {}", resultChallengeDurationSet);
        return resultChallengeDurationSet;
    }

    @Override
    public Set<ChallengeDurationAdd> createChallengeDuration(Long challengeId,
                                                             List<DurationEntityResponse> durationEntityResponseList) {
        Set<DurationEntity> durationEntitySetFromResponse = durationEntityService
            .createAllDurationEntityFromResponseList(durationEntityResponseList);

        Set<ChallengeDuration> mappedChallengeDuration =
            mapToChallengeDurationForAdding(challengeId, durationEntitySetFromResponse);

        Set<ChallengeDuration> challengeDurationsForAdding =
            filterChallengeDurationToAdd(challengeId, mappedChallengeDuration);

        Set<ChallengeDurationAdd> resultChallengeDurationAddSet = createChallengeDuration(challengeDurationsForAdding);
        log.debug("**/creating all resultChallengeDurationAddSet = {} from durationEntityResponseList = {}",
            resultChallengeDurationAddSet, durationEntityResponseList);
        return resultChallengeDurationAddSet;
    }

    public Set<ChallengeDurationAdd> createChallengeDuration(Set<ChallengeDuration> challengeDurationSetReceived) {
        Set<ChallengeDuration> challengeDurationSet =
            challengeDurationSetReceived
                .stream()
                .map(this::createChallengeDuration)
                .collect(Collectors.toSet());
        Set<ChallengeDurationAdd> resultChallengeDurationAddSet = mapToChallengeDurationAdd(challengeDurationSet);
        log.debug("**/creating resultChallengeDurationAddSet = {}", resultChallengeDurationAddSet);
        return resultChallengeDurationAddSet;
    }

    @Override
    public ChallengeDuration createChallengeDuration(ChallengeDuration challengeDuration) {
        boolean existsChallengeDuration = checkChallengeDurationExists(challengeDuration).isUserExists();
        if (existsChallengeDuration) {
            throw new AlreadyExistException(String.format(CHALLENGE_DURATION_ALREADY_EXIST, challengeDuration));
        }
        ChallengeDuration challengeDurationAdded = challengeDurationRepository.save(challengeDuration);
        log.debug("**/adding new ChallengeDuration = {}", challengeDurationAdded);
        return challengeDurationAdded;
    }

    public Set<ChallengeDurationAdd> mapToChallengeDurationAdd(Set<ChallengeDuration> challengeDurationSet) {
        Set<ChallengeDurationAdd> resultChallengeDurationAddSet = challengeDurationSet
            .stream()
            .map(challengeDuration ->
                ChallengeDurationAdd
                    .builder()
                    .startDate(challengeDuration.getDurationEntity().getStartDate())
                    .endDate(challengeDuration.getDurationEntity().getEndDate())
                    .build())
            .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/mapping resultChallengeDurationAddSet = {}", resultChallengeDurationAddSet);
        return resultChallengeDurationAddSet;
    }

    public Set<ChallengeDuration> filterChallengeDurationToAdd(
        Long challengeId, Set<ChallengeDuration> challengeDurationSet) {
        List<ChallengeDuration> challengeDurationFromDB =
            challengeDurationRepository.getChallengeDurationByChallengeId(challengeId);
        Set<ChallengeDuration> resultChallengeDurationSet = challengeDurationSet
            .stream()
            .filter(challengeDuration -> challengeDurationFromDB
                .stream()
                .noneMatch(fromDB -> fromDB.getChallenge().getId().equals(challengeId)
                    && fromDB.getDurationEntity().getStartDate()
                    .equals(challengeDuration.getDurationEntity().getStartDate())
                    && fromDB.getDurationEntity().getEndDate()
                    .equals(challengeDuration.getDurationEntity().getEndDate())))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/filtering challengeDurationSet= {} for new ChallengeDuration", challengeDurationSet);
        return resultChallengeDurationSet;
    }

    @Override
    public ChallengeDurationExistUserResponse checkChallengeDurationExists(ChallengeDuration challengeDuration) {
        ChallengeDurationExistUserResponse existsChallengeDuration =
            ChallengeDurationExistUserResponse
                .builder()
                .userExists(challengeDurationRepository.existsChallengeDurations(challengeDuration))
                .build();
        log.debug("**/checking existence ChallengeDuration = {} result ={}",
            challengeDuration, existsChallengeDuration);
        return existsChallengeDuration;
    }

    @Override
    public ChallengeDurationDeleteResponse deleteChallengeDuration(ChallengeDurationDelete challengeDurationDelete) {
        ChallengeDuration challengeDuration = getChallengeDurationByChallengeIdAndStartEndDate(
            challengeDurationDelete.getChallengeId(),
            challengeDurationDelete.getStartDate(),
            challengeDurationDelete.getEndDate());

        List<UserChallenge> userChallengeList = userChallengeRepository.getListRegisteredByChallengeIdAndDates(
            challengeDurationDelete.getChallengeId(),
            challengeDurationDelete.getStartDate(),
            challengeDurationDelete.getEndDate());

        userChallengeList.forEach(userChallengeService::deleteUserChallenge);
        return deleteChallengeDuration(challengeDuration);
    }

    @Override
    public ChallengeDurationDeleteResponse deleteChallengeDuration(ChallengeDuration challengeDuration) {
        try {
            challengeDurationRepository.delete(challengeDuration);
            challengeDurationRepository.flush();
        } catch (ValidationException e) {
            throw new DatabaseRepositoryException(CHALLENGE_DURATION_DELETING_ERROR);
        }
        archiveModel(challengeDuration);
        ChallengeDurationDeleteResponse challengeDurationDeleteResponse =
            ChallengeDurationDeleteResponse
                .builder()
                .startDate(challengeDuration.getDurationEntity().getStartDate())
                .endDate(challengeDuration.getDurationEntity().getEndDate())
                .build();
        log.debug("challengeDuration {} was successfully deleted", challengeDuration);
        return challengeDurationDeleteResponse;
    }

    @Override
    public ChallengeDuration getChallengeDurationByChallengeIdAndStartEndDate(
        Long challengeId, LocalDate startDate, LocalDate endDate) {
        ChallengeDuration challengeDuration = challengeDurationRepository
            .getChallengeDurationByChallengeIdAndStartEndDate(
                challengeId, startDate, endDate)
            .orElseThrow(() -> new NotExistException(
                String.format(CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES,
                    challengeId, startDate, endDate)));
        log.debug("getting ChallengeDuration {} by challengeId ={} startDate ={} endDate ={}",
            challengeDuration, challengeId, startDate, endDate);
        return challengeDuration;
    }

    @Override
    public ChallengeDuration getChallengeDurationById(Long challengeDurationId) {
        ChallengeDuration challengeDuration = challengeDurationRepository.findById(challengeDurationId)
            .orElseThrow(() -> new NotExistException(String.format(
                CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID, challengeDurationId)));
        log.debug("getting ChallengeDuration ={} by challengeDurationId ={}",
            challengeDuration, challengeDurationId);
        return challengeDuration;
    }

    @Override
    public ChallengeDuration getChallengeDurationByChallengeIdAndDurationId(Long challengeId, Long durationId) {
        ChallengeDuration challengeDuration = challengeDurationRepository
            .getChallengeDurationByChallengeIdAndDurationId(challengeId, durationId)
            .orElseThrow(() -> new NotExistException(String.format(
                CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID,
                challengeId, durationId)));
        log.debug("getting ChallengeDuration ={} by challengeId ={} durationId ={}",
            challengeDuration, challengeId, durationId);
        return challengeDuration;
    }

    @Override
    public void updateChallengeDurationUserExist(Long challengeDurationId, Boolean userExist) {
        ChallengeDuration challengeDuration = getChallengeDurationById(challengeDurationId);
        challengeDuration.setUserExist(userExist);
        challengeDurationRepository.save(challengeDuration);
        log.debug("**/Updating challengeDuration = " + challengeDuration);
    }

    @Override
    public boolean existChallengeDurationRegisteredUsers(Long challengeDurationId) {
        boolean existsRegisteredUsers = challengeDurationRepository
            .existChallengeDurationRegisteredUsers(challengeDurationId);
        log.debug("checking existence registeredUsers in ChallengeDuration by challengeId ={}",
            challengeDurationId);
        return existsRegisteredUsers;
    }

    @Override
    public void archiveModel(ChallengeDuration challengeDuration) {
        ChallengeDurationArch challengeDurationArch =
            dtoConverter.convertToDto(challengeDuration, ChallengeDurationArch.class);
        archiveService.saveModel(challengeDurationArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ChallengeDurationArch challengeDurationArch =
            objectMapper.readValue(archiveObject, ChallengeDurationArch.class);
        challengeDurationRepository.save(dtoConverter.convertToEntity(
            challengeDurationArch, ChallengeDuration.builder().build()));
    }
}
