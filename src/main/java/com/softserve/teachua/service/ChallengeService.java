package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge.*;
import com.softserve.teachua.model.Challenge;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ChallengeService {
    List<ChallengePreview> getAllChallenges(Boolean isActive);

    ChallengeProfile getChallenge(Long id, Pageable pageable);

    Challenge getChallengeById(Long id);

    SuccessCreatedChallenge createChallenge(CreateChallenge createChallenge, HttpServletRequest httpServletRequest);

    SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge);

    ChallengeDeleteResponse deleteChallenge(Long id);

    SuccessUpdateChallengePreview updateChallengePreview(Long id, SuccessUpdateChallengePreview updateChallengePreview);
}
