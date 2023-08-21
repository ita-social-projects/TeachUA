package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationApprovedSuccess;
import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationCanceledSuccess;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationResponse;
import com.softserve.teachua.dto.challenge_registration.FullChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UnapprovedChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationResponse;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeRegistration;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ChallengeRegistrationRepository;
import com.softserve.teachua.service.ChallengeRegistrationService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeRegistrationServiceImpl implements ChallengeRegistrationService {
    private final ChallengeRegistrationRepository challengeRegistrationRepository;
    private final ChildService childService;
    private final UserService userService;
    private final ChallengeService challengeService;
    private final DtoConverter dtoConverter;

    @Override
    @Transactional
    public List<ChildrenChallengeRegistrationResponse> register(ChildrenChallengeRegistrationRequest childrenChallengeRegistrationRequest) {
        log.info("Creating challenge registration with request: {}", childrenChallengeRegistrationRequest);
        Challenge challenge = challengeService.getChallengeById(childrenChallengeRegistrationRequest.getChallengeId());
        List<ChallengeRegistration> registered = childrenChallengeRegistrationRequest.getChildIds().stream()
                .map(createAndSaveChallengeWithChildIdFunction(challenge, childrenChallengeRegistrationRequest.getComment()))
                .toList();
        ChildrenChallengeRegistrationResponse crr = new ChildrenChallengeRegistrationResponse();
        return registered.stream()
                .map(cr -> dtoConverter.convertToDto(cr, crr))
                .toList();
    }

    @Override
    @Transactional
    public UserChallengeRegistrationResponse register(UserChallengeRegistrationRequest userChallengeRegistrationRequest) {
        log.info("Creating user challenge registration with request: {}", userChallengeRegistrationRequest);
        Challenge challenge = challengeService.getChallengeById(userChallengeRegistrationRequest.getChallengeId());
        User user = userService.getUserById(userChallengeRegistrationRequest.getUserId());
        ChallengeRegistration cr = ChallengeRegistration.builder()
                .challenge(challenge)
                .comment(userChallengeRegistrationRequest.getComment())
                .user(user)
                .build();
        cr = challengeRegistrationRepository.save(cr);
        return dtoConverter.convertToDto(cr, new UserChallengeRegistrationResponse());
    }

    @Override
    public List<UnapprovedChallengeRegistration> getAllUnapprovedByManagerId(Long managerId) {
        log.info("Getting all unapproved challenge registrations by manager id: {}", managerId);
        List<ChallengeRegistration> unapprovedChallengesRegistrations = challengeRegistrationRepository
                .findAllUnapprovedByManagerId(managerId);
        UnapprovedChallengeRegistration dto = new UnapprovedChallengeRegistration();
        return unapprovedChallengesRegistrations.stream()
                .map(ucr -> dtoConverter.convertToDto(ucr, dto))
                .toList();
    }

    @Override
    @Transactional
    public ChallengeRegistrationApprovedSuccess approve(Long challengeRegistrationId) {
        log.info("Approving challenge registration with id: {}", challengeRegistrationId);
        challengeRegistrationRepository.approveChallengeRegistration(challengeRegistrationId);
        return new ChallengeRegistrationApprovedSuccess(challengeRegistrationId, true);
    }

    @Override
    @Transactional
    public boolean existsActiveRegistrationForChildren(Long challengeId, Long childId) {
        log.info("Checking if active registration exists for challenge ID {} and child ID {}", challengeId, childId);
        return challengeRegistrationRepository.existsActiveRegistrationByChildIdAndChallengeId(challengeId, childId);
    }

    @Override
    public List<ChildResponse> getChildrenForCurrentUserAndCheckIsDisabledByChallengeId(Long challengeId) {
        log.info("Getting children for current user and checking if disabled by challenge Id {}", challengeId);
        User user = userService.getAuthenticatedUserWithChildren();
        Set<Child> children = user.getChildren();
        ChildResponse cr = new ChildResponse();
        return children.stream()
                .map(c -> createChildResponseWithRegistrationCheck(challengeId, cr, c))
                .toList();
    }

    @Override
    public List<FullChallengeRegistration> getApplicationsForUserAndChildrenByUserId(Long userId) {
        log.info("Getting applications by user ID {}", userId);
        FullChallengeRegistration ua = new FullChallengeRegistration();
        return challengeRegistrationRepository.findRegistrationsByUserIdOrChildParentId(userId)
                .stream()
                .map(cr -> dtoConverter.convertToDto(cr, ua))
                .toList();
    }

    @Override
    @Transactional
    public ChallengeRegistrationCanceledSuccess cancel(Long challengeRegistrationId) {
        log.info("Cancelling challenge registration with id: {}", challengeRegistrationId);
        challengeRegistrationRepository.cancelChallengeRegistration(challengeRegistrationId);
        return new ChallengeRegistrationCanceledSuccess(challengeRegistrationId, false);
    }

    @Override
    public List<FullChallengeRegistration> getAllChallengeRegistrationsByManagerId(Long managerId) {
        log.info("getAllChallengesByUserId: {}", managerId);
        List<ChallengeRegistration> challengeRegistrations = challengeRegistrationRepository
                .findByChallenge_User_Id(managerId);
        FullChallengeRegistration dto = new FullChallengeRegistration();
        return challengeRegistrations.stream()
                .map(cr -> dtoConverter.convertToDto(cr, dto))
                .toList();
    }

    @Override
    public boolean isUserAlreadyRegisteredToChallenge(Long challenge, Long userId) {
        return challengeRegistrationRepository.existsByChallenge_IdAndUser_Id(challenge, userId);
    }

    @NotNull
    private Function<Long, ChallengeRegistration> createAndSaveChallengeWithChildIdFunction(Challenge challenge, String comment) {
        return childId -> {
            Child child = childService.getById(childId);
            ChallengeRegistration challengeRegistration = ChallengeRegistration.builder()
                    .challenge(challenge)
                    .comment(comment)
                    .child(child)
                    .build();
            return challengeRegistrationRepository.save(challengeRegistration);
        };
    }

    private ChildResponse createChildResponseWithRegistrationCheck(
            Long challengeId, ChildResponse cr, Child c) {
        log.info("Creating child response with registration check for challenge ID {} and child ID {}", challengeId, c.getId());
        ChildResponse childResponse = dtoConverter.convertToDto(c, cr);
        if (existsActiveRegistrationForChildren(challengeId, c.getId())) {
            childResponse.setDisabled(true);
        }
        return childResponse;
    }
}
