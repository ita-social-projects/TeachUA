package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationApprovedSuccess;
import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationCanceledSuccess;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationResponse;
import com.softserve.teachua.dto.challenge_registration.FullChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UnapprovedChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationResponse;
import com.softserve.teachua.dto.child.ChildResponse;


import java.util.List;

public interface ChallengeRegistrationService {
    List<ChildrenChallengeRegistrationResponse> register(ChildrenChallengeRegistrationRequest childrenChallengeRegistrationRequest);

    UserChallengeRegistrationResponse register(UserChallengeRegistrationRequest userChallengeRegistrationRequest);

    List<UnapprovedChallengeRegistration> getAllUnapprovedByManagerId(Long managerId);

    ChallengeRegistrationApprovedSuccess approve(Long challengeRegistrationId);

    boolean existsActiveRegistrationForChildren(Long challengeId, Long childId);

    List<ChildResponse> getChildrenForCurrentUserAndCheckIsDisabledByChallengeId(Long challengeId);

    List<FullChallengeRegistration> getApplicationsForUserAndChildrenByUserId(Long userId);

    ChallengeRegistrationCanceledSuccess cancel(Long challengeRegistrationId);

    List<FullChallengeRegistration> getAllChallengeRegistrationsByManagerId(Long managerId);

    boolean isUserAlreadyRegisteredToChallenge(Long challenge, Long userId);
}
