package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.club.dto.location.LocationProfile;
import com.softserve.club.model.Center;
import com.softserve.club.model.Club;
import com.softserve.club.model.Location;
import com.softserve.club.repository.LocationRepository;
import com.softserve.club.service.CityService;
import com.softserve.club.service.DistrictService;
import com.softserve.club.service.LocationService;
import com.softserve.club.service.StationService;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.IncorrectInputException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService {
    private static final String NOT_EXIST_EXCEPTION = "Location with %d id not exists";

    private final LocationRepository locationRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final ArchiveMQMessageProducer<Location> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    public LocationServiceImpl(LocationRepository locationRepository, DtoConverter dtoConverter,
                               ObjectMapper objectMapper, CityService cityService, DistrictService districtService,
                               StationService stationService,
                               ArchiveMQMessageProducer<Location> archiveMQMessageProducer,
                               ArchiveClient archiveClient) {
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.cityService = cityService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
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
        archiveModel(location);
        locationRepository.deleteById(id);
        return dtoConverter.convertToDto(location, LocationProfile.class);
    }

    private void archiveModel(Location location) {
        archiveMQMessageProducer.publish(location);
    }

    @Override
    public void restoreModel(Long id) {
        var location = objectMapper.convertValue(
                archiveClient.restoreModel(Location.class.getName(), id),
                Location.class);

        locationRepository.save(location);
    }
}
