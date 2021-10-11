package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.challenge.*;
import com.softserve.teachua.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ChallengeController implements Api {

    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping("/challenges")
    public List<ChallengePreview> getAllChallenges(@RequestParam(required = false) Boolean active) {
        return challengeService.getAllChallenges(active);
    }

    @GetMapping("/challenge/{id}")
    public ChallengeProfile getChallenge(@PathVariable Long id) {
        return challengeService.getChallenge(id);
    }

    @PostMapping("/challenge")
    public SuccessCreatedChallenge createChallenge(
            @RequestBody CreateChallenge createChallenge,
            HttpServletRequest httpServletRequest) {
        return challengeService.createChallenge(createChallenge, httpServletRequest);
    }

    @PutMapping("/challenge/{id}")
    public SuccessUpdatedChallenge updateChallenge(
            @PathVariable Long id,
            @RequestBody UpdateChallenge updateChallenge) {
        return challengeService.updateChallenge(id, updateChallenge);
    }

    @DeleteMapping("/challenge/{id}")
    public ChallengeDeleteResponse deleteChallenge(@PathVariable Long id) {
        return challengeService.deleteChallenge(id);
    }
}
