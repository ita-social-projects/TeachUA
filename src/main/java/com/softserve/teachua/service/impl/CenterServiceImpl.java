package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.CenterToCenterResponseConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.search.AdvancedSearchCenterProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CenterServiceImpl implements CenterService {
    private static final String CENTER_ALREADY_EXIST = "Center already exist with name: %s";
    private static final String CENTER_NOT_FOUND_BY_ID = "Center not found by id: %s";
    private static final String CENTER_NOT_FOUND_BY_NAME = "Center not found by name: %s";
    private static final String CENTER_DELETING_ERROR = "Can't delete center cause of relationship";

    private final LocationService locationService;
    private final CenterRepository centerRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;
    private final ClubService clubService;
    private final LocationRepository locationRepository;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CenterToCenterResponseConverter centerToCenterResponseConverter;


    @Autowired
    public CenterServiceImpl(LocationService locationService, CenterRepository centerRepository,
                             ArchiveService archiveService,
                             DtoConverter dtoConverter,
                             ClubService clubService,
                             LocationRepository locationRepository,
                             CityService cityService,
                             DistrictService districtService,
                             StationService stationService,
                             ClubRepository clubRepository,
                             UserRepository userRepository,
                             UserService userService, CenterToCenterResponseConverter centerToCenterResponseConverter) {
        this.locationService = locationService;
        this.centerRepository = centerRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
        this.clubService = clubService;
        this.locationRepository = locationRepository;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.centerToCenterResponseConverter=centerToCenterResponseConverter;
    }

    /**
     * The method returns dto {@code CenterResponse} of center by id.
     *
     * @param id - put center id.
     * @return new {@code CenterResponse}.
     */
    @Override
    public CenterResponse getCenterByProfileId(Long id) {
        return centerToCenterResponseConverter.convertToCenterResponse(getCenterById(id));
    }

    /**
     * The method returns dto {@code SuccessCreatedCenter} if center successfully added.
     *
     * @param centerProfile - place body of dto {@code CenterProfile}.
     * @return new {@code SuccessCreatedCenter}.
     * @throws AlreadyExistException if center already exists.
     */
    @Override
    public SuccessCreatedCenter addCenter(CenterProfile centerProfile) {

        log.info("centerName = "+centerProfile.getName());

        if (isCenterExistByName(centerProfile.getName())) {
            throw new AlreadyExistException(String.format(CENTER_ALREADY_EXIST, centerProfile.getName()));
        }

        User user = null;
        if(centerProfile.getUserId() != null){
            log.info("CenterServiceImpl=> centerProfile.userId == "+centerProfile.getUserId());
            user = userRepository.getOne(centerProfile.getUserId());
        }else {log.info("CenterServiceImpl=> centerProfile.userId == null");}


        Center center = centerRepository.save(dtoConverter.convertToEntity(centerProfile, new Center())
                    .withUser(user));

        List<LocationProfile> locations = centerProfile.getLocations();
        if ( locations != null && !locations.isEmpty()) {
            center.setLocations(locations
                    .stream()
                    .map(locationProfile -> locationRepository.save(
                            dtoConverter.convertToEntity(locationProfile, new Location())
                                    .withCenter(center)
                                    .withCity(cityService.getCityByName(locationProfile.getCityName()))
                                    .withDistrict(locationProfile.getDistrictName() == null ? null : districtService.getDistrictByName(locationProfile.getDistrictName()))
                                    .withStation(locationProfile.getStationName() == null ? null : stationService.getStationByName(locationProfile.getStationName()))
                    ))
                    .collect(Collectors.toSet())
            );
        }

        List<Long> clubsId =centerProfile.getClubsId();
        if(clubsId != null &&  !clubsId.isEmpty())
        for(Long id : clubsId ){
            Club club = clubService.getClubById(id);
            club.setCenter(center);
            clubRepository.save(club);
        }

        log.info("**/adding new center = " + centerProfile.getName());
        return dtoConverter.convertToDto(center, SuccessCreatedCenter.class);
    }

    @Override
    public SuccessCreatedCenter addCenterRequest(CenterProfile centerProfile, HttpServletRequest httpServletRequest) {
        if(centerProfile.getUserId() != null){
            centerProfile.setUserId(userService.getUserFromRequest(httpServletRequest).getId());
        }

        return addCenter(centerProfile);
    }


    /**
     * The method returns entity {@code Center} of center by id.
     *
     * @param id - put center id.
     * @return new {@code Center}.
     * @throws NotExistException if center not exists.
     */
    @Override
    public Center getCenterById(Long id) {
        Optional<Center> optionalCenter = getOptionalCenterById(id);
        if (!optionalCenter.isPresent()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_ID, id));
        }

        Center center = optionalCenter.get();
        log.info("**/getting center by id = " + center);
        return center;
    }

    /**
     * The method returns entity {@code Center} of center by external id.
     *
     * @param centerExternalId - put centerExternalId id.
     * @return new {@code Center}.
     * @throws NotExistException if center not exists.
     */
    @Override
    public Center getCenterByExternalId(Long centerExternalId) {
        Center center = centerRepository.findCenterByCenterExternalId(centerExternalId);
        log.info("**/getting center by external id = " + center);
        return center;
    }

    /**
     * The method returns dto {@code CenterProfile} of updated club.
     *
     * @param centerProfile - place body of dto {@code CenterProfile}.
     * @return new {@code CenterProfile}.
     */

    @Override
    public CenterProfile updateCenter(Long id, CenterProfile centerProfile) {
        Center center = getCenterById(id);
        if (isCenterExistByName(centerProfile.getName())) {
            throw new AlreadyExistException(String.format(CENTER_ALREADY_EXIST, centerProfile.getName()));
        }
        Center newCenter = dtoConverter.convertToEntity(centerProfile, center)
                .withId(id);

        log.info("**/updating center by id = " + newCenter);
        return dtoConverter.convertToDto(centerRepository.save(newCenter), CenterProfile.class);
    }

    @Override
    public CenterResponse deleteCenterById(Long id) {
        Center center = getCenterById(id);


        archiveService.saveModel(center);

        try {
            log.info("delete Center");
            clubRepository.findClubsByCenter(center).forEach(club -> club.setCenter(null));
            locationRepository.findLocationsByCenter(center).forEach(location -> location.setCenter(null));
            centerRepository.deleteById(id);
            centerRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CENTER_DELETING_ERROR);
        }

        log.info("center {} was successfully deleted", center);
        return dtoConverter.convertToDto(center, CenterResponse.class);
    }

    /**
     * The method returns list of dto {@code Page<CenterResponse>} of all centers by user-owner.
     *
     * @param id - put user id.
     * @return new {@code Page<ClubResponse>}.
     */
    @Override
    public Page<CenterResponse> getCentersByUserId(Long id, Pageable pageable) {
        Page<Center> centerResponses = centerRepository.findAllByUserId(id, pageable);

        return new PageImpl<>(centerResponses
                .stream()
                .map(center -> (CenterResponse) centerToCenterResponseConverter.convertToCenterResponse(center))
                .collect(Collectors.toList()),
                centerResponses.getPageable(), centerResponses.getTotalElements());
    }

    /**
     * The method returns page of dto {@code Page<CenterResponse>} of all centers by advancedSearchCenterProfile.
     *
     * @param advancedSearchCenterProfile - put user id.
     * @param pageable - pagination object.
     * @return new {@code Page<ClubResponse>}.
     *
     * @author Vasyl Khula
     */
    @Override
    public Page<CenterResponse> getAdvancedSearchCenters(AdvancedSearchCenterProfile advancedSearchCenterProfile,
                                                         Pageable pageable) {
        Page<Center> centersOnPage = centerRepository.findAllBylAdvancedSearch(
                advancedSearchCenterProfile.getCityName(),
                advancedSearchCenterProfile.getDistrictName(),
                advancedSearchCenterProfile.getStationName(),
                pageable);

        return new PageImpl<>(centersOnPage
                .stream()
                .map(center -> (CenterResponse) centerToCenterResponseConverter.convertToCenterResponse(center))
                .peek(System.out::println)
                .collect(Collectors.toList()),
                centersOnPage.getPageable(), centersOnPage.getTotalElements());
    }

    /**
     * The method returns entity {@code Center} of center by name.
     *
     * @param name - put center name.
     * @return new {@code Center}.
     * @throws NotExistException if center not exists.
     */
    @Override
    public Center getCenterByName(String name) {
        Optional<Center> optionalCenter = getOptionalCenterByName(name);
        if (!optionalCenter.isPresent()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_NAME, name));
        }

        Center center = optionalCenter.get();
        log.info("**/getting center by name = " + name);
        return center;
    }

    /**
     * The method returns list of dto {@code List<CenterResponse>} of all centers.
     *
     * @return new {@code List<CenterResponse>}.
     */
    @Override
    public List<CenterResponse> getListOfCenters() {
        List<CenterResponse> centerResponses = centerRepository.findAll()
                .stream()
                .map(center -> (CenterResponse) centerToCenterResponseConverter.convertToCenterResponse(center))
                .collect(Collectors.toList());

//        log.info("**/getting list of centers = " + centerResponses);
        return centerResponses;
    }

    private boolean isCenterExistByName(String name) {
        return centerRepository.existsByName(name);
    }

    private Optional<Center> getOptionalCenterById(Long id) {
        if(id == null){
            return Optional.empty();
        }
        return centerRepository.findById(id);
    }

    private Optional<Center> getOptionalCenterByName(String name) {
        return centerRepository.findByName(name);
    }
}
