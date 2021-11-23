package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.model.*;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.LocationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository,
                               DtoConverter dtoConverter) {
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
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
}
