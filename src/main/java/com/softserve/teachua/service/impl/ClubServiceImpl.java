package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.ClubToClubResponseConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.*;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.*;
import com.softserve.teachua.repository.CenterRepository;
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
    private final ClubToClubResponseConverter toClubResponseConverter;
    private final ArchiveService archiveService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CenterRepository centerRepository;
    private final LocationService locationService;


    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository,
                           CenterRepository centerRepository,
                           LocationRepository locationRepository,
                           DtoConverter dtoConverter,
                           ArchiveService archiveService,
                           CityService cityService,
                           DistrictService districtService,
                           StationService stationService,
                           CategoryService categoryService,
                           UserService userService,
                           ClubToClubResponseConverter toClubResponseConverter, LocationService locationService) {
        this.clubRepository = clubRepository;
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.toClubResponseConverter = toClubResponseConverter;
        this.centerRepository = centerRepository;
        this.locationService = locationService;
    }

    /**
     * The method returns dto {@code ClubResponse} of club by id.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @Override
    public ClubResponse getClubProfileById(Long id) {
        return toClubResponseConverter.convertToClubResponse(getClubById(id));
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
     * The method returns entity {@code Club} of club by id.
     *
     * @param clubExternalId - put club id.
     * @return new {@code Club}.
     * @throws NotExistException if club not exists.
     */
    @Override
    public List<Club> getClubByClubExternalId(Long clubExternalId) {
        List<Club> clubs = clubRepository.findClubByClubExternalId(clubExternalId);
        log.info("getting club by external id {}", clubExternalId);
        return clubs;
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
    public SuccessUpdatedClub updateClub(Long id, ClubResponse clubProfile) {
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

        return toClubResponseConverter.convertToClubResponse(getClubByName(name));
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
        List<LocationProfile> locations = clubProfile.getLocations();
        if (locations != null && !locations.isEmpty()) {
            for (LocationProfile profile : locations) {
                if (profile.getCityName() != null && !profile.getCityName().isEmpty()) {
                    profile.setCityId(cityService.getCityByName(profile.getCityName()).getId());
                }
                if (profile.getDistrictName() != null && !profile.getDistrictName().isEmpty()) {
                    profile.setDistrictId(districtService.getDistrictByName(profile.getDistrictName()).getId());
                }
                if (profile.getStationName() != null && !profile.getStationName().isEmpty()) {
                    profile.setStationId(stationService.getStationByName(profile.getStationName()).getId());
                }
            }
        }

        if (isClubExistByName(clubProfile.getName())) {
            throw new AlreadyExistException(String.format(CLUB_ALREADY_EXIST, clubProfile.getName()));
        }

        User user = null;
        if (clubProfile.getUserId() != null) {
            user = userService.getUserById(clubProfile.getUserId());
        }

        //todo delete or replace this block
        log.info("== add method");

        log.info("==clubService=?  clubProfile.centerID" + clubProfile.getCenterId());
        Club club = clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                .withCategories(clubProfile.getCategoriesName()
                        .stream()
                        .map(categoryService::getCategoryByName)
                        .collect(Collectors.toSet())))
                .withUser(user);

        if (locations != null && !locations.isEmpty()) {
            club.setLocations(
                    clubProfile.getLocations()
                            .stream()
                            .map(locationProfile -> locationRepository.save(
                                    dtoConverter.convertToEntity(locationProfile, new Location())
                                            .withClub(club)
                                            .withCity(cityService.getCityById(locationProfile.getCityId()))
                                            .withDistrict(districtService.getDistrictById(
                                                    locationProfile.getDistrictId())
                                            )
                                            .withStation(stationService.getStationById(
                                                    locationProfile.getStationId()))
                            ))
                            .collect(Collectors.toSet())
            );
        }
        log.info("adding club with name : {}", clubProfile.getName());
        return dtoConverter.convertToDto(club, SuccessCreatedClub.class);
    }

    @Override
    public Club addClubsFromExcel(ClubProfile clubProfile) {

        if (clubProfile.getCenterId() == null) {
            log.info("(row 256, ClubServiceImpl)  addClubsFromExcel => " + clubProfile.getCenterExternalId() + " not found");

            try {
                return clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                        .withCategories(clubProfile.getCategoriesName()
                                .stream()
                                .map(categoryService::getCategoryByName)
                                .collect(Collectors.toSet())))
                        .withUser(null)
                        .withCenter(null);
            } catch (Exception e) {
                //todo bad solution .... do refactor !!!!!
                log.info("(row 268, ClubServiceImpl)    saving club ");
                log.info(e.getMessage());

                return new Club();
            }

        } else {
            Center center = centerRepository.findById(clubProfile.getCenterId()).get();
            log.info("(clubServiceImpl) ==>  addClubsFromExcel = >  with EXTERNAL_center_id =" + center.getCenterExternalId());
            log.info("addClubsFromExcel => " + clubProfile.getCenterId() + " with real center , id =" + center.getId());
            return clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                    .withCategories(clubProfile.getCategoriesName()
                            .stream()
                            .map(categoryService::getCategoryByName)
                            .collect(Collectors.toSet())))
                    .withUser(null)
                    .withCenter(center);
        }
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
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList());

        log.info("getting list of clubs {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getListClubsByUserId(Long id) {
        List<ClubResponse> clubResponses = clubRepository.findAllByUserId(id)
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList());

        log.info("getting list of clubs by user id {}", clubResponses);
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
                .map(category -> (ClubResponse) toClubResponseConverter.convertToClubResponse(category))
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
                advancedSearchClubProfile.getAge(),
                advancedSearchClubProfile.getCityName(),
                advancedSearchClubProfile.getDistrictName(),
                advancedSearchClubProfile.getStationName(),
                CategoryUtil.replaceSemicolonToComma(advancedSearchClubProfile.getCategoriesName()),
                advancedSearchClubProfile.getIsOnline(),
                pageable);

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
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

        log.info("===find clubs : " + clubResponses.getNumberOfElements());

        if (clubResponses.getNumberOfElements() == 0) {
            log.info("==============================");
            log.info("clubResponses by club name is empty==> start search by center name " + searchClubProfile.getClubName());
            clubResponses = clubRepository.
                    findClubsByCenterName(searchClubProfile.getClubName(),
                            searchClubProfile.getCityName(), pageable);
            log.info("result of search by centerName : " + clubResponses.getNumberOfElements());
            log.info(clubResponses.toString());
        }

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
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
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList()),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    public List<ClubResponse> getClubByCategoryAndCity(SearchClubProfile searchClubProfile) {
        List<Club> clubResponses = clubRepository.findAllClubsByParameters(
                searchClubProfile.getCityName(), searchClubProfile.getCategoryName());

        return clubResponses
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList());
    }

    @Override
    public ClubResponse changeClubOwner(Long id, ClubOwnerProfile clubOwnerProfile) {
        Club club = getClubById(id);
        club.setUser(clubOwnerProfile.getUser());

        log.info("changed club owner by id {}", club);
        return dtoConverter.convertToDto(clubRepository.save(club), ClubResponse.class);
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
            locationRepository.findAll()
                    .stream()
                    .filter(location -> location.getClub() != null && location.getClub().getId().equals(id))
                    .forEach(location -> locationService.addLocation(dtoConverter.convertToDto(location.withClub(null), LocationProfile.class)));

            updateClub(id, dtoConverter.convertToDto(club.withLocations(null), ClubResponse.class));

            clubRepository.deleteById(id);
            clubRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CLUB_DELETING_ERROR);
        }

        log.info("club {} was successfully deleted", club);
        return toClubResponseConverter.convertToClubResponse(club);
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