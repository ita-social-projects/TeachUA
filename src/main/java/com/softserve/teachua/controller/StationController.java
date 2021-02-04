package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.service.StationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StationController implements Api {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * The controller returns dto {@code StationResponse} of station.
     *
     * @param id - put station id.
     * @return new {@code StationResponse}.
     */
    @GetMapping("/station/{id}")
    public StationResponse getStation(@PathVariable long id) {
        return stationService.getStationProfileById(id);
    }

    /**
     * The controller returns dto {@code SuccessCreatedStation} of created station.
     *
     * @param stationProfile - place body to {@link StationProfile}.
     * @return new {@code SuccessCreatedStation}.
     */
    @PostMapping("/station")
    public SuccessCreatedStation addStation(
            @Valid
            @RequestBody StationProfile stationProfile) {
        return stationService.addStation(stationProfile);
    }

    /**
     * The controller returns dto {@code StationProfile} about updated Station.
     *
     * @param id             - put station id.
     * @param stationProfile - place body to {@link CityProfile}.
     * @return new {@code StationProfile}.
     */
    @PutMapping("/station/{id}")
    public StationProfile updateStation(
            @PathVariable Long id,
            @Valid
            @RequestBody StationProfile stationProfile) {
        return stationService.updateStation(id, stationProfile);
    }

    /**
     * The controller returns list of dto {@code List<StationResponse>} of stations.
     *
     * @return new {@code List<StationResponse>}.
     */
    @GetMapping("/stations")
    public List<StationResponse> getStations() {
        return stationService.getListOfStations();
    }
}
