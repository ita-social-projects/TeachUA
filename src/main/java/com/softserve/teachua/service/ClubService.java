package com.softserve.teachua.service;


import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubService {

    ClubResponse getClubProfileById(Long id);

    Club getClubById(Long id);

    Club getClubByName(String name);

    SuccessUpdatedClub updateClub(Long id, ClubProfile clubProfile);

    ClubResponse getClubProfileByName(String name);

    ClubResponse deleteClubById(Long id);

    SuccessCreatedClub addClub(ClubProfile clubProfile);

    List<ClubResponse> getListOfClubs();

    List<ClubResponse> getSimilarClubsByCategoryName(SimilarClubProfile similarClubProfile);

    Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable);

    List<SearchPossibleResponse> getPossibleClubByName(String text, String cityName);
}
