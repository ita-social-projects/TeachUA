package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.ChallengeDuration;
import java.time.LocalDate;
import java.util.List;

public interface ChallengeDurationService {
    List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin();

    List<ChallengeDurationForAdminDurationString> getListDurations(Long id);

    String createChallengeDuration(Long challengeId, List<DurationEntityResponse> durationEntityResponseList);

    Boolean deleteChallengeDuration(ChallengeDurationDelete challengeDurationDelete);

    Boolean existUser(ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate);

    ChallengeDuration getChallengeDurationByChallengeIdAndStartEndDate(
        Long challengeId, LocalDate startDate, LocalDate endDate);
}
