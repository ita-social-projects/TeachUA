package com.softserve.teachua.service;

import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import java.util.List;

public interface UserChallengeStatusService {
    List<UserChallengeStatusGet> getAllUserChallengeStatus();

    List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions();

    UserChallengeStatusGet getUserChallengeStatusByStatusName(String statusName);

    UserChallengeStatusAdd addUserChallengeStatus(UserChallengeStatusAdd userChallengeStatusAdd);

    UserChallengeStatusDelete deleteUserChallengeStatusById(Long id);

    UserChallengeStatusUpdate updateUserChallengeStatus(UserChallengeStatusUpdate userChallengeStatusUpdate);

    boolean isUserChallengeStatusExistsById(Long id);
}
