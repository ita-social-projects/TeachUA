package com.softserve.teachua.service;

import com.softserve.teachua.dto.user_challenge.UserChallengeCreateResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeDeleteResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeUpdateResponse;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminDelete;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetByChallengeIdDurationId;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminUpdate;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForUserCreate;
import com.softserve.teachua.dto.user_challenge.exist.UserChallengeForExist;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileDelete;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserCreateWithDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetLocalDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetString;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import java.time.LocalDate;
import java.util.List;

public interface UserChallengeService {
    List<UserChallengeForProfileGet> getUserChallengeForProfileByUserId(Long id);

    UserChallengeCreateResponse createUserChallengeByUser(
        UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate);

    UserChallenge getUserChallengeByUserIdChallengeIdStartDateEndDate(
        Long userId, Long challengeId, LocalDate startDate, LocalDate endDate);

    List<UserChallengeForUserGetString> getListUserChallengeDurationByChallengeId(Long id);

    List<UserChallengeForUserGetString> filterNonActiveUserChallengeForUserGetString(
        List<UserChallengeForUserGetLocalDate> userChallengeForUserGetLocalDateList);

    boolean checkIfChallengeIsActive(Long challengeId);

    boolean checkIfDateAfterNowDate(LocalDate date);

    List<UserChallengeForAdminGet> getListUserChallengeForAdmin();

    List<UserChallengeForAdminGetChallengeDuration> getListChallengeDurationForAdmin(Long id);

    List<UserChallengeForAdminRegisteredUser> getListRegisteredUsersByChallengeIdChallengeDurationId(
        UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId);

    List<UserChallengeForAdminNotRegisteredUser> getListNotRegisteredUsersByChallengeIdChallengeDurationId(
        UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId);

    UserChallengeCreateResponse createUserChallengeFromAdmin(UserChallengeForUserCreate userChallengeForUserCreate);

    UserChallengeCreateResponse createUserChallenge(Long userId, ChallengeDuration challengeDuration);

    UserChallengeUpdateResponse updateUserChallengeFromAdmin(UserChallengeForAdminUpdate userChallengeForAdminUpdate);

    User updateUserByIdAndUserChallengeData(
        Long userId, UserChallengeForAdminUpdate userChallengeForAdminUpdate);

    List<UserChallengeForExist> getListUserChallengeForExist();

    boolean isUserChallengeExistByUserIdChallengeDurationId(Long userId, Long challengeDurationId);

    UserChallengeDeleteResponse deleteUserChallengeForProfile(
        UserChallengeForProfileDelete userChallengeForProfileDelete);

    UserChallengeDeleteResponse deleteUserChallengeForAdmin(UserChallengeForAdminDelete userChallengeForAdminDelete);

    UserChallengeDeleteResponse deleteUserChallenge(UserChallenge userChallenge);
}
