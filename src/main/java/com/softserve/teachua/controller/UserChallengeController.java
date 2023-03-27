package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
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
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileDelete;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserCreateWithDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetString;
import com.softserve.teachua.service.UserChallengeService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "userChallenge", description = "the UserChallenge API")
@SecurityRequirement(name = "api")
public class UserChallengeController implements Api {
    private final UserChallengeService userChallengeService;

    public UserChallengeController(UserChallengeService userChallengeService) {
        this.userChallengeService = userChallengeService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/user-challenge/user/{id}")
    public List<UserChallengeForProfileGet> getListUserChallengeForProfile(@PathVariable Long id) {
        return userChallengeService.getUserChallengeForProfileByUserId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/user-challenge/user/delete")
    public UserChallengeDeleteResponse deleteUserChallengeForProfile(
            @Valid @RequestBody UserChallengeForProfileDelete userChallengeForProfileDelete) {
        return userChallengeService.deleteUserChallengeForProfile(userChallengeForProfileDelete);
    }

    @GetMapping("/user/user-challenge/registration/durations/{id}")
    public List<UserChallengeForUserGetString> getListUserChallengeDurationByChallengeId(@PathVariable Long id) {
        return userChallengeService.getListUserChallengeDurationByChallengeId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/user-challenge/registration")
    public UserChallengeCreateResponse createUserChallengeByUser(
            @Valid @RequestBody UserChallengeForUserCreateWithDate userChallengeForUserCreateWithDate) {
        return userChallengeService.createUserChallengeByUser(userChallengeForUserCreateWithDate);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/admin/user-challenge/challenges")
    public List<UserChallengeForAdminGet> getListUserChallengeForAdmin() {
        return userChallengeService.getListUserChallengeForAdmin();
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/admin/user-challenge/challenge/{id}/durations")
    public List<UserChallengeForAdminGetChallengeDuration> getListChallengeDurationForAdmin(@PathVariable Long id) {
        return userChallengeService.getListChallengeDurationForAdmin(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/user-challenge/challenge/duration/registered-users")
    public List<UserChallengeForAdminRegisteredUser> getListRegisteredUsersByChallengeIdChallengeDurationId(
            @RequestBody UserChallengeForAdminGetByChallengeIdDurationId userChallenge) {
        return userChallengeService.getListRegisteredUsersByChallengeIdChallengeDurationId(userChallenge);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/user-challenge/challenge/duration/not-registered-users")
    public List<UserChallengeForAdminNotRegisteredUser> getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            @RequestBody
            UserChallengeForAdminGetByChallengeIdDurationId userChallengeForAdminGetByChallengeIdDurationId) {
        return userChallengeService.getListNotRegisteredUsersByChallengeIdChallengeDurationId(
                userChallengeForAdminGetByChallengeIdDurationId);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/user-challenge/challenge/duration/registration")
    public UserChallengeCreateResponse createUserChallengeFromAdmin(
            @Valid @RequestBody UserChallengeForUserCreate userChallengeForUserCreate) {
        return userChallengeService.createUserChallengeFromAdmin(userChallengeForUserCreate);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/admin/user-challenge/challenge/duration/edit")
    public UserChallengeUpdateResponse updateUserChallengeFromAdmin(
            @Valid @RequestBody UserChallengeForAdminUpdate userChallengeForAdminUpdate) {
        return userChallengeService.updateUserChallengeFromAdmin(userChallengeForAdminUpdate);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/user-challenge/challenge/duration/delete")
    public UserChallengeDeleteResponse deleteUserChallengeForAdmin(
            @Valid @RequestBody UserChallengeForAdminDelete userChallengeForAdminDelete) {
        return userChallengeService.deleteUserChallengeForAdmin(userChallengeForAdminDelete);
    }
}
