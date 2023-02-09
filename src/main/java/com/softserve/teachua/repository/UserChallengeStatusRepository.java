package com.softserve.teachua.repository;

import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.model.UserChallengeStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserChallengeStatusRepository extends JpaRepository<UserChallengeStatus, Long> {
    @Query(value = "select distinct new com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet("
            + "    ucs.id,"
            + "    ucs.statusName)"
            + "from UserChallengeStatus ucs "
            + "order by ucs.id")
    List<UserChallengeStatusGet> getAllUserChallengeStatus();

    Optional<UserChallengeStatus> getUserChallengeStatusByStatusName(String statusName);

    Optional<UserChallengeStatus> getUserChallengeStatusById(Long id);

    boolean existsByStatusName(String statusName);
}
