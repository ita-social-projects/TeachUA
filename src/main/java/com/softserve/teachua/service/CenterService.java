package com.softserve.teachua.service;

import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.search.AdvancedSearchCenterProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface contains all needed methods to manage centers.
 */

public interface CenterService {
    /**
     * The method returns dto {@code CenterResponse} of center by id.
     *
     * @param id
     *            - put center id.
     *
     * @return new {@code CenterResponse}.
     */
    CenterResponse getCenterProfileById(Long id);

    /**
     * The method returns entity {@code Center} of center by id.
     *
     * @param id
     *            - put center id.
     *
     * @return new {@code Center}.
     *
     * @throws NotExistException
     *             if center not exists.
     */
    Center getCenterById(Long id);

    /**
     * The method returns entity {@code Center} of center by external id.
     *
     * @param centerExternalId
     *            - put centerExternalId id.
     *
     * @return new {@code Center}.
     *
     * @throws NotExistException
     *             if center not exists.
     */
    Center getCenterByExternalId(Long centerExternalId);

    /**
     * The method returns entity {@code Center} of center by name.
     *
     * @param name
     *            - put center name.
     *
     * @return new {@code Center}.
     *
     * @throws NotExistException
     *             if center not exists.
     */
    Center getCenterByName(String name);

    /**
     * The method returns dto {@code SuccessCreatedCenter} if center successfully added.
     *
     * @param centerProfile
     *            - place body of dto {@code CenterProfile}.
     *
     * @return new {@code SuccessCreatedCenter}.
     *
     * @throws AlreadyExistException
     *             if center already exists.
     */
    SuccessCreatedCenter addCenter(CenterProfile centerProfile);

    /**
     * The method returns dto {@code SuccessCreatedCenter} if center successfully added.
     *
     * @param centerProfile
     *            - place body of dto {@code CenterProfile}.
     *
     * @return new {@code SuccessCreatedCenter}.
     *
     * @throws AlreadyExistException
     *             if center already exists.
     */
    SuccessCreatedCenter addCenterRequest(CenterProfile centerProfile);

    /**
     * The method returns list of dto {@code List<CenterResponse>} of all centers.
     *
     * @return new {@code List<CenterResponse>}.
     */
    List<CenterResponse> getListOfCenters();

    /**
     * The method returns dto {@code CenterProfile} of updated club.
     *
     * @param centerProfile
     *            - place body of dto {@code CenterProfile}.
     *
     * @return new {@code CenterProfile}.
     */
    CenterProfile updateCenter(Long id, CenterProfile centerProfile);

    /**
     * The method deletes entity {@code Center} and returns dto {@code CenterResponse} of deleted center by id.
     *
     * @param id
     *            - id of Center to delete.
     *
     * @return new {@code CenterResponse}.
     *
     * @throws NotExistException
     *             if center not exists.
     */
    CenterResponse deleteCenterById(Long id);

    /**
     * The method returns list of dto {@code Page<CenterResponse>} of all centers by user-owner.
     *
     * @param id
     *            - put user id.
     *
     * @return new {@code Page<ClubResponse>}.
     */
    Page<CenterResponse> getCentersByUserId(Long id, Pageable pageable);

    /**
     * The method returns list of dto {@code Page<ClubResponse>} of all centers by center id.
     *
     * @param id
     *            - put center id.
     *
     * @return new {@code Page<ClubResponse>}.
     */
    Page<ClubResponse> getCenterClubsByCenterId(Long id, Pageable pageable);

    /**
     * The method returns page of dto {@code Page<CenterResponse>} of all centers by advancedSearchCenterProfile.
     *
     * @param advancedSearchCenterProfile
     *            - put user id.
     * @param pageable
     *            - pagination object.
     *
     * @return new {@code Page<ClubResponse>}.
     *
     * @author Vasyl Khula
     */
    Page<CenterResponse> getAdvancedSearchCenters(AdvancedSearchCenterProfile advancedSearchCenterProfile,
            Pageable pageable);

    void updateRatingNewFeedback(FeedbackResponse feedbackResponse);

    /**
     * The method updates rating of club and returns dto {@code CenterResponse} of updated club.
     *
     * @param previousClub
     *            - id of Center to update club.
     * @param updatedClub
     *            - updated club.
     *
     * @return new {@code CenterResponse}.
     */
    CenterResponse updateRatingUpdateClub(ClubResponse previousClub, ClubResponse updatedClub);

    /**
     * The method updates rating of club and returns dto {@code CenterResponse} of updated club.
     *
     * @return new {@code CenterResponse}.
     */
    CenterResponse updateRatingDeleteClub(ClubResponse clubResponse);

    /**
     * The method updates rating of all centers and returns list of dto {@code List<CenterResponse>}.
     *
     * @return new {@code List<CenterResponse>}.
     */
    List<CenterResponse> updateRatingForAllCenters();
}
