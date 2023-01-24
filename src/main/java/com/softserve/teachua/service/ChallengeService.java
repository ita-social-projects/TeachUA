package com.softserve.teachua.service;

import com.softserve.teachua.dto.challenge.ChallengeDeleteResponse;
import com.softserve.teachua.dto.challenge.ChallengePreview;
import com.softserve.teachua.dto.challenge.ChallengeProfile;
import com.softserve.teachua.dto.challenge.CreateChallenge;
import com.softserve.teachua.dto.challenge.SuccessCreatedChallenge;
import com.softserve.teachua.dto.challenge.SuccessUpdateChallengePreview;
import com.softserve.teachua.dto.challenge.SuccessUpdatedChallenge;
import com.softserve.teachua.dto.challenge.UpdateChallenge;
import com.softserve.teachua.dto.challenge.UpdateChallengeDate;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import java.util.List;

/**
 * This interface contains all needed methods to manage about us items using AboutUsItemRepository.
 */

public interface ChallengeService {
    /**
     * The method returns list of entities {@code List<ChallengePreview>} of all challenges.
     *
     * @return new {@code List<ChallengePreview>}.
     */
    List<ChallengePreview> getAllChallenges(Boolean isActive);

    /**
     * The method returns dto {@code ChallengeProfile} of challenge by id.
     *
     * @param id
     *            - put Challenge id.
     *
     * @return new {@code ChallengeProfile}.
     *
     * @throws NotExistException
     *             if challenge not exists.
     */
    ChallengeProfile getChallenge(Long id);

    /**
     * The method returns entity {@code Challenge} of challenge by id.
     *
     * @param id
     *            - put Challenge id.
     *
     * @return new {@code Challenge}.
     *
     * @throws NotExistException
     *             if challenge not exists.
     */
    Challenge getChallengeById(Long id);

    /**
     * The method returns dto {@code SuccessCreatedChallenge} if challenge successfully added.
     *
     * @param createChallenge
     *            - place body of dto {@code CreateChallenge}.
     *
     * @return new {@code SuccessCreatedChallenge}.
     */
    SuccessCreatedChallenge createChallenge(CreateChallenge createChallenge);

    /**
     * The method updates challenge and returns dto {@code SuccessUpdatedChallenge} of updated challenge.
     *
     * @param id
     *            - put Challenge id.
     * @param updateChallenge
     *            - place body of dto {@code UpdateChallenge}.
     *
     * @return new {@code SuccessUpdatedChallenge}.
     */
    SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge);

    /**
     * The method deletes entity {@code Challenge} returns dto {@code ChallengeDeleteResponse} of deleted challenge by
     * id.
     *
     * @param id
     *            - id of Challenge to delete.
     *
     * @return new {@code ChallengeDeleteResponse}.
     *
     * @throws NotExistException
     *             if challenge not exists.
     */
    ChallengeDeleteResponse deleteChallenge(Long id);

    /**
     * The method updates challenge and returns dto {@code SuccessUpdatedChallenge} of updated challenge.
     *
     * @param id
     *            - put Challenge id.
     * @param updateChallengePreview
     *            - place body of dto {@code SuccessUpdateChallengePreview}.
     *
     * @return new {@code SuccessUpdateChallengePreview}.
     */
    SuccessUpdateChallengePreview updateChallengePreview(Long id, SuccessUpdateChallengePreview updateChallengePreview);

    /**
     * The method returns entity {@code Challenge}
     *
     * @param name
     *            - put Challenge name.
     *
     * @return {@code Challenge}.
     */
    Challenge getChallengeByName(String name);

    /**
     * The method returns {@code List<SuccessUpdatedTask>}
     *
     * @param id
     *            - put Challenge id.
     * @param startDate
     *            - place body of dto {@code UpdateChallengeDate}.
     *
     * @return {@code List<SuccessUpdatedTask>}.
     */
    List<SuccessUpdatedTask> cloneChallenge(Long id, UpdateChallengeDate startDate);
}
