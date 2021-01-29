package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.search.SearchClubDto;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubService {

    ClubResponse getClubProfileById(Long id);

    Club getClubById(Long id);

    Club getClubByName(String name);

    ClubResponse getClubProfileByName(String name);

    SuccessCreatedClub addClub(ClubProfile clubProfile);

    List<ClubResponse> getListOfClubs();

    Page<ClubResponse> getClubsBySearchParameters(SearchClubDto searchClubDto, Pageable pageable);
}
