package com.softserve.teachua.service;

import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.search.AdvancedSearchCenterProfile;
import com.softserve.teachua.model.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CenterService {
    CenterResponse getCenterByProfileId(Long id);

    Center getCenterById(Long id);

    Center getCenterByExternalId(Long id);

    Center getCenterByName(String name);

    SuccessCreatedCenter addCenter(CenterProfile centerProfile);

    SuccessCreatedCenter addCenterRequest(CenterProfile centerProfile, HttpServletRequest httpServletRequest);

    List<CenterResponse> getListOfCenters();

    CenterProfile updateCenter(Long id, CenterProfile centerProfile);

    CenterResponse deleteCenterById(Long id);

    Page<CenterResponse> getCentersByUserId(Long id, Pageable pageable);

    Page<CenterResponse> getAdvancedSearchCenters(AdvancedSearchCenterProfile advancedSearchCenterProfile,
                                                  Pageable pageable);

    CenterResponse updateRatingUpdateClub(ClubResponse previousClub, ClubResponse updatedClub);

    CenterResponse updateRatingDeleteClub(ClubResponse clubResponse);

    List<CenterResponse> updateRatingForAllCenters();
}
