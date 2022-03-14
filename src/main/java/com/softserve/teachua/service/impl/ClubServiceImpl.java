package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.softserve.teachua.converter.*;
import com.softserve.teachua.dto.club.*;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.*;
import com.softserve.teachua.model.*;
import com.softserve.teachua.model.archivable.ClubArch;
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

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class ClubServiceImpl implements ClubService, ArchiveMark<Club> {
    private static final String CLUB_ALREADY_EXIST = "Club already exist with name: %s";
    private static final String CLUB_NOT_FOUND_BY_ID = "Club not found by id: %s";
    private static final String CLUB_NOT_FOUND_BY_NAME = "Club not found by name: %s";
    private static final String CLUB_DELETING_ERROR = "Can't delete club cause of relationship";
    private static final String CLUB_CREATING_ERROR = "Club without \"%s\" isn't created.";
    private static final String CLUB_CANT_BE_MANAGE_BY_USER =
            "The user cannot manage a club that does not belong to the user";

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
    private final ObjectMapper objectMapper;
    private FeedbackService feedbackService;
    private final ContactsStringConverter contactsStringConverter;
    private final LocationResponsConvertToLocation locationResponsConvertToLocation;

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
                           GalleryRepository galleryRepository,
                           CenterService centerService,
                           FeedbackRepository feedbackRepository,
                           ObjectMapper objectMapper,
                           ContactsStringConverter contactsStringConverter, LocationResponsConvertToLocation locationResponsConvertToLocation) {
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
        this.objectMapper = objectMapper;
        this.contactsStringConverter = contactsStringConverter;
        this.locationResponsConvertToLocation = locationResponsConvertToLocation;
    }

    @Autowired
    public void setFeedbackService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public ClubResponse getClubProfileById(Long id) {
        return toClubResponseConverter.convertToClubResponse(getClubById(id));
    }

    @Override
    public Club getClubById(Long id) {
        Optional<Club> optionalClub = getOptionalClubById(id);
        if (!optionalClub.isPresent()) {
            throw new NotExistException(String.format(CLUB_NOT_FOUND_BY_ID, id));
        }

        Club club = optionalClub.get();

        log.debug("getting club by id {}", id);
        return club;
    }

    @Override
    public List<Club> getClubByClubExternalId(Long clubExternalId) {
        List<Club> clubs = clubRepository.findClubByClubExternalId(clubExternalId);
        log.debug("getting club by external id {}", clubExternalId);
        return clubs;
    }

    @Override
    public Club getClubByName(String name) {
        Optional<Club> optionalClub = getOptionalClubByName(name);
        if (!optionalClub.isPresent()) {
            throw new NotExistException(String.format(CLUB_NOT_FOUND_BY_NAME, name));
        }

        Club club = optionalClub.get();
        log.debug("getting club by name {}", club.getName());
        return club;
    }

    @Override
    public SuccessUpdatedClub updateClub(Long id, ClubResponse clubResponse) {
        User user = userService.getCurrentUser();
        validateClubOwner(id, user);
        Club club = getClubById(id);
        Set<LocationResponse> locations = null;

        if (clubResponse.getLocations() != null) {
            locations = new HashSet<>(clubResponse.getLocations());
            if (!locations.isEmpty()) {
                for (LocationResponse profile : locations) {
                    coordinatesConverter.locationResponseConverterToDb(profile);
                    if (profile.getCityName() != null && !profile.getCityName().isEmpty()) {
                        profile.setCityId(cityService.getCityByName(profile.getCityName()).getId());
                    }
                    if (profile.getDistrictName() != null && !profile.getDistrictName().isEmpty()) {
                        profile.setDistrictId(districtService.getDistrictByName(profile.getDistrictName()).getId());
                    }
                    if (profile.getStationName() != null && !profile.getStationName().isEmpty()) {
                        profile.setStationId(stationService.getStationByName(profile.getStationName()).getId());
                    }
                    profile.setClubId(id);
                }
            }
        }
        Club newClub = dtoConverter.convertToEntity(clubResponse, club)
                .withId(id)
                .withContacts(contactsStringConverter.convertContactDataResponseToString(clubResponse.getContacts()))
                .withLocations(locationService.updateLocationByClub(locations, club));
//
//        Club newClub = Club.builder()
//                .id(clubResponse.getId())
//                .name(clubResponse.getName())
//                .description(clubResponse.getDescription())
//                .urlWeb(clubResponse.getUrlWeb())
//                .urlLogo(clubResponse.getUrlLogo())
//                .urlBackground(clubResponse.getUrlBackground())
//                .workTime(clubResponse.getWorkTime())
//                .rating(clubResponse.getRating())
//                .feedbackCount(clubResponse.getFeedbackCount())
//                .isOnline(clubResponse.getIsOnline())
//                .isApproved(clubResponse.getIsApproved())
//                .ageFrom(clubResponse.getAgeFrom())
//                .ageTo(clubResponse.getAgeTo())
////                .categories(dtoConverter.convertToEntity(clubResponse.getCategories(),))
//                .build();


        log.debug("updating club by id {}", newClub);
        return dtoConverter.convertToDto(clubRepository.save(newClub), SuccessUpdatedClub.class);
    }

    @Override
    public ClubResponse getClubProfileByName(String name) {
        return toClubResponseConverter.convertToClubResponse(getClubByName(name));
    }

    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        List<LocationProfile> locations = clubProfile.getLocations();

        if (locations != null && !locations.isEmpty()) {
            for (LocationProfile profile : locations) {
                coordinatesConverter.locationProfileConverterToDb(profile);
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

        User user = userService.getCurrentUser();
        clubProfile.setUserId(user.getId());

        //todo delete or replace this block
        log.debug("== add method");

        log.debug("==clubService=?  clubProfile.centerID" + clubProfile.getCenterId());
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
                            .map(url -> galleryRepository.save(dtoConverter.convertToEntity(url, new GalleryPhoto())
                                    .withClub(club)
                                    .withUrl(url.getUrlGallery())))
                            .collect(Collectors.toList())
            );
        }
        log.debug("adding club with name : {}", clubProfile.getName());
        return dtoConverter.convertToDto(club, SuccessCreatedClub.class);
    }

    @Override
    public Club addClubsFromExcel(ClubProfile clubProfile) {
        if (clubProfile.getCenterId() == null) {
            log.debug("(row 256, ClubServiceImpl)  addClubsFromExcel => "
                    + clubProfile.getCenterExternalId() + " not found");

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
                log.debug("(row 268, ClubServiceImpl)    saving club ");
                log.debug(e.getMessage());

                return new Club();
            }
        } else {
            Center center = centerRepository.findById(clubProfile.getCenterId()).get();
            log.debug("(clubServiceImpl) ==>  addClubsFromExcel = >  with EXTERNAL_center_id ="
                    + center.getCenterExternalId());
            log.debug("addClubsFromExcel => " + clubProfile.getCenterId() + " with real center, id =" + center.getId());
            return clubRepository.save(dtoConverter.convertToEntity(clubProfile, new Club())
                            .withCategories(clubProfile.getCategoriesName()
                                    .stream()
                                    .map(categoryService::getCategoryByName)
                                    .collect(Collectors.toSet())))
                    .withUser(null)
                    .withCenter(center);
        }
    }

    @Override
    public List<ClubResponse> getListOfClubs() {
        List<ClubResponse> clubResponses = clubRepository.findAll()
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList());

        log.debug("getting list of clubs {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getListClubsByUserId(Long id) {
        List<ClubResponse> clubResponses = clubRepository.findAllByUserId(id)
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList());
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

    @Override
    public Page<ClubResponse> getAdvancedSearchClubs(
            AdvancedSearchClubProfile advancedSearchClubProfile, Pageable pageable) {
        if (advancedSearchClubProfile.getAge() != null
                && (advancedSearchClubProfile.getAge() < 2 || advancedSearchClubProfile.getAge() > 18)) {
            throw new IncorrectInputException("Age should be from 2 to 18 years inclusive");
        }

        log.debug("getAdvancedSearchClubs, advClubProf :" + advancedSearchClubProfile.toString());

        Page<Club> clubResponses = clubRepository.findAllBylAdvancedSearch(
                advancedSearchClubProfile.getName(),
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

    @Override
    public Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable) {
        log.debug("getClubsBySearchParameters ===> ");
        log.debug(searchClubProfile.toString());

        Page<Club> clubResponses = clubRepository.findAllByParameters(
                searchClubProfile.getClubName(),
                searchClubProfile.getCityName(),
                searchClubProfile.getCategoryName(),
                searchClubProfile.getIsOnline(),
                pageable);


        log.debug("===find clubs : " + clubResponses.getNumberOfElements());

        if (clubResponses.getNumberOfElements() == 0) {
            log.debug("==============================");
            log.debug("clubResponses by club name is empty==> start search by center name "
                    + searchClubProfile.getClubName());
            clubResponses = clubRepository
                    .findClubsByCenterName(searchClubProfile.getClubName(),
                            searchClubProfile.getCityName(), pageable);
            log.debug("result of search by centerName : " + clubResponses.getNumberOfElements());
            log.debug(clubResponses.toString());
        }

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList()),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
    public List<SearchPossibleResponse> getPossibleClubByName(String text, String cityName) {
        return clubRepository.findTop3ByName(text, cityName, PageRequest.of(0, 3))
                .stream()
                .map(category -> (SearchPossibleResponse) dtoConverter
                        .convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClubResponse> getClubsByUserId(Long id, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllByUserId(id, pageable);

        return new PageImpl<>(clubResponses
                .stream()
                .map(club -> (ClubResponse) toClubResponseConverter.convertToClubResponse(club))
                .collect(Collectors.toList()),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
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
            ClubOwnerProfile clubOwnerProfile
    ) {
        User user = userService.getCurrentUser();
        validateClubOwner(id, user);
        Club club = getClubById(id);
        club.setUser(clubOwnerProfile.getUser());

        log.debug("changed club owner by id {}", club);
        return dtoConverter.convertToDto(clubRepository.save(club), ClubResponse.class);
    }

    @Override
    public void updateContacts() {
        List<Center> centers = centerRepository.findAll();
        List<Center> updatedCenters = centers
                .stream()
                .filter(center -> center.getContacts()!=null)
                .filter(center -> !this.isValidJSON(center.getContacts()))
                .peek(center -> {
                    JsonNodeFactory factory = JsonNodeFactory.instance;
                    if (center.getContacts().startsWith("{")) {
                        String contacts = center.getContacts().replace("::", ":");
                        ObjectNode node = (ObjectNode) toJSON(contacts);

                        if (node.has("1")) {
                            ArrayNode array = factory.arrayNode();
                            array.add(convert(node.get("1").asText()));
                            node.set("1", array);
                        }

                        center.setContacts(node.toString());
                    } else {
                        ObjectNode json = factory.objectNode();
                        Stream.of(center.getContacts().split(","))
                                .map(String::trim)
                                .filter(contact -> !contact.isEmpty())
                                .filter(contact -> !contact.endsWith("::"))
                                .map(contact -> contact.split("::"))
                                .forEach(contact -> {
                                    String key = contact[0];
                                    String value = contact[1];

                                    if (key.equals("1")) {
                                        ArrayNode array = json.has(key)
                                                ? (ArrayNode) json.get(key)
                                                : factory.arrayNode();

                                        String convertedValue = convert(value);

                                        if (convertedValue.equals("+3804442732290443600106")) {
                                            array.add("+380444273229");
                                            array.add("+380443600106");
                                        } else {
                                            array.add(convertedValue);
                                        }

                                        json.set(key, array);
                                    } else {
                                        json.put(key, value);
                                    }
                                });

                        center.setContacts(json.toString());
                    }
                    log.info(center.getContacts());
                })
                .collect(Collectors.toList());
        updatedCenters.forEach((center -> centerRepository.save(center)));


        List<Club> clubs = clubRepository.findAll();
        List<Club> updatedClubs = clubs
                .stream()
                .filter(club -> club.getContacts()!=null)
                .filter((club) -> !this.isValidJSON(club.getContacts()))
                .peek((club) -> {
                    JsonNodeFactory factory = JsonNodeFactory.instance;
                    if (club.getContacts().startsWith("{")) {
                        String contacts = club.getContacts().replace("::", ":");
                        ObjectNode node = (ObjectNode) toJSON(contacts);

                        if (node.has("1")) {
                            ArrayNode array = factory.arrayNode();
                            array.add(convert(node.get("1").asText()));
                            node.set("1", array);
                        }

                        club.setContacts(node.toString());
                    } else {
                        ObjectNode json = factory.objectNode();

                        Stream.of(club.getContacts().split(","))
                                .map(String::trim)
                                .filter(contact -> !contact.isEmpty())
                                .filter(contact -> !contact.endsWith("::"))
                                .map(contact -> contact.split("::"))
                                .forEach(contact -> {
                                    String key = contact[0];
                                    String value = contact[1];

                                    if (key.equals("1")) {
                                        ArrayNode array = json.has(key)
                                                ? (ArrayNode) json.get(key)
                                                : factory.arrayNode();

                                        String convertedValue = convert(value);

                                        if (convertedValue.equals("+380950993545093138461606328812020958114277")) {
                                            array.add("+380950993545");
                                            array.add("+380931384616");
                                            array.add("+380632881202");
                                            array.add("+380958114277");
                                        } else if (convertedValue.equals("+38044517699704451761880445178279")) {
                                            array.add("+380445176997");
                                            array.add("+380445176188");
                                            array.add("+380445178279");
                                        } else if (convertedValue.equals("+38044599612309640318")) {
                                            array.add("+380445996123");
                                        } else if (convertedValue.equals("+3804456499930445749818")) {
                                            array.add("+380445649993");
                                            array.add("+380445749818");
                                        } else if (convertedValue.equals("+3804456462130445608993")) {
                                            array.add("+380445646213");
                                            array.add("+380445608993");
                                        } else if (convertedValue.equals("+3804456254940445640218")) {
                                            array.add("+380445625494");
                                            array.add("+380445640218");
                                        } else if (convertedValue.equals("+380938380570994517940")) {
                                            array.add("+380938380570");
                                            array.add("+380994517940");
                                        } else {
                                            array.add(convertedValue);
                                        }

                                        json.set(key, array);
                                    } else {
                                        json.put(key, value);
                                    }
                                });

                        club.setContacts(json.toString());
                    }

                    log.info(club.getContacts());
                })
                .collect(Collectors.toList());
        updatedClubs.forEach((club -> clubRepository.save(club)));
    }

    private JsonNode toJSON(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private boolean isValidJSON(final String json) {
        JsonNode jsonNode = toJSON(json);
        return jsonNode != null;}

    private String convert(String value) {
        String updatedValue = value.replaceAll("\\D", "");

        if (updatedValue.startsWith("+380")) {
            return updatedValue;
        } else if (updatedValue.startsWith("0")) {
            updatedValue = updatedValue.replaceFirst("0", "+380");
        } else if (updatedValue.startsWith("380")) {
            updatedValue = updatedValue.replaceFirst("3", "+3");
        } else if (updatedValue.startsWith("800")) {
            updatedValue = updatedValue.replaceFirst("8", "+3808");
        } else {
            updatedValue = "+380" + updatedValue;
        }

        return updatedValue;
    }

    @Override
    public ClubResponse deleteClubById(Long id) {
        User user = userService.getCurrentUser();
        validateClubOwner(id, user);

        Club club = getClubById(id);

        try {
            club.getFeedbacks()
                    .stream().forEach(feedback -> {
                feedback.setClub(null);
                feedbackRepository.save(feedback);
            });

            club.getLocations()
                    .stream().forEach(location -> {
                        location.setClub(null);
                        locationRepository.save(location);
                    });

            club.getUrlGallery()
                    .stream().forEach(urlGallery -> {
                        Optional<GalleryPhoto> galleryPhoto = galleryRepository.findById(urlGallery.getId());
                        if (galleryPhoto.isPresent()){
                            galleryPhoto.get().setClub(null);
                            galleryRepository.save(galleryPhoto.get());
                        }
                    });


//            fileUploadService.deleteImages(club.getUrlLogo(), club.getUrlBackground(), club.getUrlGallery());

            clubRepository.deleteById(id);
            clubRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CLUB_DELETING_ERROR);
        }

        archiveModel(club);

        log.debug("club {} was successfully deleted", club);
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

    @Override
    public void validateClubOwner(Long id, User userFromRequest) {
        User userFromClub = getClubById(id).getUser();

        if (!(userFromClub != null && userFromRequest != null && userFromRequest.equals(userFromClub))) {
            throw new NotVerifiedUserException(CLUB_CANT_BE_MANAGE_BY_USER);
        }
    }

    @Override
    public SuccessUpdatedClub updateRatingNewFeedback(FeedbackResponse feedbackResponse) {
        Club club = getClubById(feedbackResponse.getClub().getId());

        Long newFeedbackCount = club.getFeedbackCount() + 1;
        Double newRating = (club.getRating() * club.getFeedbackCount() + feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public SuccessUpdatedClub updateRatingEditFeedback(
            FeedbackResponse previousFeedback,
            FeedbackResponse updatedFeedback) {
        Club club = getClubById(previousFeedback.getClub().getId());

        Double newRating =
                (club.getRating() * club.getFeedbackCount() - previousFeedback.getRate() + updatedFeedback.getRate())
                        / club.getFeedbackCount();

        return updateClubRating(club, newRating, club.getFeedbackCount());
    }

    @Override
    public SuccessUpdatedClub updateRatingDeleteFeedback(FeedbackResponse feedbackResponse) {
        Club club = getClubById(feedbackResponse.getClub().getId());

        Long newFeedbackCount = club.getFeedbackCount() - 1;
        Double newRating = newFeedbackCount == 0 ? 0 :
                (club.getRating() * club.getFeedbackCount() - feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public List<ClubResponse> updateRatingForAllClubs() {
        return getListOfClubs().stream().map(clubResponse -> {
            Club updClub = getClubById(clubResponse.getId());
            updClub.setRating(feedbackRepository.findAvgRating(clubResponse.getId()));
            updClub.setFeedbackCount(feedbackRepository.getAllByClubId(clubResponse.getId()).stream().count());
            clubRepository.save(updClub);
            return clubResponse;
        }).collect(Collectors.toList());
    }

    private SuccessUpdatedClub updateClubRating(Club club, Double raring, Long feedbackCount) {
        ClubResponse previousClub = dtoConverter.convertToDto(club, ClubResponse.class);

        club.setRating(raring);
        club.setFeedbackCount(feedbackCount);
        Club updClub = clubRepository.save(club);

        if (updClub.getCenter() != null) {
            centerService.updateRatingUpdateClub(
                    previousClub,
                    dtoConverter.convertToDto(updClub, ClubResponse.class)
            );
        }

        return dtoConverter.convertToDto(updClub, SuccessUpdatedClub.class);
    }

    @Override
    public void archiveModel(Club club) {
        ClubArch clubArch = dtoConverter.convertToDto(club, ClubArch.class);
        clubArch.setUrlGalleriesIds(club.getUrlGallery().stream().map(GalleryPhoto::getId).collect(Collectors.toList()));
        clubArch.setLocationsIds(club.getLocations().stream().map(Location::getId).collect(Collectors.toSet()));
        clubArch.setCategoriesIds(club.getCategories().stream().map(Category::getId).collect(Collectors.toSet()));
        clubArch.setFeedbacksIds(club.getFeedbacks().stream().map(Feedback::getId).collect(Collectors.toSet()));
        archiveService.saveModel(clubArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ClubArch clubArch = objectMapper.readValue(archiveObject, ClubArch.class);
        Club club = Club.builder().build();
        club = dtoConverter.convertToEntity(clubArch, club)
                .withId(null)
                .withCategories(clubArch.getCategoriesIds()
                        .stream().map(categoryService::getCategoryById).collect(Collectors.toSet()))
                .withLocations(clubArch.getLocationsIds()
                        .stream().map(locationService::getLocationById).collect(Collectors.toSet()))
                .withUrlGallery(clubArch.getUrlGalleriesIds()
                        .stream().map(galleryRepository::findById).filter(Optional::isPresent)
                        .map(Optional::get).collect(Collectors.toList()))
                .withFeedbacks(clubArch.getFeedbacksIds()
                        .stream().map(feedbackService::getFeedbackById).collect(Collectors.toSet()));
        if(Optional.ofNullable(clubArch.getCenterId()).isPresent()){
            club.setCenter(centerService.getCenterById(clubArch.getCenterId()));
        }
        if(Optional.ofNullable(clubArch.getUserId()).isPresent()){
            club.setUser(userService.getUserById(clubArch.getUserId()));
        }
        Club finalClub = clubRepository.save(club);
        club.getLocations().forEach(location -> location.setClub(finalClub));
        club.getUrlGallery().forEach(galleryPhoto -> galleryPhoto.setClub(finalClub));
        club.getFeedbacks().forEach(feedback -> feedback.setClub(finalClub));

    }
}
