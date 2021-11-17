package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StationController implements Api {
    private final StationService stationService;

    @Autowired
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
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
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
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
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

    /**
     * The controller returns list of dto {@code List<StationResponse>} of station.
     *
     * @param name - put city name.
     * @return new {@code List<StationResponse>}.
     */
    @GetMapping("/stations/{name}")
    public List<StationResponse> getStationsByCityName(@PathVariable String name) {
        return stationService.getListOfStationsByCityName(name);
    }

    /**
     * The controller returns dto {@code StationResponse} of deleted station.
     *
     * @param id - put station id.
     * @return new {@code StationResponse}.
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @DeleteMapping("/station/{id}")
    public StationResponse deleteStation(@PathVariable Long id) {
        return stationService.deleteStationById(id);
    }
}
