package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.ClubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ClubResponse getClubById(Long id) {
        if (!isClubExistById(id)) {
            String clubNotFoundById = String.format("Club not found by id %s", id);
            log.error(clubNotFoundById);
            throw new NotExistException(clubNotFoundById);
        }
        Club club = clubRepository.getById(id);
        String gettingClubDyId = String.format("Getting club by id %s", id);
        log.info(gettingClubDyId);
        return ClubResponse.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .name(club.getName())
                .urlWeb(club.getUrlWeb())
                .build();
    }

    @Override
    public Club getClubByName(String name) {
        return clubRepository.findByName(name);
    }

    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        if (isClubExistByName(clubProfile.getName())) {
            String clubAlreadyExist = String.format("Club already exist by name %s", clubProfile.getName());
            log.error(clubAlreadyExist);
            throw new AlreadyExistException(clubAlreadyExist);
        }
        Club club = clubRepository.save(Club
                .builder()
                .ageFrom(clubProfile.getAgeFrom())
                .ageTo(clubProfile.getAgeTo())
                .name(clubProfile.getName())
                .build());
        String clubAdding = String.format("Added club by name %s", clubProfile.getName());
        log.info(clubAdding);
        return SuccessCreatedClub.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .build();
    }

    @Override
    public List<ClubResponse> getListOfClubs() {
        String gettingListOfClubs = String.format("Getting list of clubs");
        log.info(gettingListOfClubs);
        return clubRepository.findAll()
                .stream()
                .map(club -> new ClubResponse(club.getId(), club.getAgeFrom(), club.getAgeTo(), club.getName(), club.getUrlWeb()))
                .collect(Collectors.toList());
    }

    private boolean isClubExistById(Long id) {
        return getClubById(id) != null;
    }

    private boolean isClubExistByName(String name) {
        return getClubByName(name) != null;
    }
}
