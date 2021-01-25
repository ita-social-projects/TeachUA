package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;

import java.util.List;

public interface ClubService {
    ClubResponse getClubById(Long id);

    SuccessCreatedClub addClub(ClubProfile clubProfile);

    List<ClubResponse> getListOfClubs();
}
