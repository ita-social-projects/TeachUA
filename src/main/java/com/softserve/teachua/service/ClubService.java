package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.model.Club;

import java.util.List;

public interface ClubService {

    ClubResponse getClubProfileById(Long id);

    Club getClubById(Long id);

    Club getClubByName(String name);

    SuccessCreatedClub addClub(ClubProfile clubProfile);

    List<ClubResponse> getListOfClubs();
}
