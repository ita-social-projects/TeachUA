package com.softserve.teachua.service;

import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.model.Station;
import java.util.List;
import java.util.Optional;

/**
 * This interface contains all needed methods to manage stations.
 */

public interface StationService {
    /**
     * The method returns dto {@code StationResponse} of Station by id.
     *
     * @param id
     *            - put station id.
     *
     * @return new {@code StationResponse}.
     */
    StationResponse getStationProfileById(Long id);

    /**
     * The method returns entity {@code Station} of station by id.
     *
     * @param id
     *            - put station id.
     *
     * @return new {@code Station}.
     *
     * @throws NotExistException
     *             if Station not exists.
     */
    Station getStationById(Long id);

    /**
     * The method returns entity {@code Station} of station by name.
     *
     * @param name
     *            - put station name.
     *
     * @return new {@code Station}.
     *
     * @throws NotExistException
     *             if station not exists.
     */
    Station getStationByName(String name);

    List<StationResponse> getAllByDistrictNameAndCityName(StationProfile stationProfile);

    /**
     * The method returns optional {@code Optional<Station>} of station by name.
     *
     * @param name
     *            - put station name.
     *
     * @return new {@code Optional<Station>}.
     *
     * @throws NotExistException
     *             if station not exists.
     */
    Optional<Station> getOptionalStationByName(String name);

    /**
     * The method returns dto {@code SuccessCreatedStation} if station successfully added.
     *
     * @param stationProfile
     *            - place place body of dto {@code StationProfile}.
     *
     * @return new {@code SuccessCreatedStation}.
     *
     * @throws AlreadyExistException
     *             if station already exists.
     */
    SuccessCreatedStation addStation(StationProfile stationProfile);

    /**
     * The method returns list of dto {@code List<StationResponse>} of all stations.
     *
     * @return new {@code List<StationResponse>}.
     */
    List<StationResponse> getListOfStations();

    /**
     * The method returns entity {@code Station} of station by id.
     *
     * @param name
     *            - put city name.
     *
     * @return new {@code List<StationResponse>}.
     */
    List<StationResponse> getListOfStationsByCityName(String name);

    /**
     * The method returns dto {@code StationProfile} of updated station.
     *
     * @param stationProfile
     *            - place body of dto {@code StationProfile}.
     *
     * @return new {@code StationProfile}.
     */
    StationProfile updateStation(Long id, StationProfile stationProfile);

    /**
     * The method deletes station and returns dto {@code StationResponse}.
     *
     * @param id
     *            - id of district to delete
     *
     * @return new {@code StationResponse}.
     *
     * @throws NotExistException
     *             {@link NotExistException} if the station doesn't exist.
     */
    StationResponse deleteStationById(Long id);
}
