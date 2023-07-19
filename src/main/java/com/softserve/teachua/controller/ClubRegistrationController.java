package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.dto.club_registration.RegistrationApprovedSuccess;
import com.softserve.teachua.dto.club_registration.RegistrationCanceledSuccess;
import com.softserve.teachua.dto.club_registration.UnapprovedClubRegistration;
import com.softserve.teachua.dto.club_registration.FullClubRegistration;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationResponse;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationResponse;
import com.softserve.teachua.service.ClubRegistrationService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "club-registration", description = "the Club Registration API")
@SecurityRequirement(name = "api")
public class ClubRegistrationController implements Api {
    private final ClubRegistrationService clubRegistrationService;

    @GetMapping("/club-registration/user-children/{clubId}")
    public ResponseEntity<List<ChildResponse>> getChildrenByParentId(@PathVariable Long clubId) {
        List<ChildResponse> children = clubRegistrationService
                .getChildrenForCurrentUserAndCheckIsDisabledByClubId(clubId);

        return ResponseEntity.ok(children);
    }

    @GetMapping("/club-registration/user-applications/{userId}")
    public ResponseEntity<List<FullClubRegistration>> getUserApplications(@PathVariable Long userId) {
        List<FullClubRegistration> applications = clubRegistrationService.getApplicationsByUserId(userId);

        return ResponseEntity.ok(applications);
    }

    @AllowedRoles(RoleData.USER)
    @PostMapping("/club-registration")
    public ResponseEntity<List<ClubRegistrationResponse>> addClubRegistration(
            @Valid @RequestBody ClubRegistrationRequest clubRegistrationRequest) {
        List<ClubRegistrationResponse> response = clubRegistrationService.create(clubRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AllowedRoles(RoleData.USER)
    @PostMapping("/club-registration/user")
    public ResponseEntity<UserClubRegistrationResponse> addClubRegistrationForUser(
            @Valid @RequestBody UserClubRegistrationRequest userClubRegistrationRequest) {
        UserClubRegistrationResponse response = clubRegistrationService.create(userClubRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AllowedRoles(RoleData.MANAGER)
    @GetMapping("/club-registration/unapproved/{managerId}")
    public ResponseEntity<List<UnapprovedClubRegistration>> getUnapprovedClubRegistrationsByManagerId(
            @PathVariable Long managerId) {
        var unapprovedClubRegistrations = clubRegistrationService.getAllUnapprovedByManagerId(managerId);
        log.debug("got unapproved registrations list {}", unapprovedClubRegistrations);

        return ResponseEntity.ok(unapprovedClubRegistrations);
    }

    @AllowedRoles(RoleData.MANAGER)
    @GetMapping("/club-registration/{managerId}")
    public ResponseEntity<List<FullClubRegistration>> getClubRegistrationsByManagerId(
            @PathVariable Long managerId) {
        var clubRegistrations = clubRegistrationService.getAllByManagerId(managerId);
        log.debug("got registrations list {}", clubRegistrations);

        return ResponseEntity.ok(clubRegistrations);
    }

    @AllowedRoles(RoleData.MANAGER)
    @PatchMapping("/club-registration/approve/{clubRegistrationId}")
    public ResponseEntity<RegistrationApprovedSuccess> approveRegistration(@PathVariable Long clubRegistrationId) {
        RegistrationApprovedSuccess approved = clubRegistrationService.approve(clubRegistrationId);

        return ResponseEntity.ok(approved);
    }

    @AllowedRoles({RoleData.USER, RoleData.MANAGER})
    @PatchMapping("/club-registration/cancel/{clubRegistrationId}")
    public ResponseEntity<RegistrationCanceledSuccess> cancelRegistration(@PathVariable Long clubRegistrationId) {
        RegistrationCanceledSuccess canceled = clubRegistrationService.cancel(clubRegistrationId);

        return ResponseEntity.ok(canceled);
    }
}
