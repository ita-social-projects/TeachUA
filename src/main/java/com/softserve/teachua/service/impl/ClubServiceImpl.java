package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.ClubToClubResponseConverter;
import com.softserve.teachua.converter.CoordinatesConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.*;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.*;
import com.softserve.teachua.model.*;
import com.softserve.teachua.repository.*;
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

import javax.servlet.http.HttpServletRequest;
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
    private static final String CLUB_CREATING_ERROR = "Club without \"%s\" isn't created.";
    private static final String CLUB_CANT_BE_MANAGE_BY_USER = "A user cannot manage a club that does not belong to the user";

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
    private final FileUploadService fileUploadService;
    private final CoordinatesConverter coordinatesConverter;
    private final GalleryRepository galleryRepository;
    private final CenterService centerService;
    private final FeedbackRepository feedbackRepository;

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
                           ClubToClubResponseConverter toClubResponseConverter,
                           LocationService locationService,
                           FileUploadService fileUploadService,
                           CoordinatesConverter coordinatesConverter,
                           GalleryRepository galleryRepository, CenterService centerService, FeedbackRepository feedbackRepository) {

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
        this.fileUploadService = fileUploadService;
        this.coordinatesConverter = coordinatesConverter;
        this.galleryRepository = galleryRepository;
        this.centerService = centerService;
        this.feedbackRepository = feedbackRepository;
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
    public SuccessUpdatedClub updateClub(Long id, ClubResponse clubProfile, HttpServletRequest httpServletRequest) {
        validateClubOwner(id, httpServletRequest);
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
     * @throws IncorrectInputException if mandatory fields are empty.
     */
    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile, HttpServletRequest httpServletRequest) {
        List<LocationProfile> locations = clubProfile.getLocations();

        if (locations != null && !locations.isEmpty()) {
            for (LocationProfile profile : locations) {
                coordinatesConverter.LocationProfileConverterToDb(profile);
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
        if (!ifClubToCreateHaveEmptyFields(clubProfile).isEmpty()) {
            throw new IncorrectInputException(String.format(CLUB_CREATING_ERROR, ifClubToCreateHaveEmptyFields(clubProfile)));
        }

        if (isClubExistByName(clubProfile.getName())) {
            throw new AlreadyExistException(String.format(CLUB_ALREADY_EXIST, clubProfile.getName()));
        }

        User user = userService.getUserFromRequest(httpServletRequest);
        clubProfile.setUserId(user.getId());

        //todo delete or replace this block
        log.info("== add method");

        log.info("==clubService=?  clubProfile.centerID" + clubProfile.getCenterId());
        Club club = clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                .withCategories(clubProfile.getCategoriesName()
                        .stream()
                        .map(categoryService::getCategoryByName)
                        .collect(Collectors.toSet()))
                        .withRating(0d))
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

        List<GalleryPhotoProfile> galleryPhotos = clubProfile.getUrlGallery();
        if (galleryPhotos != null && !galleryPhotos.isEmpty()) {
            club.setUrlGallery(
                    galleryPhotos.stream()
                        .map(url -> galleryRepository.save(dtoConverter.convertToEntity(url, new GalleryPhoto()).
                                withClub(club).
                                withUrl(url.getUrlGallery())))
                        .collect(Collectors.toList())
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

//        log.info("getting list of clubs by user id {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getSimilarClubsByCategoryName(SimilarClubProfile similarClubProfile) {
        return clubRepository.findByCategoriesNames(
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

        if (advancedSearchClubProfile.getAge() != null &&
                (advancedSearchClubProfile.getAge() < 2 || advancedSearchClubProfile.getAge() > 18)) {
            throw new IncorrectInputException("Age should be from 2 to 18 years inclusive");
        }

        log.info("getAdvancedSearchClubs, advClubProf :" + advancedSearchClubProfile.toString());

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

        log.info("getClubsBySearchParameters ===> ");
        log.info(searchClubProfile.toString());

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
    public ClubResponse changeClubOwner(
            Long id,
            ClubOwnerProfile clubOwnerProfile,
            HttpServletRequest httpServletRequest
    ) {
        validateClubOwner(id, httpServletRequest);
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
    public ClubResponse deleteClubById(Long id, HttpServletRequest httpServletRequest) {
        validateClubOwner(id, httpServletRequest);

        Club club = getClubById(id);

        archiveService.saveModel(club);

        try {
            locationRepository.findAll()
                    .stream()
                    .filter(location -> location.getClub() != null && location.getClub().getId().equals(id))
                    .forEach(location -> locationService.addLocation(dtoConverter.convertToDto(location.withClub(null), LocationProfile.class)));
            fileUploadService.deleteImages(club.getUrlLogo(), club.getUrlBackground(), club.getUrlGallery());
            updateClub(id, dtoConverter.convertToDto(club.withLocations(null), ClubResponse.class), httpServletRequest);

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

    private String ifClubToCreateHaveEmptyFields(ClubProfile clubProfile) {
        if (clubProfile.getName() == null || clubProfile.getName().trim().isEmpty()) {
            return "Назва";
        }
        if (clubProfile.getContacts() == null || clubProfile.getContacts().trim().isEmpty()) {
            return "Контакти";
        }
        if (clubProfile.getDescription() == null || clubProfile.getDescription().trim().isEmpty()) {
            return "Опис";
        }
        if (clubProfile.getCategoriesName() == null || clubProfile.getCategoriesName().isEmpty()) {
            return "Категорії";
        }
        return "";
    }

    @Override
    public void validateClubOwner(Long id, HttpServletRequest httpServletRequest) {
        User userFromClub = getClubById(id).getUser();
        User userFromRequest = userService.getUserFromRequest(httpServletRequest);

        if(!(userFromClub != null && userFromRequest != null && userFromRequest.equals(userFromClub))) {
            throw new NotVerifiedUserException(CLUB_CANT_BE_MANAGE_BY_USER);
        }
    }

    @Override
    public SuccessUpdatedClub updateRatingNewFeedback(FeedbackResponse feedbackResponse){
        Club club = getClubById(feedbackResponse.getClub().getId());

        Long newFeedbackCount = club.getFeedbackCount() + 1;
        Double newRating = (club.getRating() * club.getFeedbackCount() +  feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public SuccessUpdatedClub updateRatingEditFeedback(
            FeedbackResponse previousFeedback,
            FeedbackResponse updatedFeedback
    ){
        Club club = getClubById(previousFeedback.getClub().getId());

        Double newRating =
                (club.getRating() * club.getFeedbackCount() - previousFeedback.getRate() + updatedFeedback.getRate())
                        / club.getFeedbackCount();

        return updateClubRating(club, newRating, club.getFeedbackCount());
    }

    @Override
    public SuccessUpdatedClub updateRatingDeleteFeedback(FeedbackResponse feedbackResponse){
        Club club = getClubById(feedbackResponse.getClub().getId());

        Long newFeedbackCount = club.getFeedbackCount() - 1;
        Double newRating = newFeedbackCount == 0 ? 0 :
                (club.getRating() * club.getFeedbackCount() -  feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public List<ClubResponse> updateRatingForAllClubs(){
        return getListOfClubs().stream().map(clubResponse -> {
           Club updClub = getClubById(clubResponse.getId());
           updClub.setRating(feedbackRepository.findAvgRating(clubResponse.getId()));
           updClub.setFeedbackCount(feedbackRepository.getAllByClubId(clubResponse.getId()).stream().count());
            clubRepository.save(updClub);
           return clubResponse;
        }).collect(Collectors.toList());
    }

    public SuccessUpdatedClub updateClubRating(Club club, Double raring, Long feedbackCount){
        ClubResponse previousClub = dtoConverter.convertToDto(club, ClubResponse.class);

        club.setRating(raring);
        club.setFeedbackCount(feedbackCount);
        Club updClub = clubRepository.save(club);

        centerService.updateRatingUpdateClub(
                previousClub,
                dtoConverter.convertToDto(updClub, ClubResponse.class)
        );

        return dtoConverter.convertToDto(updClub, SuccessUpdatedClub.class);
    }

}