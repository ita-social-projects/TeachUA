package com.softserve.teachua.service;


import com.softserve.teachua.dto.club.*;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClubService {

    ClubResponse getClubProfileById(Long id);

    Club getClubById(Long id);

    Club addClubsFromExcel(ClubProfile clubProfile);

    List<Club> getClubByClubExternalId(Long clubExternalId);

    Club getClubByName(String name);

    SuccessUpdatedClub updateClub(Long id, ClubResponse clubProfile, HttpServletRequest httpServletRequest);

    ClubResponse getClubProfileByName(String name);

    ClubResponse deleteClubById(Long id, HttpServletRequest httpServletRequest);

    SuccessCreatedClub addClub(ClubProfile clubProfile, HttpServletRequest httpServletRequest);

    List<ClubResponse> getListOfClubs();

    List<ClubResponse> getListClubsByUserId(Long id);

    Page<ClubResponse> getClubsByUserId(Long id, Pageable pageable);

    List<ClubResponse> getSimilarClubsByCategoryName(SimilarClubProfile similarClubProfile);

    Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable);

    Page<ClubResponse> getAdvancedSearchClubs(AdvancedSearchClubProfile advancedSearchClubProfile, Pageable pageable);

    List<SearchPossibleResponse> getPossibleClubByName(String text, String cityName);

    List<ClubResponse> getClubByCategoryAndCity(SearchClubProfile searchClubProfile);

    ClubResponse changeClubOwner(Long id, ClubOwnerProfile clubOwnerProfile, HttpServletRequest httpServletRequest);

    void validateClubOwner(Long id, HttpServletRequest httpServletRequest);

    SuccessUpdatedClub updateRatingNewFeedback(FeedbackResponse feedbackResponse);

    SuccessUpdatedClub updateRatingEditFeedback(FeedbackResponse previousFeedback, FeedbackResponse updatedFeedback);

    SuccessUpdatedClub updateRatingDeleteFeedback(FeedbackResponse feedbackResponse);

    List<ClubResponse> updateRatingForAllClubs();
}
