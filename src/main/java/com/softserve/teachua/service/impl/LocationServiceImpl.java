package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.archivable.LocationArch;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.service.LocationService;
import com.softserve.teachua.service.StationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService, ArchiveMark<Location> {
    private static final String NOT_EXIST_EXCEPTION = "Location with %d id not exists";

    private final LocationRepository locationRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final ArchiveService archiveService;
    private final CenterService centerService;
    private final ClubService clubService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, DtoConverter dtoConverter,
            ObjectMapper objectMapper, ArchiveService archiveService, @Lazy CenterService centerService,
            @Lazy ClubService clubService, CityService cityService, DistrictService districtService,
            StationService stationService) {
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.archiveService = archiveService;
        this.centerService = centerService;
        this.clubService = clubService;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
    }

    @Override
    public Location addLocation(LocationProfile locationProfile) {
        log.debug(locationProfile.toString());
        Location location = null;
        try {
            location = locationRepository.save(dtoConverter.convertToEntity(locationProfile, new Location()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return location;
    }

    @Override
    public Location updateLocation(Long id, LocationProfile locationProfile) {
        Location location = getLocationById(id);
        Location newLocation = dtoConverter.convertToEntity(locationProfile, location).withId(id)
                .withClub(location.getClub()).withCity(location.getCity()).withDistrict(location.getDistrict())
                .withStation(location.getStation());

        log.info("**/updating location by id = " + newLocation);
        return locationRepository.save(newLocation);
    }

    @Override
    public Set<Location> updateCenterLocation(Set<LocationProfile> locations, Center center) {
        if (locations == null || locations.isEmpty()) {
            throw new IncorrectInputException("empty Location");
        }

        locationRepository.deleteAllByCenter(center);

        return locations.stream().map(
                locationProfile -> locationRepository.save(dtoConverter.convertToEntity(locationProfile, new Location())
                        .withCenter(center).withCity(cityService.getCityById(locationProfile.getCityId()))
                        .withDistrict(districtService.getDistrictById(locationProfile.getDistrictId()))
                        .withStation(stationService.getStationById(locationProfile.getStationId()))))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Location> updateLocationByClub(Set<LocationProfile> locations, Club club) {
        locationRepository.deleteAllByClub(club);

        if (locations == null || locations.isEmpty()) {
            return null;
        }

        return locations.stream()
                .map(location -> locationRepository
                        .save(dtoConverter.convertToEntity(location, new Location()).withClub(club)
                                .withCity(cityService.getCityById(location.getCityId()))
                                .withDistrict(districtService.getDistrictById(location.getDistrictId()))
                                .withStation(stationService.getStationById(location.getStationId()))))
                .collect(Collectors.toSet());
    }

    @Override
    public Location getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.orElseThrow(() -> new NotExistException(String.format(NOT_EXIST_EXCEPTION, id)));
    }

    @Override
    public List<Location> getListOfAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public LocationProfile deleteLocationById(Long id) {
        Location location = getLocationById(id);

        locationRepository.deleteById(id);
        return dtoConverter.convertToDto(location, LocationProfile.class);
    }

    @Override
    public void archiveModel(Location location) {
        archiveService.saveModel(dtoConverter.convertToDto(location, LocationArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        LocationArch locationArch = objectMapper.readValue(archiveObject, LocationArch.class);
        Location location = dtoConverter.convertToEntity(locationArch, Location.builder().build()).withId(null);
        if (Optional.ofNullable(locationArch.getCityId()).isPresent()) {
            location.setCity(cityService.getCityById(locationArch.getCityId()));
        }
        if (Optional.ofNullable(locationArch.getClubId()).isPresent()) {
            location.setClub(clubService.getClubById(locationArch.getClubId()));
        }
        if (Optional.ofNullable(locationArch.getCenterId()).isPresent()) {
            location.setCenter(centerService.getCenterById(locationArch.getCenterId()));
        }
        if (Optional.ofNullable(locationArch.getDistrictId()).isPresent()) {
            location.setDistrict(districtService.getDistrictById(locationArch.getDistrictId()));
        }
        if (Optional.ofNullable(locationArch.getStationId()).isPresent()) {
            location.setStation(stationService.getStationById(locationArch.getStationId()));
        }
        locationRepository.save(location);
    }
}
