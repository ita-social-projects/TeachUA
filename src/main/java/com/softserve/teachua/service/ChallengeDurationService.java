package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge_duration.ChallengeDurationAdd;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDeleteResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationExistUserResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.ChallengeDuration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ChallengeDurationService {
    List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin();

    List<ChallengeDurationForAdminDurationString> getListDurations(Long id);

    Set<ChallengeDurationAdd> createChallengeDuration(
        Long challengeId, List<DurationEntityResponse> durationEntityResponseList);

    ChallengeDuration createChallengeDuration(ChallengeDuration challengeDuration);

    ChallengeDurationDeleteResponse deleteChallengeDuration(ChallengeDurationDelete challengeDurationDelete);

    ChallengeDurationDeleteResponse deleteChallengeDuration(ChallengeDuration challengeDuration);

    Boolean existUser(ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate);

    ChallengeDuration getChallengeDurationByChallengeIdAndStartEndDate(
        Long challengeId, LocalDate startDate, LocalDate endDate);

    ChallengeDuration getChallengeDurationByChallengeIdAndDurationId(Long challengeId, Long durationId);

    ChallengeDuration getChallengeDurationById(Long challengeDurationId);

    void updateChallengeDurationUserExist(Long challengeDurationId, Boolean userExist);

    boolean existChallengeDurationRegisteredUsers(Long challengeDurationId);

    ChallengeDurationExistUserResponse checkChallengeDurationExists(ChallengeDuration challengeDuration);
}
