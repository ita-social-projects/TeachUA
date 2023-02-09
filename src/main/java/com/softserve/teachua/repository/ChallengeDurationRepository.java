package com.softserve.teachua.repository;

import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.model.ChallengeDuration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeDurationRepository extends JpaRepository<ChallengeDuration, Long> {
    @Query(value = "select distinct new com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin("
        + "    c.id,"
        + "    c.name,"
        + "    c.isActive)"
        + "from Challenge c")
    List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin();

    List<ChallengeDuration> getChallengeDurationByChallengeId(Long id);

    Optional<ChallengeDuration> getChallengeDurationById(Long challengeDurationId);

    @Query(value = "select cd "
        + "from ChallengeDuration cd "
        + "where cd.challenge.id=?1 "
        + "and cd.durationEntity.id=?2")
    Optional<ChallengeDuration> getChallengeDurationByChallengeIdAndDurationId(Long challengeId, Long durationId);

    @Query(value = "select cd "
        + "from ChallengeDuration cd "
        + "where cd.challenge.id =:challengeId "
        + "and cd.durationEntity.startDate=:startDate "
        + "and cd.durationEntity.endDate=:endDate")
    Optional<ChallengeDuration> getChallengeDurationByChallengeIdAndStartEndDate(Long challengeId, LocalDate startDate,
                                                                                 LocalDate endDate);

    @Query(value =
        "select distinct new com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate("
            + "       cd.durationEntity.startDate,"
            + "       cd.durationEntity.endDate)"
            + "from ChallengeDuration cd "
            + "where cd.challenge.id =:id")
    List<ChallengeDurationForAdminDurationLocalDate> getListDurations(Long id);

    @Query(value = "select cd "
            + "     from ChallengeDuration cd "
            + "     where cd.challenge.id = :challengeId"
            + "     and cd.durationEntity.startDate = :startDate "
            + "     and cd.durationEntity.endDate = :endDate")
    Optional<ChallengeDuration> getChallengeDurationByChallengeIdAndStartDateAndEndDate(Long challengeId,
                                                                                        LocalDate startDate,
                                                                                        LocalDate endDate);

    @Query(value = "select "
            + "      case "
            + "          when count(uc.user) > 0 then true "
            + "          else false "
            + "      end "
            + "      from UserChallenge uc"
            + "      where uc.challengeDuration.durationEntity.startDate =:startDate "
            + "      and uc.challengeDuration.durationEntity.endDate =:endDate")
    boolean existUser(LocalDate startDate, LocalDate endDate);
}
