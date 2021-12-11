package com.softserve.teachua.tools.transfer_service;

import com.softserve.teachua.dto.challenge.SuccessCreatedChallenge;

import java.util.List;

public interface ChallengeTransferService {
    /**
     * The method returns list of dto {@code List<SuccessCreatedChallenge>} if challenges successfully added.
     *
     * @param filePath - path to file with jsons of tasks.
     * @return new {@code List<SuccessCreatedChallenge>}.
     */
    List<SuccessCreatedChallenge> createChallengesFromFile(String filePath);

    /**
     * The method returns list of dto {@code List<SuccessCreatedChallenge>} if challenges successfully added.
     *
     * @return new {@code List<SuccessCreatedChallenge>}.
     */
    List<SuccessCreatedChallenge> createChallengesFromRepository();
}
