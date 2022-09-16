package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.challenge.SuccessCreatedChallenge;
import com.softserve.teachua.tools.service.ChallengeTransferService;
import com.softserve.teachua.utils.annotation.DevPermit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChallengeTransferController implements Api {
    private final ChallengeTransferService challengeTransferService;

    @Autowired
    public ChallengeTransferController(ChallengeTransferService challengeTransferService) {
        this.challengeTransferService = challengeTransferService;
    }

    /**
     * Use this endpoint to create challenges from file. The controller returns list of dto
     * {@code List<SuccessCreatedChallenge>} of created challenges.
     *
     * @param filePath
     *            - path to file with jsons of tasks.
     *
     * @return new {@code List<SuccessCreatedChallenge>}.
     */
    @DevPermit
    @PostMapping("transfer/challenge")
    public List<SuccessCreatedChallenge> addChallengesFromFile(@RequestParam("filePath") String filePath) {
        return challengeTransferService.createChallengesFromFile(filePath);
    }

    /**
     * Use this endpoint to create challenges from infoRepository class. The controller returns list of dto
     * {@code List<SuccessCreatedChallenge>} of created challenges.
     *
     * @return new {@code List<SuccessCreatedChallenge>}.
     */
    @DevPermit
    @PostMapping("transfer/challenge/repository")
    public List<SuccessCreatedChallenge> addChallengesFromRepository() {
        return challengeTransferService.createChallengesFromRepository();
    }

    @DevPermit
    @PatchMapping("challenges/alter/rename")
    public ResponseEntity<String> renameChallenges() {
        return ResponseEntity.ok(challengeTransferService.renameChallengeTable());
    }

    @DevPermit
    @PatchMapping("challenges/alter/description")
    public ResponseEntity<String> alterDescription() {
        return ResponseEntity.ok(challengeTransferService.alterDescriptionColumn());
    }
}
