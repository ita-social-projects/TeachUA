package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.service.ChallengeDurationService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "challengeDuration", description = "the challengeDuration API")
@SecurityRequirement(name = "api")
public class ChallengeDurationController implements Api {
    private final ChallengeDurationService challengeDurationService;

    public ChallengeDurationController(ChallengeDurationService challengeDurationService) {
        this.challengeDurationService = challengeDurationService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/admin/challenge-durations")
    public List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin() {
        return challengeDurationService.getListChallengeDurationForAdmin();
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/admin/challenge-durations/{id}")
    public List<ChallengeDurationForAdminDurationString> getListDurations(@PathVariable Long id) {
        return challengeDurationService.getListDurations(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/challenge-duration/{id}")
    public String createChallengeDuration(@PathVariable Long id,
                                          @RequestBody List<DurationEntityResponse> durationEntityResponseList) {
        return challengeDurationService.createChallengeDuration(id, durationEntityResponseList);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/challenge-duration/delete")
    public Boolean deleteChallengeDurationForAdmin(@RequestBody ChallengeDurationDelete challengeDurationDelete) {
        return challengeDurationService.deleteChallengeDuration(challengeDurationDelete);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/admin/challenge-durations/check-users")
    public boolean existUsers(
            @RequestBody ChallengeDurationForAdminDurationLocalDate challengeDurationForAdminDurationLocalDate) {
        return challengeDurationService.existUser(challengeDurationForAdminDurationLocalDate);
    }
}
