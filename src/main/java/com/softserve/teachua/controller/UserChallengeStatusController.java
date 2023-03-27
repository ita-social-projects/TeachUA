package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusExist;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import com.softserve.teachua.service.UserChallengeStatusService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "userChallengeStatus", description = "the userChallengeStatus API")
@SecurityRequirement(name = "api")
public class UserChallengeStatusController implements Api {
    private final UserChallengeStatusService userChallengeStatusService;

    public UserChallengeStatusController(UserChallengeStatusService userChallengeStatusService) {
        this.userChallengeStatusService = userChallengeStatusService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/user-challenge/status")
    public List<UserChallengeStatusGet> getAllUserChallengeStatus() {
        return userChallengeStatusService.getAllUserChallengeStatus();
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/user-challenge/status/options")
    public List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions() {
        return userChallengeStatusService.getAllUserChallengeStatusForOptions();
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/user-challenge/status")
    public UserChallengeStatusAdd addUserChallengeStatus(
            @Valid @RequestBody UserChallengeStatusAdd userChallengeStatusAdd) {
        return userChallengeStatusService.addUserChallengeStatus(userChallengeStatusAdd);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/user-challenge/status/edit")
    public UserChallengeStatusUpdate updateUserChallengeStatus(
            @Valid @RequestBody UserChallengeStatusUpdate userChallengeStatusUpdate) {
        return userChallengeStatusService.updateUserChallengeStatus(userChallengeStatusUpdate);
    }

    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/user-challenge/status/{id}")
    public UserChallengeStatusDelete deleteUserChallengeStatus(@NotNull @PathVariable Long id) {
        return userChallengeStatusService.deleteUserChallengeStatusById(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/user-challenge/status/exist/{id}")
    public UserChallengeStatusExist checkIfUserChallengeStatusIdExist(@NotNull @PathVariable Long id) {
        return userChallengeStatusService.isUserChallengeStatusExistsById(id);
    }
}
