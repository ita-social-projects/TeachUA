package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Station;
import com.softserve.teachua.repository.StationRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StationServiceImpl implements StationService {
    private static final String STATION_ALREADY_EXIST = "Station already exist with name: %s";
    private static final String STATION_NOT_FOUND_BY_ID = "Station not found by id: %s";
    private static final String STATION_NOT_FOUND_BY_NAME = "Station not found by name: %s";
    private static final String STATION_DELETING_ERROR = "Can't delete station cause of relationship";

    private final DtoConverter dtoConverter;
    private final StationRepository stationRepository;
    private final CityService cityService;
    private final DistrictService districtService;
    private final ArchiveService archiveService;

    @Autowired
    public StationServiceImpl(DtoConverter dtoConverter, StationRepository stationRepository,
                              CityService cityService, DistrictService districtService, ArchiveService archiveService) {
        this.dtoConverter = dtoConverter;
        this.stationRepository = stationRepository;
        this.cityService = cityService;
        this.districtService = districtService;
        this.archiveService = archiveService;
    }

    @Override
    public StationResponse getStationProfileById(Long id) {
        return dtoConverter.convertToDto(getStationById(id), StationResponse.class);
    }

    @Override
    public Station getStationById(Long id) {
        Optional<Station> optionalStation = id == null ? Optional.empty() : getOptionalStationById(id);
        if (!optionalStation.isPresent()) {
            return null;
            // throw new NotExistException(String.format(STATION_NOT_FOUND_BY_ID, id));
        }

        Station station = optionalStation.get();
        log.debug("**/getting station by id = " + station);
        return station;
    }

    @Override
    public Station getStationByName(String name) {
        Optional<Station> optionalStation = getOptionalStationByName(name);
        if (!optionalStation.isPresent()) {
            throw new NotExistException(String.format(STATION_NOT_FOUND_BY_NAME, name));
        }

        Station station = optionalStation.get();
        log.debug("**/getting city by id = " + station);
        return station;
    }

    @Override
    public Optional<Station> getOptionalStationByName(String name) {
        return stationRepository.findByName(name);
    }

    @Override
    public SuccessCreatedStation addStation(StationProfile stationProfile) {
        if (isStationExistByName(stationProfile.getName())) {
            throw new AlreadyExistException(String.format(STATION_ALREADY_EXIST, stationProfile.getName()));
        }
        Station station = stationRepository.save(dtoConverter.convertToEntity(stationProfile, new Station())
                .withCity(cityService.getCityByName(stationProfile.getCityName())));
        log.debug("**/adding new station = " + station);
        return dtoConverter.convertToDto(station, SuccessCreatedStation.class);
    }

    @Override
    public List<StationResponse> getListOfStations() {
        List<StationResponse> stationResponses = stationRepository.findAll()
                .stream()
                .map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of stations = " + stationResponses);
        return stationResponses;
    }

    @Override
    public List<StationResponse> getListOfStationsByCityName(String name) {
        List<StationResponse> stationResponses = stationRepository.findAllByCityName(name)
                .stream()
                .map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of stations = " + stationResponses);
        return stationResponses;
    }

    @Override
    public StationProfile updateStation(Long id, StationProfile stationProfile) {
        Station station = getStationById(id);
        Station newStation = dtoConverter.convertToEntity(stationProfile, station)
                .withId(id)
                .withCity(cityService.getCityByName(stationProfile.getCityName()));

        log.debug("**/updating station by id = " + newStation);
        return dtoConverter.convertToDto(stationRepository.save(newStation), StationProfile.class);
    }

    @Override
    public StationResponse deleteStationById(Long id) {
        Station station = getStationById(id);

//        archiveService.saveModel(station);

        try {
            stationRepository.deleteById(id);
            stationRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(STATION_DELETING_ERROR);
        }

        log.debug("station {} was successfully deleted", station);
        return dtoConverter.convertToDto(station, StationResponse.class);
    }

    private boolean isStationExistByName(String name) {
        return stationRepository.existsByName(name);
    }

    private Optional<Station> getOptionalStationById(Long id) {
        return stationRepository.findById(id);
    }
}
