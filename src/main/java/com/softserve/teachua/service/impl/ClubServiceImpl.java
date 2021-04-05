package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.*;
import com.softserve.teachua.utils.CategoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class ClubServiceImpl implements ClubService {
    private static final String CLUB_ALREADY_EXIST = "Club already exist with name: %s";
    private static final String CLUB_NOT_FOUND_BY_ID = "Club not found by id: %s";
    private static final String CLUB_NOT_FOUND_BY_NAME = "Club not found by name: %s";
    private static final String CLUB_DELETING_ERROR = "Can't delete club cause of relationship";


    private final ClubRepository clubRepository;
    private final LocationRepository locationRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final CategoryService categoryService;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, LocationRepository locationRepository, DtoConverter dtoConverter, ArchiveService archiveService, CityService cityService, DistrictService districtService, StationService stationService, CategoryService categoryService) {
        this.clubRepository = clubRepository;
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.categoryService = categoryService;
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
        Optional<Club> optionalClub = getOptionalClubById(id);
        if (!optionalClub.isPresent()) {
            throw new NotExistException(String.format(CLUB_NOT_FOUND_BY_ID, id));
        }

        Club club = optionalClub.get();

        log.info("getting club by id {}", id);
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
        Optional<Club> optionalClub = getOptionalClubByName(name);
        if (!optionalClub.isPresent()) {
            throw new NotExistException(String.format(CLUB_NOT_FOUND_BY_NAME, name));
        }

        Club club = optionalClub.get();
        log.info("getting club by name {}", club.getName());
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
    public SuccessUpdatedClub updateClub(Long id, ClubProfile clubProfile) {
        Club club = getClubById(id);
        Club newClub = dtoConverter.convertToEntity(clubProfile, club)
                .withId(id);

        log.info("updating club by id {}", newClub);
        return dtoConverter.convertToDto(clubRepository.save(newClub), SuccessUpdatedClub.class);
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
            throw new AlreadyExistException(String.format(CLUB_ALREADY_EXIST, clubProfile.getName()));
        }

        Club club = clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                .withCategories(clubProfile.getCategoriesName()
                        .stream()
                        .map(categoryService::getCategoryByName)
                        .collect(Collectors.toSet())));

        if(!clubProfile.getLocations().isEmpty()) {
            club.setLocations(
                    clubProfile.getLocations()
                            .stream()
                            .map(locationProfile -> locationRepository.save(
                                    dtoConverter.convertToEntity(locationProfile, new Location())
                                            .withClub(club)
                                            .withCity(cityService.getCityByName(locationProfile.getCityName()))
                                            .withDistrict(districtService.getOptionalDistrictByName(
                                                    locationProfile.getDistrictName())
                                                    .orElse(null))
                                            .withStation(stationService.getOptionalStationByName(
                                                    locationProfile.getStationName())
                                                    .orElse(null))
                            ))
                            .collect(Collectors.toSet())
            );
        }

        log.info("adding club with name {}", clubProfile.getName());
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

        log.info("getting list of clubs {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getSimilarClubsByCategoryName(SimilarClubProfile similarClubProfile) {
        return clubRepository.findByCategoryName(
                similarClubProfile.getId(),
                CategoryUtil.replaceSemicolonToComma(similarClubProfile.getCategoriesName()),
                similarClubProfile.getCityName(),
                PageRequest.of(0, 2))
                .stream()
                .map(category -> (ClubResponse) dtoConverter.convertToDto(category, ClubResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * The method which return possible results of search by entered text.
     *
     * @param advancedSearchClubProfile -  put text of advanced search
     * @return {@code Page<ClubResponse>}
     */
    @Override
    public Page<ClubResponse> getAdvancedSearchClubs(AdvancedSearchClubProfile advancedSearchClubProfile, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllBylAdvancedSearch(
                advancedSearchClubProfile.getAgeFrom(),
                advancedSearchClubProfile.getAgeTo(),
                advancedSearchClubProfile.getCityName(),
                advancedSearchClubProfile.getDistrictName(),
                advancedSearchClubProfile.getStationName(),
                CategoryUtil.replaceSemicolonToComma(advancedSearchClubProfile.getCategoriesName()),
                advancedSearchClubProfile.getIsOnline(),
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
     * @param searchClubProfile -  put text of search (based on clubName, cityName & categoryName)
     * @return {@code Page<ClubResponse>}
     */
    @Override
    public Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllByParameters(
                searchClubProfile.getClubName(),
                searchClubProfile.getCityName(),
                searchClubProfile.getCategoryName(),
                searchClubProfile.getIsOnline(),
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
    public List<SearchPossibleResponse> getPossibleClubByName(String text, String cityName) {
        return clubRepository.findTop3ByName(text, cityName, PageRequest.of(0, 3))
                .stream()
                .map(category -> (SearchPossibleResponse) dtoConverter.convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * The method returns list of dto {@code Page<ClubResponse>} of all clubs by user-owner.
     *
     * @param id - put user id.
     * @return new {@code Page<ClubResponse>}.
     */
    @Override
    public Page<ClubResponse> getClubsByUserId(Long id, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllByUserId(id, pageable);

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) dtoConverter.convertToDto(club, ClubResponse.class))
                .collect(Collectors.toList()),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    public List<ClubResponse> getClubByCategoryAndCity(SearchClubProfile searchClubProfile) {
        List<Club> clubResponses = clubRepository.findAllClubsByParameters(
                searchClubProfile.getCityName(), searchClubProfile.getCategoryName());

        return clubResponses
                .stream()
                .map(club -> (ClubResponse) dtoConverter.convertToDto(club, ClubResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * The method returns dto {@code ClubResponse} of deleted club by id.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     * @throws DatabaseRepositoryException if club contain foreign keys.
     */
    @Override
    public ClubResponse deleteClubById(Long id) {
        Club club = getClubById(id);

        archiveService.saveModel(club);

        try {
            clubRepository.deleteById(id);
            clubRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CLUB_DELETING_ERROR);
        }

        log.info("club {} was successfully deleted", club);
        return dtoConverter.convertToDto(club, ClubResponse.class);
    }

    private boolean isClubExistByName(String name) {
        return clubRepository.existsByName(name);
    }

    private Optional<Club> getOptionalClubById(Long id) {
        return clubRepository.findById(id);
    }

    private Optional<Club> getOptionalClubByName(String name) {
        return clubRepository.findByName(name);
    }
}
