package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.club.dto.center.CenterProfile;
import com.softserve.club.dto.center.CenterResponse;
import com.softserve.club.dto.center.SuccessCreatedCenter;
import com.softserve.club.dto.club.ClubResponse;
import com.softserve.club.dto.location.LocationProfile;
import com.softserve.club.dto.search.AdvancedSearchCenterProfile;
import com.softserve.club.model.Center;
import com.softserve.club.model.Club;
import com.softserve.club.repository.CenterRepository;
import com.softserve.club.repository.ClubRepository;
import com.softserve.club.repository.LocationRepository;
import com.softserve.club.service.CenterService;
import com.softserve.club.service.CityService;
import com.softserve.club.service.ClubService;
import com.softserve.club.service.DistrictService;
import com.softserve.club.service.LocationService;
import com.softserve.club.service.StationService;
import com.softserve.club.util.converter.CenterToCenterResponseConverter;
import com.softserve.club.util.converter.ClubToClubResponseConverter;
import com.softserve.club.util.converter.CoordinatesConverter;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CenterServiceImpl implements CenterService/*, ArchiveMark<Center>*/ {
    private static final String CENTER_ALREADY_EXIST = "Center already exist with name: %s";
    private static final String CENTER_NOT_FOUND_BY_ID = "Center not found by id: %s";
    private static final String CENTER_NOT_FOUND_BY_NAME = "Center not found by name: %s";
    private static final String CENTER_DELETING_ERROR = "Can't delete center cause of relationship";
    private final LocationService locationService;
    private final CenterRepository centerRepository;
    private final DtoConverter dtoConverter;
    private final LocationRepository locationRepository;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final ClubRepository clubRepository;
    private final CenterToCenterResponseConverter centerToCenterResponseConverter;
    private final ClubToClubResponseConverter toClubResponseConverter;
    private final CoordinatesConverter coordinatesConverter;
    private final ObjectMapper objectMapper;
    private ClubService clubService;

    @Autowired
    public CenterServiceImpl(LocationService locationService, CenterRepository centerRepository,
                             DtoConverter dtoConverter,
                             LocationRepository locationRepository, CityService cityService,
                             DistrictService districtService, StationService stationService,
                             ClubRepository clubRepository,
                             CenterToCenterResponseConverter centerToCenterResponseConverter,
                             ClubToClubResponseConverter toClubResponseConverter,
                             CoordinatesConverter coordinatesConverter, ObjectMapper objectMapper) {
        this.locationService = locationService;
        this.centerRepository = centerRepository;
        this.dtoConverter = dtoConverter;
        this.locationRepository = locationRepository;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.clubRepository = clubRepository;
        this.centerToCenterResponseConverter = centerToCenterResponseConverter;
        this.toClubResponseConverter = toClubResponseConverter;
        this.coordinatesConverter = coordinatesConverter;
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setClubService(@Lazy ClubService clubService) {
        this.clubService = clubService;
    }

    private void convertCoordinates(LocationProfile locationProfile) {
        coordinatesConverter.locationProfileConverterToDb(locationProfile);
    }

    private Set<LocationProfile> setLocationToCenter(List<LocationProfile> locations, long id) {
        if (!locations.isEmpty()) {
            for (LocationProfile profile : locations) {
                convertCoordinates(profile);
                if (profile.getCityName() != null && !profile.getCityName().isEmpty()) {
                    profile.setCityId(cityService.getCityByName(profile.getCityName()).getId());
                }
                if (profile.getDistrictName() != null && !profile.getDistrictName().isEmpty()) {
                    profile.setDistrictId(districtService.getDistrictByName(profile.getDistrictName()).getId());
                }
                if (profile.getStationName() != null && !profile.getStationName().isEmpty()) {
                    profile.setStationId(stationService.getStationByName(profile.getStationName()).getId());
                }
                profile.setCenterId(id);
            }
        }
        return new HashSet<>(locations);
    }

    @Override
    public CenterResponse getCenterProfileById(Long id) {
        return centerToCenterResponseConverter.convertToCenterResponse(getCenterById(id));
    }

    @Override
    public SuccessCreatedCenter addCenter(CenterProfile centerProfile) {
        //todo
        //log.debug("centerName = " + centerProfile.getName());
        //if (isCenterExistByName(centerProfile.getName())) {
        //    throw new AlreadyExistException(String.format(CENTER_ALREADY_EXIST, centerProfile.getName()));
        //}
        //
        //User user = null;
        //if (centerProfile.getUserId() != null) {
        //    log.debug("CenterServiceImpl=> centerProfile.userId == " + centerProfile.getUserId());
        //    user = userRepository.findById(centerProfile.getUserId()).orElseThrow(NotExistException::new);
        //} else {
        //    log.debug("CenterServiceImpl=> centerProfile.userId == null");
        //}
        //
        //Center center = centerRepository.save(dtoConverter.convertToEntity(centerProfile, new Center())
        //        .withUser(user)
        //        .withClubCount((long) centerProfile.getClubsId().size())
        //        .withRating(0.0));
        //
        //List<LocationProfile> locations = centerProfile.getLocations();
        //if (locations != null && !locations.isEmpty()) {
        //    for (LocationProfile profile : locations) {
        //        convertCoordinates(profile);
        //    }
        //    center.setLocations(
        //            locations.stream()
        //                    .map(locationProfile -> locationRepository.save(dtoConverter
        //                            .convertToEntity(locationProfile, new Location()).withCenter(center)
        //                            .withCity(cityService.getCityByName(locationProfile.getCityName()))
        //                            .withDistrict(locationProfile.getDistrictName() == null ? null
        //                                    : districtService.getDistrictByName(locationProfile.getDistrictName()))
        //                            .withStation(locationProfile.getStationName() == null ? null
        //                                    : stationService.getStationByName(locationProfile.getStationName()))))
        //                    .collect(Collectors.toSet()));
        //}
        //
        //saveClubs(centerProfile, center);
        //
        //log.debug("**/adding new center = " + centerProfile.getName());
        //return dtoConverter.convertToDto(center, SuccessCreatedCenter.class);
        throw new NotImplementedException();
    }

    private void saveClubs(CenterProfile centerProfile, Center center) {
        List<Long> clubsId = centerProfile.getClubsId();

        if (clubsId != null && !clubsId.isEmpty()) {
            for (Long id : clubsId) {
                log.debug("ID - " + id);
                Club club = clubService.getClubById(id);
                club.setCenter(center);
                clubRepository.save(club);
            }
        }
    }

    @Override
    public SuccessCreatedCenter addCenterRequest(CenterProfile centerProfile) {
        //todo
        /*
        centerProfile.setUserId(customUserDetailsService.getUserPrincipal().getId());

        return addCenter(centerProfile);
        */
        throw new NotImplementedException();
    }

    @Override
    public Center getCenterById(Long id) {
        Optional<Center> optionalCenter = getOptionalCenterById(id);
        if (optionalCenter.isEmpty()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_ID, id));
        }

        Center center = optionalCenter.get();
        log.debug("**/getting center by id = " + center);
        return center;
    }

    @Override
    public Center getCenterByExternalId(Long centerExternalId) {
        Center center = centerRepository.findCenterByCenterExternalId(centerExternalId);
        log.debug("**/getting center by external id = " + center);
        return center;
    }

    @Override
    public CenterProfile updateCenter(Long id, CenterProfile centerProfile) {
        Center center = getCenterById(id);

        Set<LocationProfile> locations = setLocationToCenter(centerProfile.getLocations(), id);
        Center newCenter = dtoConverter.convertToEntity(centerProfile, center)
                .withId(id)
                .withContacts(centerProfile.getContacts())
                .withName(centerProfile.getName())
                .withDescription(centerProfile.getDescription())
                .withUrlBackgroundPicture(centerProfile.getUrlBackgroundPicture())
                .withLocations(locationService.updateCenterLocation(locations, center));

        saveClubs(centerProfile, center);
        log.debug("**/updating center by id = " + newCenter);
        return dtoConverter.convertToDto(centerRepository.save(newCenter), CenterProfile.class);
    }

    @Override
    public CenterResponse deleteCenterById(Long id) {
        Center center = getCenterById(id);

        try {
            log.debug("delete Center");
            clubRepository.findClubsByCenter(center).forEach(club -> club.setCenter(null));
            locationRepository.findLocationsByCenter(center).forEach(location -> location.setCenter(null));
            centerRepository.deleteById(id);
            centerRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CENTER_DELETING_ERROR);
        }

        //archiveModel(center);

        log.debug("center {} was successfully deleted", center);
        return dtoConverter.convertToDto(center, CenterResponse.class);
    }

    @Override
    public Page<CenterResponse> getCentersByUserId(Long id, Pageable pageable) {
        Page<Center> centerResponses = centerRepository.findAllByUserId(id, pageable);

        return new PageImpl<>(
                centerResponses.stream()
                        .map(centerToCenterResponseConverter::convertToCenterResponse)
                        .toList(),
                centerResponses.getPageable(), centerResponses.getTotalElements());
    }

    @Override
    public Page<ClubResponse> getCenterClubsByCenterId(Long id, Pageable pageable) {
        Page<Club> clubsResponses = clubRepository.findAllByCenterId(id, pageable);
        return new PageImpl<>(
                clubsResponses
                        .stream().map(toClubResponseConverter::convertToClubResponse)
                        .toList(), clubsResponses.getPageable(), clubsResponses.getTotalElements()
        );
    }

    @Override
    public Page<CenterResponse> getAdvancedSearchCenters(AdvancedSearchCenterProfile advancedSearchCenterProfile,
                                                         Pageable pageable) {
        Page<Center> centersOnPage = centerRepository.findAllBylAdvancedSearch(
                advancedSearchCenterProfile.getCityName(), advancedSearchCenterProfile.getDistrictName(),
                advancedSearchCenterProfile.getStationName(), pageable);

        return new PageImpl<>(
                centersOnPage.stream()
                        .map(centerToCenterResponseConverter::convertToCenterResponse).toList(),
                centersOnPage.getPageable(), centersOnPage.getTotalElements());
    }

    @Override
    public Center getCenterByName(String name) {
        Optional<Center> optionalCenter = getOptionalCenterByName(name);
        if (optionalCenter.isEmpty()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_NAME, name));
        }

        Center center = optionalCenter.get();
        log.debug("**/getting center by name = " + name);
        return center;
    }

    @Override
    public List<CenterResponse> getListOfCenters() {
        return centerRepository.findAll().stream()
                .map(centerToCenterResponseConverter::convertToCenterResponse)
                .toList();
    }

    private boolean isCenterExistByName(String name) {
        return centerRepository.existsByName(name);
    }

    private Optional<Center> getOptionalCenterById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return centerRepository.findById(id);
    }

    private Optional<Center> getOptionalCenterByName(String name) {
        return centerRepository.findByName(name);
    }

    @Override
    public CenterResponse updateRatingUpdateClub(ClubResponse previousClub, ClubResponse updatedClub) {
        Center center = getCenterById(previousClub.getCenter().getId());

        double newRating;
        long newClubCount;

        // some centers have rating or ClubCount = null, so we set default value to them
        if (center.getClubCount() == null) {
            center.setClubCount((long) center.getClubs().size());
        }
        if (center.getRating() == null) {
            center.setRating(0.0);
        }

        if (updatedClub.getFeedbackCount() == 0) {
            newClubCount = center.getClubCount() - 1;
            newRating = newClubCount == 0 ? 0
                    : (center.getRating() * center.getClubCount() - previousClub.getRating()) / newClubCount;
        } else if (previousClub.getFeedbackCount() == 0) {
            newClubCount = center.getClubCount() + 1;
            newRating = (center.getRating() * center.getClubCount() + updatedClub.getRating()) / newClubCount;
        } else {
            newClubCount = center.getClubCount();
            newRating = newClubCount == 0 ? 0
                    : (center.getRating() * center.getClubCount() - previousClub.getRating() + updatedClub.getRating())
                    / newClubCount;
        }

        centerRepository.updateRating(center.getId(), newRating, newClubCount);

        return getCenterProfileById(center.getId());
    }

    @Override
    public CenterResponse updateRatingDeleteClub(ClubResponse clubResponse) {
        Center center = getCenterById(clubResponse.getCenter().getId());

        long newClubCount = center.getClubCount() - 1;
        double newRating = newClubCount == 0 ? 0
                : (center.getRating() * center.getClubCount() - clubResponse.getRating()) / newClubCount;

        clubRepository.updateRating(center.getId(), newRating, newClubCount);

        return getCenterProfileById(center.getId());
    }

    @Override
    public List<CenterResponse> updateRatingForAllCenters() {
        return getListOfCenters().stream().map(centerResponse -> {
            Center updCenter = getCenterById(centerResponse.getId());
            updCenter.setClubCount(clubRepository.findClubsByCenter(updCenter).stream()
                    .filter(club -> club.getFeedbackCount() > 0).count());
            updCenter.setRating(clubRepository.findAvgRating(centerResponse.getId()));
            centerRepository.save(updCenter);
            return centerResponse;
        }).toList();
    }

    //todo
    /*
    @Override
    public void archiveModel(Center center) {
        CenterArch centerArch = dtoConverter.convertToDto(center, CenterArch.class);
        centerArch.setClubsIds(center.getClubs().stream().map(Club::getId).collect(Collectors.toSet()));
        centerArch.setLocationsIds(
                center.getLocations().stream().map(Location::getId).collect(Collectors.toSet()));
        archiveService.saveModel(centerArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        CenterArch centerArch = objectMapper.readValue(archiveObject, CenterArch.class);
        Center center = Center.builder().build();
        center = dtoConverter.convertToEntity(centerArch, center).withId(null)
                .withUser(userService.getUserById(centerArch.getUserId()));

        Center finalCenter = centerRepository.save(center);
        centerArch.getLocationsIds().stream().map(locationService::getLocationById)
                .forEach(location -> location.setCenter(finalCenter));

        centerArch.getClubsIds().stream().map(clubService::getClubById).forEach(club -> club.setCenter(finalCenter));
    }
    */
}
