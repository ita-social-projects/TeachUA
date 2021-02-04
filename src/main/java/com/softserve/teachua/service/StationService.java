package com.softserve.teachua.service;

import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.model.Station;

import java.util.List;

public interface StationService {
    StationResponse getStationProfileById(Long id);

    Station getStationById(Long id);

    Station getStationByName(String name);

    SuccessCreatedStation addStation(StationProfile stationProfile);

    List<StationResponse> getListOfStations();

    StationProfile updateStation(Long id, StationProfile stationProfile);
}
