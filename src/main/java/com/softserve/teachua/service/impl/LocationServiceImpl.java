package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.*;
import com.softserve.teachua.model.archivable.LocationArch;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService, ArchiveMark<Location> {
    private final static String NOT_EXIST_EXCEPTION = "Location with %d id not exists";
    private final LocationRepository locationRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final CenterRepository centerRepository;
    private final ClubRepository clubRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository,
                               DtoConverter dtoConverter,
                               ArchiveService archiveService,
                               ObjectMapper objectMapper,
                               CityService cityService,
                               DistrictService districtService,
                               StationService stationService,
                               CenterRepository centerRepository,
                               ClubRepository clubRepository) {
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.centerRepository = centerRepository;
        this.clubRepository = clubRepository;
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
    public Set<Location> updateCenterLocation(Set<LocationProfile> locations, Center center) {
        if (locations == null || locations.isEmpty()) {
            throw new IncorrectInputException("empty Location");
        }

        locationRepository.deleteAllByCenter(center);

        Set<Location> locationSet = locations
                .stream()
                .map(locationProfile -> locationRepository
                        .save(dtoConverter.convertToEntity(locationProfile, new Location()
                                .withCenter(center))))
                .collect(Collectors.toSet());

        return locationSet;
    }

    @Override
    public Set<Location> updateLocationByClub(Set<LocationResponse> locations, Club club) {
        if (locations == null || locations.isEmpty()) {
            return null;
        }

        locationRepository.deleteAllByClub(club);

        Set<Location> locationSet = locations
                .stream()
                .map(locationResponse -> locationRepository
                        .save(dtoConverter.convertToEntity(locationResponse, new Location().withClub(club))))
                .collect(Collectors.toSet());

        return locationSet;
    }

    @Override
    public Location getLocationById(Long id){
        Optional<Location> location = locationRepository.findById(id);
        return location.orElseThrow(() -> new NotExistException(String.format(NOT_EXIST_EXCEPTION, id)));
    }

    @Override
    public LocationResponse deleteLocationById(Long id) {
        Location location = getLocationById(id);

        locationRepository.deleteById(id);
        return dtoConverter.convertToDto(location, LocationResponse.class);
    }

    @Override
    public void archiveModel(Location location) {
        LocationArch locationArch = dtoConverter.convertToDto(location, LocationArch.class);
        if (Optional.ofNullable(location.getCity()).isPresent()) {
            locationArch.setCityId(location.getCity().getId());
        }
        if(Optional.ofNullable(location.getClub()).isPresent()){
            locationArch.setClubId(location.getClub().getId());
        }
        if(Optional.ofNullable(location.getCenter()).isPresent()){
            locationArch.setCenterId(location.getCenter().getId());
        }
        if(Optional.ofNullable(location.getDistrict()).isPresent()){
            locationArch.setDistrictId(location.getDistrict().getId());
        }
        if(Optional.ofNullable(location.getStation()).isPresent()){
            locationArch.setStationId(location.getStation().getId());
        }
        archiveService.saveModel(locationArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        LocationArch locationArch = objectMapper.readValue(archiveObject, LocationArch.class);
        Location location = Location.builder().build();
        Long locationId = location.getId();
        location = dtoConverter.convertToEntity(locationArch, location)
                .withId(locationId);
        if (Optional.ofNullable(locationArch.getCityId()).isPresent()) {
            location.setCity(cityService.getCityById(locationArch.getCityId()));
        }
        if(Optional.ofNullable(locationArch.getClubId()).isPresent()){
            location.setClub(clubRepository.findById(locationArch.getClubId()).orElse(null));
        }
        if(Optional.ofNullable(locationArch.getCenterId()).isPresent()){
            location.setCenter(centerRepository.findById(locationArch.getCenterId()).orElse(null));
        }
        if(Optional.ofNullable(locationArch.getDistrictId()).isPresent()){
            location.setDistrict(districtService.getDistrictById(locationArch.getDistrictId()));
        }
        if(Optional.ofNullable(locationArch.getStationId()).isPresent()){
            location.setStation(stationService.getStationById(locationArch.getStationId()));
        }
        locationRepository.save(location);
    }
}
