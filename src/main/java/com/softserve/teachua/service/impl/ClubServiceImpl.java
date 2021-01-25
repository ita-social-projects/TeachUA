package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ClubResponse getClubById(Long id) {
        Club club = clubRepository.getById(id);
        return ClubResponse.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .name(club.getName())
                .urlWeb(club.getUrlWeb())
                .build();
    }

    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        Club club = clubRepository.save(Club
                .builder()
                .ageFrom(clubProfile.getAgeFrom())
                .ageTo(clubProfile.getAgeTo())
                .name(clubProfile.getName())
                .build());
        return SuccessCreatedClub.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .build();
    }

    @Override
    public List<ClubResponse> getListOfClubs() {
        return clubRepository.findAll()
                .stream()
                .map(club -> new ClubResponse(club.getId(), club.getAgeFrom(), club.getAgeTo(), club.getName(), club.getUrlWeb()))
                .collect(Collectors.toList());
    }
}
