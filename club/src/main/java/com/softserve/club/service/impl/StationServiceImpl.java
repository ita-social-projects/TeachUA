package com.softserve.club.service.impl;

import com.softserve.club.dto.station.StationProfile;
import com.softserve.club.dto.station.StationResponse;
import com.softserve.club.dto.station.SuccessCreatedStation;
import com.softserve.club.model.Station;
import com.softserve.club.repository.StationRepository;
import com.softserve.club.service.CityService;
import com.softserve.club.service.DistrictService;
import com.softserve.club.service.StationService;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StationServiceImpl implements StationService {
    private static final String STATION_ALREADY_EXIST = "Station already exist with name: %s";
    private static final String STATION_NOT_FOUND_BY_NAME = "Station not found by name: %s";
    private static final String STATION_DELETING_ERROR = "Can't delete station cause of relationship";

    private final DtoConverter dtoConverter;
    private final StationRepository stationRepository;
    private final CityService cityService;
    private final DistrictService districtService;

    public StationServiceImpl(DtoConverter dtoConverter, StationRepository stationRepository,
                              CityService cityService, DistrictService districtService) {
        this.dtoConverter = dtoConverter;
        this.stationRepository = stationRepository;
        this.cityService = cityService;
        this.districtService = districtService;
    }

    @Override
    public StationResponse getStationProfileById(Long id) {
        return dtoConverter.convertToDto(getStationById(id), StationResponse.class);
    }

    @Override
    public Station getStationById(Long id) {
        Optional<Station> optionalStation = id == null ? Optional.empty() : getOptionalStationById(id);
        if (optionalStation.isEmpty()) {
            return null;
        }

        Station station = optionalStation.get();
        log.debug("**/getting station by id = " + station);
        return station;
    }

    @Override
    public Station getStationByName(String name) {
        Optional<Station> optionalStation = getOptionalStationByName(name);
        if (optionalStation.isEmpty()) {
            throw new NotExistException(String.format(STATION_NOT_FOUND_BY_NAME, name));
        }

        Station station = optionalStation.get();
        log.debug("**/getting city by id = " + station);
        return station;
    }

    @Override
    public List<StationResponse> getAllByDistrictNameAndCityName(StationProfile stationProfile) {
        List<StationResponse> stationList = stationRepository
                .findAllByDistrictNameAndCityName(stationProfile.getDistrictName(), stationProfile.getCityName())
                .stream().map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .toList();

        log.debug("**/get all stations by District = " + stationList);

        return stationList;
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
                .withCity(cityService.getCityByName(stationProfile.getCityName()))
                .withDistrict(districtService.getDistrictByName(stationProfile.getDistrictName())));
        log.debug("**/adding new station = " + station);
        return dtoConverter.convertToDto(station, SuccessCreatedStation.class);
    }

    @Override
    public List<StationResponse> getListOfStations() {
        List<StationResponse> stationResponses = stationRepository.findAll().stream()
                .map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .toList();

        log.debug("**/getting list of stations = " + stationResponses);
        return stationResponses;
    }

    @Override
    public List<StationResponse> getListOfStationsByCityName(String name) {
        List<StationResponse> stationResponses = stationRepository.findAllByCityName(name).stream()
                .map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .toList();

        log.debug("**/getting list of stations = " + stationResponses);
        return stationResponses;
    }

    @Override
    public StationProfile updateStation(Long id, StationProfile stationProfile) {
        Station station = getStationById(id);
        Station newStation = dtoConverter.convertToEntity(stationProfile, station).withId(id)
                .withCity(cityService.getCityByName(stationProfile.getCityName()))
                .withDistrict(districtService.getDistrictByName(stationProfile.getDistrictName()));

        log.debug("**/updating station by id = " + newStation);
        return dtoConverter.convertToDto(stationRepository.save(newStation), StationProfile.class);
    }

    @Override
    public StationResponse deleteStationById(Long id) {
        Station station = getStationById(id);

        try {
            stationRepository.deleteById(id);
            stationRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(STATION_DELETING_ERROR);
        }

        //archiveModel(station);

        log.debug("station {} was successfully deleted", station);
        return dtoConverter.convertToDto(station, StationResponse.class);
    }

    private boolean isStationExistByName(String name) {
        return stationRepository.existsByName(name);
    }

    private Optional<Station> getOptionalStationById(Long id) {
        return stationRepository.findById(id);
    }

    //todo@
    /*
    @Override
    public void archiveModel(Station station) {
        archiveService.saveModel(dtoConverter.convertToDto(station, StationArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        StationArch stationArch = objectMapper.readValue(archiveObject, StationArch.class);
        Station station = Station.builder().build();
        station = dtoConverter.convertToEntity(stationArch, station).withId(null)
                .withCity(cityService.getCityById(stationArch.getCityId()));
        stationRepository.save(station);
    }
    */
}
