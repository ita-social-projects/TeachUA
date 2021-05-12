package com.softserve.teachua.service;

import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.model.Station;

import java.util.List;
import java.util.Optional;

public interface StationService {
    StationResponse getStationProfileById(Long id);

    Station getStationById(Long id);

    Station getStationByName(String name);

    Optional<Station> getOptionalStationByName(String name);

    SuccessCreatedStation addStation(StationProfile stationProfile);

    List<StationResponse> getListOfStations();

    List<StationResponse> getListOfStationsByCityName(String name);

    StationProfile updateStation(Long id, StationProfile stationProfile);

    StationResponse deleteStationById(Long id);
}
