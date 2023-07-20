package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.softserve.teachua.converter.ClubToClubResponseConverter;
import com.softserve.teachua.converter.ContactsStringConverter;
import com.softserve.teachua.converter.CoordinatesConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.club.ClubOwnerProfile;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.dto.search.TopClubProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.ClubArch;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ComplaintRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CategoryService;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.service.FeedbackService;
import com.softserve.teachua.service.LocationService;
import com.softserve.teachua.service.StationService;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.utils.CategoryUtil;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class ClubServiceImpl implements ClubService, ArchiveMark<Club> {
    private static final String CLUB_ALREADY_EXIST = "Club already exist with name: %s";
    private static final String CLUB_NOT_FOUND_BY_ID = "Club not found by id: %s";
    private static final String CLUB_NOT_FOUND_BY_NAME = "Club not found by name: %s";
    private static final String CLUB_DELETING_ERROR = "Can't delete club cause of relationship";
    private static final String CLUB_CANT_BE_MANAGE_BY_USER =
            "The user cannot manage a club that does not belong to the user";
    private final ComplaintRepository complaintRepository;
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
    private final CoordinatesConverter coordinatesConverter;
    private final GalleryRepository galleryRepository;
    private final CenterService centerService;
    private final FeedbackRepository feedbackRepository;
    private final ObjectMapper objectMapper;
    private final ContactsStringConverter contactsStringConverter;
    private FeedbackService feedbackService;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, CenterRepository centerRepository,
                           LocationRepository locationRepository, DtoConverter dtoConverter,
                           ArchiveService archiveService, CityService cityService, DistrictService districtService,
                           StationService stationService, CategoryService categoryService, UserService userService,
                           ClubToClubResponseConverter toClubResponseConverter, LocationService locationService,
                           CoordinatesConverter coordinatesConverter, GalleryRepository galleryRepository,
                           CenterService centerService, FeedbackRepository feedbackRepository,
                           ObjectMapper objectMapper, ContactsStringConverter contactsStringConverter,
                           ComplaintRepository complaintRepository) {
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
        this.coordinatesConverter = coordinatesConverter;
        this.galleryRepository = galleryRepository;
        this.centerService = centerService;
        this.feedbackRepository = feedbackRepository;
        this.objectMapper = objectMapper;
        this.contactsStringConverter = contactsStringConverter;
        this.complaintRepository = complaintRepository;
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
        if (optionalClub.isEmpty()) {
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
        if (optionalClub.isEmpty()) {
            throw new NotExistException(String.format(CLUB_NOT_FOUND_BY_NAME, name));
        }

        Club club = optionalClub.get();
        log.debug("getting club by name {}", club.getName());
        return club;
    }

    @Override
    @Transactional
    public SuccessUpdatedClub updateClub(Long id, ClubResponse clubResponse) {
        User user = userService.getAuthenticatedUser();
        validateClubOwner(id, user);
        Club club = getClubById(id);
        Set<LocationProfile> locations = null;

        if (clubResponse.getLocations() != null) {
            locations = new HashSet<>(clubResponse.getLocations());
            if (!locations.isEmpty()) {
                for (LocationProfile profile : locations) {
                    updateLocation(profile);
                    profile.setClubId(id);
                }
            }
        }

        Center center = null;
        if (clubResponse.getCenter() != null && clubResponse.getCenter().getId() != null) {
            center = centerService.getCenterById(clubResponse.getCenter().getId());
        }

        Club updatedClub = dtoConverter.convertToEntity(clubResponse, club).withId(id)
                .withCategories(
                        clubResponse.getCategories().stream().map(CategoryResponse::getName)
                                .map(categoryService::getCategoryByName).collect(Collectors.toSet()))
                .withContacts(contactsStringConverter.convertContactDataResponseToString(clubResponse.getContacts()))
                .withLocations(locationService.updateLocationByClub(locations, club)).withCenter(center);

        List<GalleryPhoto> galleryPhotos = clubResponse.getUrlGallery();
        if (galleryPhotos != null && !galleryPhotos.isEmpty()) {
            galleryRepository.deleteAllByClubId(clubResponse.getId());
            updatedClub.setUrlGallery(galleryPhotos.stream().map(photo -> galleryRepository.save(
                            new GalleryPhoto().withUrl(photo.getUrl()).withClub(updatedClub)))
                    .toList());
        }

        log.debug("updating club by id {}", updatedClub);
        return dtoConverter.convertToDto(clubRepository.save(updatedClub), SuccessUpdatedClub.class);
    }

    private void updateLocation(LocationProfile profile) {
        coordinatesConverter.locationProfileConverterToDb(profile);
        if (StringUtils.isNotEmpty(profile.getCityName())) {
            profile.setCityId(cityService.getCityByName(profile.getCityName()).getId());
        }
        if (StringUtils.isNotEmpty(profile.getDistrictName())) {
            profile.setDistrictId(districtService.getDistrictByName(profile.getDistrictName()).getId());
        }
        if (StringUtils.isNotEmpty(profile.getStationName())) {
            profile.setStationId(stationService.getStationByName(profile.getStationName()).getId());
        }
    }

    @Override
    public ClubResponse getClubProfileByName(String name) {
        return toClubResponseConverter.convertToClubResponse(getClubByName(name));
    }

    @Override
    public SuccessCreatedClub addClub(ClubProfile clubProfile) {
        if (isClubExistByName(clubProfile.getName())) {
            throw new AlreadyExistException(String.format(CLUB_ALREADY_EXIST, clubProfile.getName()));
        }

        Center center = null;
        if (clubProfile.getCenterId() != null) {
            center = centerService.getCenterById(clubProfile.getCenterId());
        }

        List<LocationProfile> locations = clubProfile.getLocations();

        if (locations != null && !locations.isEmpty()) {
            for (LocationProfile profile : locations) {
                updateLocation(profile);
            }
        }

        User user = userService.getAuthenticatedUser();
        clubProfile.setUserId(user.getId());

        Club club = clubRepository.save(
                dtoConverter.convertToEntity(clubProfile, new Club())
                        .withCategories(clubProfile.getCategoriesName().stream()
                                .map(categoryService::getCategoryByName)
                                .collect(Collectors.toSet()))
                        .withRating(0d).withUser(user).withCenter(center));

        if (locations != null && !locations.isEmpty()) {
            club.setLocations(
                    clubProfile.getLocations().stream()
                            .map(locationProfile -> locationRepository.save(dtoConverter
                                    .convertToEntity(locationProfile, new Location()).withClub(club)
                                    .withCity(cityService.getCityById(locationProfile.getCityId()))
                                    .withDistrict(districtService.getDistrictById(locationProfile.getDistrictId()))
                                    .withStation(stationService.getStationById(locationProfile.getStationId()))))
                            .collect(Collectors.toSet()));
        }

        List<GalleryPhotoProfile> galleryPhotos = clubProfile.getUrlGallery();
        if (galleryPhotos != null && !galleryPhotos.isEmpty()) {
            club.setUrlGallery(galleryPhotos.stream()
                    .map(url -> galleryRepository.save(dtoConverter.convertToEntity(url,
                            new GalleryPhoto()).withClub(club).withUrl(url.getUrlGallery())))
                    .toList());
        }
        log.debug("adding club with name : {}", clubProfile.getName());
        return dtoConverter.convertToDto(club, SuccessCreatedClub.class);
    }

    @Override
    public Club addClubsFromExcel(ClubProfile clubProfile) {
        if (clubProfile.getCenterId() == null) {
            log.debug("(row 256, ClubServiceImpl)  addClubsFromExcel => " + clubProfile.getCenterExternalId()
                    + " not found");

            try {
                return clubRepository
                        .save(dtoConverter.convertToEntity(clubProfile, new Club())
                                .withCategories(clubProfile.getCategoriesName().stream()
                                        .map(categoryService::getCategoryByName).collect(Collectors.toSet())))
                        .withUser(null).withCenter(null);
            } catch (Exception e) {
                log.debug("(row 268, ClubServiceImpl)    saving club ");
                log.debug(e.getMessage());

                return new Club();
            }
        } else {
            Center center = centerRepository.findById(clubProfile.getCenterId()).orElseThrow(NotExistException::new);
            log.debug("(clubServiceImpl) ==>  addClubsFromExcel = >  with EXTERNAL_center_id ="
                    + center.getCenterExternalId());
            log.debug("addClubsFromExcel => " + clubProfile.getCenterId() + " with real center, id =" + center.getId());
            return clubRepository
                    .save(dtoConverter.convertToEntity(clubProfile, new Club())
                            .withCategories(clubProfile.getCategoriesName().stream()
                                    .map(categoryService::getCategoryByName).collect(Collectors.toSet())))
                    .withUser(null).withCenter(center);
        }
    }

    @Override
    public List<ClubResponse> getListOfClubs() {
        List<ClubResponse> clubResponses = clubRepository.findAll().stream()
                .map(toClubResponseConverter::convertToClubResponse)
                .toList();

        log.debug("getting list of clubs {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getListOfClubsByCenterId(long centerId) {
        List<ClubResponse> clubResponses = clubRepository.findClubsByCenterId(centerId).stream()
                .map(toClubResponseConverter::convertToClubResponse)
                .toList();
        log.debug("getting list of clubs {}", clubResponses);
        return clubResponses;
    }

    @Override
    public List<ClubResponse> getListClubsByUserId(Long id) {
        return clubRepository.findAllByUserId(id).stream()
                .map(toClubResponseConverter::convertToClubResponse)
                .toList();
    }

    @Override
    public List<ClubResponse> getSimilarClubsByCategoryName(SimilarClubProfile similarClubProfile) {
        return clubRepository
                .findByCategoriesNames(similarClubProfile.getId(),
                        CategoryUtil.replaceSemicolonToComma(similarClubProfile.getCategoriesName()),
                        similarClubProfile.getCityName(), PageRequest.of(0, 2))
                .stream().map(toClubResponseConverter::convertToClubResponse)
                .toList();
    }

    @Override
    public Page<ClubResponse> getAdvancedSearchClubs(AdvancedSearchClubProfile advancedSearchClubProfile,
                                                     Pageable pageable) {
        if (advancedSearchClubProfile.getAge() != null
                && (advancedSearchClubProfile.getAge() < 2 || advancedSearchClubProfile.getAge() > 18)) {
            throw new IncorrectInputException("Age should be from 2 to 18 years inclusive");
        }
        if (advancedSearchClubProfile.getCategoriesName() == null) {
            advancedSearchClubProfile.setCategoriesName(
                    categoryService.getAllCategories().stream().map(CategoryResponse::getName).toList());
        }
        log.debug("getAdvancedSearchClubs, advClubProf :" + advancedSearchClubProfile);

        Page<Club> clubResponses = clubRepository.findAllBylAdvancedSearch(advancedSearchClubProfile.getName(),
                advancedSearchClubProfile.getAge(), advancedSearchClubProfile.getCityName(),
                advancedSearchClubProfile.getDistrictName(), advancedSearchClubProfile.getStationName(),
                CategoryUtil.replaceSemicolonToComma(advancedSearchClubProfile.getCategoriesName()),
                advancedSearchClubProfile.getIsOnline(), pageable);

        return new PageImpl<>(
                clubResponses.stream().map(toClubResponseConverter::convertToClubResponse)
                        .toList(),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
    public Page<ClubResponse> getClubsWithoutCategories(Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllWithoutCategories(pageable);

        return new PageImpl<>(
                clubResponses.stream().map(toClubResponseConverter::convertToClubResponse)
                        .toList(),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
    public Page<ClubResponse> getClubsBySearchParameters(SearchClubProfile searchClubProfile, Pageable pageable) {
        log.debug("getClubsBySearchParameters ===> ");
        log.debug(searchClubProfile.toString());

        Page<Club> clubResponses = clubRepository.findAllByParameters(searchClubProfile.getClubName(),
                searchClubProfile.getCityName(), searchClubProfile.getCategoryName(), searchClubProfile.getIsOnline(),
                pageable);

        log.debug("===find clubs : " + clubResponses.getNumberOfElements());

        return new PageImpl<>(
                clubResponses.stream().map(toClubResponseConverter::convertToClubResponse)
                        .toList(),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
    public List<SearchPossibleResponse> getPossibleClubByName(String text, String cityName) {
        return clubRepository.findTop3ByName(text, cityName, PageRequest.of(0, 3)).stream()
                .map(category -> (SearchPossibleResponse) dtoConverter.convertToDto(category,
                        SearchPossibleResponse.class))
                .toList();
    }

    @Override
    public Page<ClubResponse> getClubsByUserId(Long id, Pageable pageable) {
        Page<Club> clubResponses = clubRepository.findAllByUserId(id, pageable);

        return new PageImpl<>(
                clubResponses.stream().map(toClubResponseConverter::convertToClubResponse)
                        .toList(),
                clubResponses.getPageable(), clubResponses.getTotalElements());
    }

    @Override
    public List<ClubResponse> getClubByCategoryAndCity(SearchClubProfile searchClubProfile) {
        List<Club> clubResponses = clubRepository.findAllClubsByParameters(searchClubProfile.getCityName(),
                searchClubProfile.getCategoryName());

        return clubResponses.stream().map(toClubResponseConverter::convertToClubResponse)
                .toList();
    }

    @Override
    public ClubResponse changeClubOwner(Long clubId, ClubOwnerProfile clubOwnerProfile) {
        User user = userService.getAuthenticatedUser();
        validateClubOwner(clubId, user);
        Club club = getClubById(clubId);
        club.setUser(clubOwnerProfile.getUser());

        log.debug("changed club owner by id {}", club);
        return dtoConverter.convertToDto(clubRepository.save(club), ClubResponse.class);
    }

    @Override
    public void updateContacts() {
        List<Center> centers = centerRepository.findAll();
        List<Center> updatedCenters = getUpdatedCenters(centers);
        centerRepository.saveAll(updatedCenters);

        List<Club> clubs = clubRepository.findAll();
        List<Club> updatedClubs = getUpdatedClubs(clubs);
        clubRepository.saveAll(updatedClubs);
    }

    private List<Center> getUpdatedCenters(List<Center> centers) {
        return centers.stream().filter(center -> center.getContacts() != null)
                .filter(center -> !this.isValidJSON(center.getContacts())).map(center -> {
                    JsonNodeFactory factory = JsonNodeFactory.instance;
                    if (center.getContacts().startsWith("{")) {
                        String contacts = center.getContacts().replace("::", ":");
                        ObjectNode node = getJsonNodes(factory, contacts);

                        center.setContacts(node.toString());
                    } else {
                        ObjectNode json = factory.objectNode();
                        updateCenterContacts(center, factory, json);

                        center.setContacts(json.toString());
                    }
                    log.info(center.getContacts());
                    return center;
                }).toList();
    }

    private void updateCenterContacts(Center center, JsonNodeFactory factory, ObjectNode json) {
        Stream.of(center.getContacts().split(",")).map(String::trim)
                .filter(contact -> !contact.isEmpty()).filter(contact -> !contact.endsWith("::"))
                .map(contact -> contact.split("::")).forEach(contact -> {
                    String key = contact[0];
                    String value = contact[1];

                    if (key.equals("1")) {
                        ArrayNode array = json.has(key) ? (ArrayNode) json.get(key)
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
    }

    private List<Club> getUpdatedClubs(List<Club> clubs) {
        return clubs.stream().filter(club -> club.getContacts() != null)
                .filter(club -> !this.isValidJSON(club.getContacts())).map(club -> {
                    JsonNodeFactory factory = JsonNodeFactory.instance;
                    if (club.getContacts().startsWith("{")) {
                        String contacts = club.getContacts().replace("::", ":");
                        ObjectNode node = getJsonNodes(factory, contacts);

                        club.setContacts(node.toString());
                    } else {
                        ObjectNode json = factory.objectNode();
                        updateClubContacts(club, factory, json);

                        club.setContacts(json.toString());
                    }
                    log.info(club.getContacts());
                    return club;
                }).toList();
    }

    private void updateClubContacts(Club club, JsonNodeFactory factory, ObjectNode json) {
        Stream.of(club.getContacts().split(",")).map(String::trim)
                .filter(contact -> !contact.isEmpty())
                .filter(contact -> !contact.endsWith("::")).map(contact -> contact.split("::"))
                .forEach(contact -> {
                    String key = contact[0];
                    String value = contact[1];

                    if (key.equals("1")) {
                        ArrayNode array = json.has(key) ? (ArrayNode) json.get(key)
                                : factory.arrayNode();

                        String convertedValue = convert(value);
                        // @formatter:off
                        switch (convertedValue) {
                          case "+380950993545093138461606328812020958114277" -> {
                              array.add("+380950993545");
                              array.add("+380931384616");
                              array.add("+380632881202");
                              array.add("+380958114277");
                          }
                          case "+38044517699704451761880445178279" -> {
                              array.add("+380445176997");
                              array.add("+380445176188");
                              array.add("+380445178279");
                          }
                          case "+38044599612309640318" -> array.add("+380445996123");
                          case "+3804456499930445749818" -> {
                              array.add("+380445649993");
                              array.add("+380445749818");
                          }
                          case "+3804456462130445608993" -> {
                              array.add("+380445646213");
                              array.add("+380445608993");
                          }
                          case "+3804456254940445640218" -> {
                              array.add("+380445625494");
                              array.add("+380445640218");
                          }
                          case "+380938380570994517940" -> {
                              array.add("+380938380570");
                              array.add("+380994517940");
                          }
                          default -> array.add(convertedValue);
                        }
                        // @formatter:on
                        json.set(key, array);
                    } else {
                        json.put(key, value);
                    }
                });
    }

    private ObjectNode getJsonNodes(JsonNodeFactory factory, String contacts) {
        ObjectNode node = (ObjectNode) toJSON(contacts);

        if (node.has("1")) {
            ArrayNode array = factory.arrayNode();
            array.add(convert(node.get("1").asText()));
            node.set("1", array);
        }
        return node;
    }

    private JsonNode toJSON(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidJSON(final String json) {
        JsonNode jsonNode = toJSON(json);
        return jsonNode != null;
    }

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
    @Transactional
    public ClubResponse deleteClubById(Long id) {
        User user = userService.getAuthenticatedUser();
        validateClubOwner(id, user);

        Club club = getClubById(id);

        try {
            club.getFeedbacks().forEach(feedback -> feedbackRepository.deleteById(feedback.getId()));

            club.getLocations().forEach(location -> {
                location.setClub(null);
                locationRepository.deleteById(location.getId());
            });

            club.getUrlGallery().forEach(urlGallery -> {
                Optional<GalleryPhoto> galleryPhoto = galleryRepository.findById(urlGallery.getId());
                if (galleryPhoto.isPresent()) {
                    galleryPhoto.get().setClub(null);
                    galleryRepository.deleteById(galleryPhoto.get().getId());
                }
            });

            complaintRepository.getAllByClubId(club.getId())
                    .forEach(complaint -> complaintRepository.deleteById(complaint.getId()));

            clubRepository.deleteById(id);
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
    public void validateClubOwner(Long id, User user) {
        User userFromClub = getClubById(id).getUser();

        if (!userFromClub.equals(user) && !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new NotVerifiedUserException(CLUB_CANT_BE_MANAGE_BY_USER);
        }
    }

    @Override
    public SuccessUpdatedClub updateRatingNewFeedback(FeedbackResponse feedbackResponse) {
        Club club = getClubById(feedbackResponse.getClub().getId());

        // sometimes clubs have FeedBacks and Rating fields = NULL, we set it to the default value
        if (club.getFeedbackCount() == null) {
            club.setFeedbackCount(0L);
        }
        if (club.getRating() == null) {
            club.setRating(0.0);
        }

        long newFeedbackCount = club.getFeedbackCount() + 1;
        Double newRating = (club.getRating() * club.getFeedbackCount() + feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public SuccessUpdatedClub updateRatingEditFeedback(FeedbackResponse previousFeedback,
                                                       FeedbackResponse updatedFeedback) {
        Club club = getClubById(previousFeedback.getClub().getId());

        Double newRating = (club.getRating() * club.getFeedbackCount() - previousFeedback.getRate()
                + updatedFeedback.getRate()) / club.getFeedbackCount();

        return updateClubRating(club, newRating, club.getFeedbackCount());
    }

    @Override
    public SuccessUpdatedClub updateRatingDeleteFeedback(FeedbackResponse feedbackResponse) {
        Club club = getClubById(feedbackResponse.getClub().getId());

        long newFeedbackCount = club.getFeedbackCount() - 1;
        Double newRating = newFeedbackCount == 0 ? 0
                : (club.getRating() * club.getFeedbackCount() - feedbackResponse.getRate()) / newFeedbackCount;

        return updateClubRating(club, newRating, newFeedbackCount);
    }

    @Override
    public List<ClubResponse> updateRatingForAllClubs() {
        return getListOfClubs().stream().map(clubResponse -> {
            Club updClub = getClubById(clubResponse.getId());
            updClub.setRating(feedbackRepository.findAvgRating(clubResponse.getId()));
            updClub.setFeedbackCount((long) feedbackRepository.getAllByClubId(clubResponse.getId()).size());
            clubRepository.save(updClub);
            return clubResponse;
        }).toList();
    }

    @Override
    public List<ClubResponse> getTopClubsByCity(TopClubProfile topClubProfile) {
        return clubRepository
                .findTopClubsByCity(topClubProfile.getCityName(), topClubProfile.getAmount()).stream()
                .map(toClubResponseConverter::convertToClubResponse).toList();
    }

    private SuccessUpdatedClub updateClubRating(Club club, Double rating, Long feedbackCount) {
        ClubResponse previousClub = dtoConverter.convertToDto(club, ClubResponse.class);

        club.setRating(rating);
        club.setFeedbackCount(feedbackCount);
        Club updClub = clubRepository.save(club);

        if (updClub.getCenter() != null) {
            centerService.updateRatingUpdateClub(previousClub, dtoConverter.convertToDto(updClub, ClubResponse.class));
        }

        return dtoConverter.convertToDto(updClub, SuccessUpdatedClub.class);
    }

    @Override
    public void archiveModel(Club club) {
        ClubArch clubArch = dtoConverter.convertToDto(club, ClubArch.class);
        clubArch.setUrlGalleriesIds(
                club.getUrlGallery().stream().map(GalleryPhoto::getId).toList());
        clubArch.setLocationsIds(club.getLocations().stream().map(Location::getId).collect(Collectors.toSet()));
        clubArch.setCategoriesIds(club.getCategories().stream().map(Category::getId).collect(Collectors.toSet()));
        clubArch.setFeedbacksIds(club.getFeedbacks().stream().map(Feedback::getId).collect(Collectors.toSet()));
        archiveService.saveModel(clubArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ClubArch clubArch = objectMapper.readValue(archiveObject, ClubArch.class);
        Club club = Club.builder().build();
        club = dtoConverter.convertToEntity(clubArch, club).withId(null)
                .withCategories(clubArch.getCategoriesIds().stream().map(categoryService::getCategoryById)
                        .collect(Collectors.toSet()))
                .withLocations(clubArch.getLocationsIds().stream().map(locationService::getLocationById)
                        .collect(Collectors.toSet()))
                .withUrlGallery(clubArch.getUrlGalleriesIds().stream().map(galleryRepository::findById)
                        .filter(Optional::isPresent).map(Optional::get).toList())
                .withFeedbacks(clubArch.getFeedbacksIds().stream().map(feedbackService::getFeedbackById)
                        .collect(Collectors.toSet()));
        if (Optional.ofNullable(clubArch.getCenterId()).isPresent()) {
            club.setCenter(centerService.getCenterById(clubArch.getCenterId()));
        }
        if (Optional.ofNullable(clubArch.getUserId()).isPresent()) {
            club.setUser(userService.getUserById(clubArch.getUserId()));
        }
        Club finalClub = clubRepository.save(club);
        club.getLocations().forEach(location -> location.setClub(finalClub));
        club.getUrlGallery().forEach(galleryPhoto -> galleryPhoto.setClub(finalClub));
        club.getFeedbacks().forEach(feedback -> feedback.setClub(finalClub));
    }
}
