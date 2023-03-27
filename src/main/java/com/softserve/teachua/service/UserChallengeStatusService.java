package com.softserve.teachua.service;

import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusExist;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import com.softserve.teachua.model.UserChallengeStatus;
import java.util.List;

public interface UserChallengeStatusService {
    UserChallengeStatus getUserChallengeStatusById(Long id);

    UserChallengeStatusGet getUserChallengeStatusByStatusName(String statusName);

    List<UserChallengeStatusGet> getAllUserChallengeStatus();

    List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions();

    UserChallengeStatusExist isUserChallengeStatusExistsById(Long id);

    UserChallengeStatusExist isUserChallengeStatusExistsByName(String name);

    List<UserChallengeStatusForOption> mapUserChallengeStatusGetToOptions(
        List<UserChallengeStatusGet> userChallengeStatusGetList);

    UserChallengeStatusAdd addUserChallengeStatus(UserChallengeStatusAdd userChallengeStatusAdd);

    UserChallengeStatusUpdate updateUserChallengeStatus(UserChallengeStatusUpdate userChallengeStatusUpdate);

    UserChallengeStatusDelete deleteUserChallengeStatusById(Long id);
}
