package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge.*;
import com.softserve.teachua.model.Challenge;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ChallengeService {
    List<ChallengePreview> getAllChallenges(Boolean isActive);

    ChallengeProfile getChallenge(Long id);

    Challenge getChallengeById(Long id);

    SuccessCreatedChallenge createChallenge(CreateChallenge createChallenge, HttpServletRequest httpServletRequest);

    SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge);

    ChallengeDeleteResponse deleteChallenge(Long id);
}
