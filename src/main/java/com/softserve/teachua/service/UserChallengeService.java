package com.softserve.teachua.service;

import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminDelete;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetByChallengeIdDurationId;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminUpdate;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForUserCreate;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileDelete;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserCreateWithDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetString;
import com.softserve.teachua.model.UserChallenge;
import java.util.List;

public interface UserChallengeService {
    List<UserChallengeForProfileGet> getUserChallengeForProfileByUserId(Long id);

    String deleteUserChallengeForProfile(UserChallengeForProfileDelete userChallengeForProfileDelete);

    String createUserChallengeByUser(UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate);

    String createUserChallengeFromAdmin(UserChallengeForUserCreate userChallengeForUserCreate);

    Long updateUserChallengeFromAdmin(UserChallengeForAdminUpdate userChallengeForAdminUpdate);

    List<UserChallengeForUserGetString> getListUserChallengeDurationByChallengeId(Long id);

    List<UserChallengeForAdminGetChallengeDuration> getListChallengeDurationForAdmin(Long id);

    List<UserChallengeForAdminRegisteredUser> getListRegisteredUsersByChallengeIdChallengeDurationId(
            UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId);

    List<UserChallengeForAdminNotRegisteredUser> getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId);

    List<UserChallengeForAdminGet> getListUserChallengeForAdmin();

    String deleteUserChallengeForAdmin(UserChallengeForAdminDelete userChallengeForAdminDelete);

    String deleteUserChallenge(UserChallenge userChallenge);
}
