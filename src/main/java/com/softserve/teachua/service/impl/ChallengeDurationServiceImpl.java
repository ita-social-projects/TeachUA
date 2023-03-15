package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

    public String createChallengeDuration(Long challengeId, List<DurationEntityResponse> durationEntityResponseList) {
        Challenge challenge = challengeService.getChallengeById(challengeId);

        durationEntityService.createAllDurationEntityFromResponseList(durationEntityResponseList);

        Set<DurationEntityResponse> durationEntityResponseSet = filterDurationEntityToAdd(
            challengeId, durationEntityResponseList);

        Set<DurationEntity> durationEntitySet = filterDurationEntityFromResponse(durationEntityResponseSet);

        durationEntitySet.stream()
            .map(durationEntity -> new ChallengeDuration(
                0L,
                DEFAULT_USER_EXIST_BOOLEAN,
                challenge,
                durationEntity))
            .forEach(this::createChallengeDuration);
        return "Успішно";
    }

    public void createChallengeDuration(ChallengeDuration challengeDuration) {
        boolean existsChallengeDuration = checkChallengeDurationExists(challengeDuration);
        if (existsChallengeDuration) {
            throw new AlreadyExistException(String.format(CHALLENGE_DURATION_ALREADY_EXIST, challengeDuration));
        }
        ChallengeDuration challengeDurationAdded = challengeDurationRepository.save(challengeDuration);
        log.debug("**/adding new ChallengeDuration = {}", challengeDurationAdded);
    }

    public Set<DurationEntity> filterDurationEntityFromResponse(Set<DurationEntityResponse> durationEntityResponseSet) {
        List<DurationEntity> durationEntityList = durationEntityService.getListDurationEntity();
        Set<DurationEntity> durationEntitySet = durationEntityList.stream()
            .filter(durationEntity -> durationEntityResponseSet
                .stream()
                .allMatch(response -> durationEntity.getStartDate().equals(response.getStartDate())
                    && durationEntity.getEndDate().equals(response.getEndDate())))
            .collect(Collectors.toSet());
        log.debug("**/filtering durationEntitySet= {} for new ChallengeDuration", durationEntitySet);
        return durationEntitySet;
    }

    public Set<DurationEntityResponse> filterDurationEntityToAdd(
        Long challengeId, List<DurationEntityResponse> durationEntityResponseList) {
        List<ChallengeDuration> challengeDurationFromDBList =
            challengeDurationRepository.getChallengeDurationByChallengeId(challengeId);
        Set<DurationEntityResponse> durationEntityResponseSet = durationEntityResponseList
            .stream()
            .filter(duration -> challengeDurationFromDBList
                .stream()
                .noneMatch(challengeDuration -> challengeDuration.getChallenge().getId().equals(challengeId)
                    && challengeDuration.getDurationEntity().getStartDate().equals(duration.getStartDate())
                    && challengeDuration.getDurationEntity().getEndDate().equals(duration.getEndDate())))
            .collect(Collectors.toSet());
        log.debug("**/filtering durationEntityResponseSet= {} for new ChallengeDuration", durationEntityResponseSet);
        return durationEntityResponseSet;
    }

    public boolean checkChallengeDurationExists(ChallengeDuration challengeDuration) {
        boolean existsChallengeDuration = challengeDurationRepository.existsChallengeDurations(challengeDuration);
        log.debug("**/checking existence ChallengeDuration = {} result ={}",
            challengeDuration, existsChallengeDuration);
        return existsChallengeDuration;
    }


    @Override
    public Boolean deleteChallengeDuration(ChallengeDurationDelete challengeDurationDelete) {
        ChallengeDuration challengeDuration = getChallengeDurationByChallengeIdAndStartEndDate(
            challengeDurationDelete.getChallengeId(),
            challengeDurationDelete.getStartDate(),
            challengeDurationDelete.getEndDate());

        List<UserChallenge> userChallengeList = userChallengeRepository.getListRegisteredByChallengeIdAndDates(
            challengeDurationDelete.getChallengeId(),
            challengeDurationDelete.getStartDate(),
            challengeDurationDelete.getEndDate());

        userChallengeList.forEach(userChallengeService::deleteUserChallenge);
        deleteChallengeDuration(challengeDuration);
        return true;
    }

    public void deleteChallengeDuration(ChallengeDuration challengeDuration) {
        try {
            challengeDurationRepository.delete(challengeDuration);
            challengeDurationRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CHALLENGE_DURATION_DELETING_ERROR);
        }
        archiveModel(challengeDuration);
        log.debug("challengeDuration {} was successfully deleted", challengeDuration);
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
