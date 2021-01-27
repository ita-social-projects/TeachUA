package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
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
    private static final String CLUB_ALREADY_EXIST = "Club already exist with name: %s";
    private static final String CLUB_NOT_FOUND_BY_ID = "Club not found by id: %s";
    private static final String CLUB_NOT_FOUND_BY_NAME = "Club not found by name: %s";

    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, DtoConverter dtoConverter) {
        this.clubRepository = clubRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public ClubResponse getClubProfileById(Long id) {
        return dtoConverter.convertToDto(getClubById(id), ClubResponse.class);
    }

    @Override
    public Club getClubById(Long id) {
        if (!isClubExistById(id)) {
            String clubNotFoundById = String.format(CLUB_NOT_FOUND_BY_ID, id);
            log.error(clubNotFoundById);
            throw new NotExistException(clubNotFoundById);
        }

        Club club = clubRepository.getById(id);
        log.info("**/getting club by id = " + club);
        return club;
    }

    @Override
    public Club getClubByName(String name) {
        if (!isClubExistByName(name)) {
            String clubNotFoundById = String.format(CLUB_NOT_FOUND_BY_NAME, name);
            log.error(clubNotFoundById);
            throw new NotExistException(clubNotFoundById);
        }

        Club club = clubRepository.findByName(name);
        log.info("**/getting club by name = " + club);
        return club;
    }

    @Override
    public ClubResponse getClubProfileByName(String name) {
        Club club = getClubByName(name);
        return ClubResponse.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .name(club.getName())
                .urlWeb(club.getUrlWeb())
                .urlLogo(club.getUrlLogo())
                .workTime(club.getWorkTime())
                .city(club.getCity())
                .categories(club.getCategories())
                .user(club.getUser())
                .center(club.getCenter())
                .build();
    }

    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        if (isClubExistByName(clubProfile.getName())) {
            String clubAlreadyExist = String.format(CLUB_ALREADY_EXIST, clubProfile.getName());
            log.error(clubAlreadyExist);
            throw new AlreadyExistException(clubAlreadyExist);
        }

        Club club = clubRepository.save(Club
                .builder()
                .ageFrom(clubProfile.getAgeFrom())
                .ageTo(clubProfile.getAgeTo())
                .name(clubProfile.getName())
                .build());

        log.info("**/adding club with name = " + clubProfile.getName());
        return SuccessCreatedClub.builder()
                .id(club.getId())
                .ageFrom(club.getAgeFrom())
                .ageTo(club.getAgeTo())
                .build();
    }

    @Override
    public List<ClubResponse> getListOfClubs() {
        List<ClubResponse> clubResponses = clubRepository.findAll()
                .stream()
                .map(club -> new ClubResponse(club.getId(), club.getAgeFrom(), club.getAgeTo(),
                        club.getName(), club.getUrlWeb(), club.getUrlLogo()
                        , club.getWorkTime(), club.getCity(), club.getCategories(), club.getUser(), club.getCenter()))
                .collect(Collectors.toList());

        log.info("/**getting list of clubs = " + clubResponses);
        return clubResponses;
    }

    private boolean isClubExistById(Long id) {
        return clubRepository.existsById(id);
    }

    private boolean isClubExistByName(String name) {
        return clubRepository.existsByName(name);
    }
}
