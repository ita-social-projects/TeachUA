package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationApprovedSuccess;
import com.softserve.teachua.dto.challenge_registration.ChallengeRegistrationCanceledSuccess;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.ChildrenChallengeRegistrationResponse;
import com.softserve.teachua.dto.challenge_registration.FullChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UnapprovedChallengeRegistration;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationRequest;
import com.softserve.teachua.dto.challenge_registration.UserChallengeRegistrationResponse;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.service.ChallengeRegistrationService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "challenge-registration", description = "the Challenge Registration API")
@SecurityRequirement(name = "api")
public class ChallengeRegistrationController implements Api {
    private final ChallengeRegistrationService challengeRegistrationService;

    @GetMapping("/challenge-registration/user-children/{clubId}")
    public ResponseEntity<List<ChildResponse>> getChildrenForCurrentUserByClubId(@PathVariable Long clubId) {
        List<ChildResponse> children = challengeRegistrationService
                .getChildrenForCurrentUserAndCheckIsDisabledByChallengeId(clubId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/challenge-registration/user-applications/{userId}")
    public ResponseEntity<List<FullChallengeRegistration>> getUserAndChildrenApplications(@PathVariable Long userId) {
        List<FullChallengeRegistration> applications = challengeRegistrationService.getApplicationsForUserAndChildrenByUserId(userId);
        return ResponseEntity.ok(applications);
    }

    @AllowedRoles(RoleData.USER)
    @PostMapping("/challenge-registration")
    public ResponseEntity<UserChallengeRegistrationResponse> challengeRegistrationUser(
            @Valid @RequestBody UserChallengeRegistrationRequest userChallengeRegistrationRequest) {
        log.info("challengeRegistrationUser - {}", userChallengeRegistrationRequest);
        UserChallengeRegistrationResponse response = challengeRegistrationService.create(userChallengeRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AllowedRoles(RoleData.USER)
    @PostMapping("/challenge-registration/children")
    public ResponseEntity<List<ChildrenChallengeRegistrationResponse>> challengeRegistrationChildren(
            @Valid @RequestBody ChildrenChallengeRegistrationRequest childrenChallengeRegistrationRequest) {
        log.info("challengeRegistrationChildren - {}", childrenChallengeRegistrationRequest);
        List<ChildrenChallengeRegistrationResponse> response = challengeRegistrationService.create(childrenChallengeRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AllowedRoles(RoleData.MANAGER)
    @GetMapping("/challenge-registration/unapproved/{managerId}")
    public ResponseEntity<List<UnapprovedChallengeRegistration>> getUnapprovedChallengeRegistrationsByManagerId(
            @PathVariable Long managerId) {
        var unapprovedClubRegistrations = challengeRegistrationService.getAllUnapprovedByManagerId(managerId);
        log.debug("got unapproved registrations list {}", unapprovedClubRegistrations);
        return ResponseEntity.ok(unapprovedClubRegistrations);
    }

    @AllowedRoles(RoleData.USER)
    @GetMapping("/challenge-registration/{userId}")
    public ResponseEntity<List<FullChallengeRegistration>> getUsersChallengeRegistrations(
            @PathVariable Long userId) {
        log.info("getUsersChallengeRegistrations - {}", userId);
        var challengeRegistrations = challengeRegistrationService.getAllChallengesByUserId(userId);
        log.debug("got registrations list {}", challengeRegistrations);
        return ResponseEntity.ok(challengeRegistrations);
    }

    @AllowedRoles(RoleData.MANAGER)
    @PatchMapping("/challenge-registration/approve/{challengeRegistrationId}")
    public ResponseEntity<ChallengeRegistrationApprovedSuccess> approveRegistration(@PathVariable Long challengeRegistrationId) {
        ChallengeRegistrationApprovedSuccess approved = challengeRegistrationService.approve(challengeRegistrationId);
        return ResponseEntity.ok(approved);
    }

    @AllowedRoles({RoleData.USER, RoleData.MANAGER})
    @PatchMapping("/challenge-registration/cancel/{challengeRegistrationId}")
    public ResponseEntity<ChallengeRegistrationCanceledSuccess> cancelRegistration(@PathVariable Long challengeRegistrationId) {
        ChallengeRegistrationCanceledSuccess canceled = challengeRegistrationService.cancel(challengeRegistrationId);
        return ResponseEntity.ok(canceled);
    }
}
