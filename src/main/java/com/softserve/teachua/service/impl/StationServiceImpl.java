package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Station;
import com.softserve.teachua.repository.StationRepository;
import com.softserve.teachua.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StationServiceImpl implements StationService {
    private static final String STATION_ALREADY_EXIST = "Station already exist with name: %s";
    private static final String STATION_NOT_FOUND_BY_ID = "Station not found by id: %s";
    private static final String STATION_NOT_FOUND_BY_NAME = "Station not found by name: %s";

    private final DtoConverter dtoConverter;
    private final StationRepository stationRepository;

    @Autowired
    public StationServiceImpl(DtoConverter dtoConverter, StationRepository stationRepository) {
        this.dtoConverter = dtoConverter;
        this.stationRepository = stationRepository;
    }

    /**
     * The method returns dto {@code StationResponse} of Station by id.
     *
     * @param id - put station id.
     * @return new {@code StationResponse}.
     */
    @Override
    public StationResponse getStationProfileById(Long id) {
        return dtoConverter.convertToDto(getStationById(id), StationResponse.class);
    }

    /**
     * The method returns entity {@code Station} of station by id.
     *
     * @param id - put station id.
     * @return new {@code Station}.
     * @throws NotExistException if Station not exists.
     */
    @Override
    public Station getStationById(Long id) {
        Optional<Station> optionalStation = getOptionalStationById(id);
        if (!optionalStation.isPresent()) {
            throw new NotExistException(String.format(STATION_NOT_FOUND_BY_ID, id));
        }

        Station station = optionalStation.get();
        log.info("**/getting station by id = " + station);
        return station;
    }

    /**
     * The method returns entity {@code Station} of station by name.
     *
     * @param name - put station name.
     * @return new {@code Station}.
     * @throws NotExistException if station not exists.
     */
    @Override
    public Station getStationByName(String name) {
        Optional<Station> optionalStation = getOptionalStationByName(name);
        if (!optionalStation.isPresent()) {
            throw new NotExistException(String.format(STATION_NOT_FOUND_BY_NAME, name));
        }

        Station station = optionalStation.get();
        log.info("**/getting city by id = " + station);
        return station;
    }

    /**
     * The method returns dto {@code SuccessCreatedStation} if station successfully added.
     *
     * @param stationProfile - place place body of dto {@code StationProfile}.
     * @return new {@code SuccessCreatedStation}.
     * @throws AlreadyExistException if station already exists.
     */
    @Override
    public SuccessCreatedStation addStation(StationProfile stationProfile) {
        if (isStationExistByName(stationProfile.getName())) {
            throw new AlreadyExistException(String.format(STATION_ALREADY_EXIST, stationProfile.getName()));
        }
        Station station = stationRepository.save(dtoConverter.convertToEntity(stationProfile, new Station()));
        log.info("**/adding new station = " + station);
        return dtoConverter.convertToDto(station, SuccessCreatedStation.class);
    }

    /**
     * The method returns list of dto {@code List<StationResponse>} of all stations.
     *
     * @return new {@code List<StationResponse>}.
     */
    @Override
    public List<StationResponse> getListOfStations() {
        List<StationResponse> stationResponses = stationRepository.findAll()
                .stream()
                .map(station -> (StationResponse) dtoConverter.convertToDto(station, StationResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of stations = " + stationResponses);
        return stationResponses;
    }

    /**
     * The method returns dto {@code StationProfile} of updated station.
     *
     * @param stationProfile - place body of dto {@code StationProfile}.
     * @return new {@code StationProfile}.
     */
    @Override
    public StationProfile updateStation(Long id, StationProfile stationProfile) {
        Station station = getStationById(id);
        Station newStation = dtoConverter.convertToEntity(stationProfile, station)
                .withId(id);

        log.info("**/updating station by id = " + newStation);
        return dtoConverter.convertToDto(stationRepository.save(newStation), StationProfile.class);
    }

    private boolean isStationExistByName(String name) {
        return stationRepository.existsByName(name);
    }

    private Optional<Station> getOptionalStationById(Long id) {
        return stationRepository.findById(id);
    }

    private Optional<Station> getOptionalStationByName(String name) {
        return stationRepository.findByName(name);
    }
}
