package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.repository.ChallengeDurationRepository;
import com.softserve.teachua.repository.UserChallengeRepository;
import com.softserve.teachua.service.ChallengeDurationService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.DurationEntityService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
//, ArchiveMark<ChallengeDuration> need to be added
public class ChallengeDurationServiceImpl implements ChallengeDurationService {
    private final ChallengeDurationRepository challengeDurationRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final DurationEntityService durationEntityService;
    private final ChallengeService challengeService;
    private final DtoConverter dtoConverter;

    public ChallengeDurationServiceImpl(ChallengeDurationRepository challengeDurationRepository,
                                        ChallengeService challengeService, DtoConverter dtoConverter,
                                        UserChallengeRepository userChallengeRepository,
                                        DurationEntityService durationEntityService) {
        this.challengeDurationRepository = challengeDurationRepository;
        this.challengeService = challengeService;
        this.dtoConverter = dtoConverter;
        this.userChallengeRepository = userChallengeRepository;
        this.durationEntityService = durationEntityService;
    }

    @Override
    public Boolean existUser(ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate) {
        return challengeDurationRepository
                .existUser(challengeDurationForAdminDurationLocalDate.getStartDate(),
                        challengeDurationForAdminDurationLocalDate.getEndDate());
    }

    @Override
    public List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin() {
        List<ChallengeDurationForAdmin> challengeDurationForAdminList =
                challengeDurationRepository.getListChallengeDurationForAdmin();
        if (challengeDurationForAdminList.isEmpty()) {
            throw new NotExistException("ChallengeDuration not found");
        }
        log.debug("**/getting challengeDurationForAdminList");
        return challengeDurationForAdminList;
    }

    @Override
    public List<ChallengeDurationForAdminDurationString> getListDurations(Long id) {
        List<ChallengeDurationForAdminDurationLocalDate> challengeDurationForAdminDurationLocalDateList =
                challengeDurationRepository.getListDurations(id);
        if (challengeDurationForAdminDurationLocalDateList.isEmpty()) {
            throw new NotExistException("ChallengeDuration not found");
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

        List<DurationEntity> durationEntitySavedList =
            durationEntityService.createAllDurationFromResponseList(durationEntityResponseList);

        List<ChallengeDuration> challengeDurationFromDBList =
                challengeDurationRepository.getChallengeDurationByChallengeId(challengeId);
        durationEntitySavedList
                .stream()
                .filter(durationEntityToAdd -> challengeDurationFromDBList
                        .stream()
                        .noneMatch(durationEntityFromDB -> durationEntityFromDB.getChallenge().equals(challenge)
                            && durationEntityFromDB.getDurationEntity().getStartDate()
                            .equals(durationEntityToAdd.getStartDate())
                            && durationEntityFromDB.getDurationEntity().getEndDate()
                                        .equals(durationEntityToAdd.getEndDate())))
                .forEach(durationEntity -> challengeDurationRepository
                        .save(new ChallengeDuration(
                                0L,
                                false,
                                challenge,
                                durationEntity)));
        return "Успішно";
    }

    @Override
    public Boolean deleteChallengeDuration(ChallengeDurationDelete challengeDurationDelete) {
        ChallengeDuration challengeDuration = challengeDurationRepository
                .getChallengeDurationByChallengeIdAndStartDateAndEndDate(challengeDurationDelete.getChallengeId(),
                        challengeDurationDelete.getStartDate(),
                        challengeDurationDelete.getEndDate())
                .orElseThrow(() -> new NotExistException(
                        String.format("ChallengeDuration not found by challengeId: %s startDate: %s endDate: %s",
                                challengeDurationDelete.getChallengeId(),
                                challengeDurationDelete.getStartDate(),
                                challengeDurationDelete.getEndDate())));

        List<UserChallenge> userChallengeList =
            userChallengeRepository.getListRegisteredByChallengeIdAndDates(
                        challengeDurationDelete.getChallengeId(),
                        challengeDurationDelete.getStartDate(),
                        challengeDurationDelete.getEndDate());

        userChallengeRepository.deleteAll(userChallengeList);

        try {
            challengeDurationRepository.delete(challengeDuration);
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException("challengeDuration deleting error");
        }

        log.debug("challengeDuration {} was successfully deleted", challengeDuration);
        return true;
    }
}
