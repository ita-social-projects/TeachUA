package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.ClubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    /**
     * The method returns dto {@code ClubResponse} of club by id.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @Override
    public ClubResponse getClubProfileById(Long id) {
        return dtoConverter.convertToDto(getClubById(id), ClubResponse.class);
    }

    /**
     * The method returns entity {@code Club} of club by id.
     *
     * @param id - put club id.
     * @return new {@code Club}.
     * @throws NotExistException if club not exists.
     */
    @Override
    public Club getClubById(Long id) {
        if (isClubExistById(id)) {
            String clubNotFoundById = String.format(CLUB_NOT_FOUND_BY_ID, id);
            log.error(clubNotFoundById);
            throw new NotExistException(clubNotFoundById);
        }

        Club club = clubRepository.getById(id);
        log.info("**/getting club by id = " + club.getId());
        return club;
    }

    /**
     * The method returns entity {@code Club} of club by name.
     *
     * @param name - put club name.
     * @return new {@code Club}.
     * @throws NotExistException if club not exists.
     */
    @Override
    public Club getClubByName(String name) {
        if (!isClubExistByName(name)) {
            String clubNotFoundByName = String.format(CLUB_NOT_FOUND_BY_NAME, name);
            log.error(clubNotFoundByName);
            throw new NotExistException(clubNotFoundByName);
        }

        Club club = clubRepository.findByName(name);
        log.info("**/getting club by name = " + club.getName());
        return club;
    }

    /**
     * The method returns dto {@code SuccessUpdatedCLub} of updated club.
     *
     * @param clubProfile - place body of dto {@code ClubProfile}.
     * @return new {@code SuccessUpdatedCLub}.
     * @throws NotExistException if club not exists by id.
     */
    @Override
    public SuccessUpdatedClub updateClub(ClubProfile clubProfile) {
        if (isClubExistById(clubProfile.getId())) {
            String clubNotFoundById = String.format(CLUB_NOT_FOUND_BY_ID, clubProfile.getId());
            log.error(clubNotFoundById);
            throw new NotExistException(clubNotFoundById);
        }

        Club club = clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club()));
        log.info("**/updating club = " + club);
        return dtoConverter.convertToDto(club, SuccessUpdatedClub.class);
    }

    /**
     * The method returns dto {@code ClubResponse} of club by name.
     *
     * @param name - put club name.
     * @return new {@code ClubResponse}.
     */
    @Override
    public ClubResponse getClubProfileByName(String name) {
        return dtoConverter.convertToDto(getClubByName(name), ClubResponse.class);
    }

    /**
     * The method returns dto {@code SuccessCreatedClub} if club successfully added.
     *
     * @param clubProfile- place dto with all params.
     * @return new {@code SuccessCreatedClub}.
     * @throws AlreadyExistException if club already exists.
     */
    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        if (isClubExistByName(clubProfile.getName())) {
            String clubAlreadyExist = String.format(CLUB_ALREADY_EXIST, clubProfile.getName());
            log.error(clubAlreadyExist);
            throw new AlreadyExistException(clubAlreadyExist);
        }

        Club club = clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club()));

        log.info("**/adding club with name = " + clubProfile.getName());
        return dtoConverter.convertToDto(club, SuccessCreatedClub.class);
    }

    /**
     * The method returns list of dto {@code List<ClubResponse>} of all clubs.
     *
     * @return new {@code List<ClubResponse>}.
     */
    @Override
    public List<ClubResponse> getListOfClubs() {
        List<ClubResponse> clubResponses = clubRepository.findAll()
                .stream()
                .map(club -> (ClubResponse) dtoConverter.convertToDto(club, ClubResponse.class))
                .collect(Collectors.toList());

        log.info("/**getting list of clubs = " + clubResponses);
        return clubResponses;
    }


    /**
     * The method which return possible results of search by entered text.
     *
     * @param searchClubProfile -  put text of search (based on clubName, cityName & categoryName)
     * @return {@code Page<ClubResponse>}
     */
    @Override
    public Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllByParameters(
                searchClubProfile.getClubName(),
                searchClubProfile.getCityName(),
                searchClubProfile.getCategoryName(),
                pageable);

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) dtoConverter.convertToDto(club, ClubResponse.class))
                .collect(Collectors.toList()),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    /**
     * The method which return possible results of search by entered text.
     *
     * @param text -  put text of search (based on clubName & cityName)
     * @return {@code List<SearchPossibleResponse>}
     */
    @Override
    public List<SearchPossibleResponse> getPossibleClubByName(String text) {
        return clubRepository.findRandomTop3ByName(text)
                .stream()
                .map(category -> (SearchPossibleResponse) dtoConverter.convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    private boolean isClubExistById(Long id) {
        return !clubRepository.existsById(id);
    }

    private boolean isClubExistByName(String name) {
        return clubRepository.existsByName(name);
    }
}
