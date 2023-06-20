package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.city.CityProfile;
import com.softserve.club.dto.station.StationProfile;
import com.softserve.club.dto.station.StationResponse;
import com.softserve.club.dto.station.SuccessCreatedStation;
import com.softserve.club.service.StationService;
import com.softserve.club.util.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the stations.
 */
@RestController
@RequestMapping("/api/v1/club/station")
public class StationController implements Api {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * Use this endpoint to get station by id. The controller returns {@code StationResponse}.
     *
     * @param id - put station id.
     * @return {@code StationResponse}.
     */
    @GetMapping("/{id}")
    public StationResponse getStation(@PathVariable long id) {
        return stationService.getStationProfileById(id);
    }

    /**
     * Use this endpoint to create station. The controller returns {@code SuccessCreatedStation}.
     *
     * @param stationProfile - place body to {@code StationProfile}.
     * @return new {@code SuccessCreatedStation}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public SuccessCreatedStation addStation(@Valid @RequestBody StationProfile stationProfile) {
        return stationService.addStation(stationProfile);
    }

    /**
     * Use this endpoint to update station by id. The controller returns {@code StationProfile}.
     *
     * @param id             - put station id.
     * @param stationProfile - place body to {@link CityProfile}.
     * @return {@code StationProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public StationProfile updateStation(@PathVariable Long id, @Valid @RequestBody StationProfile stationProfile) {
        return stationService.updateStation(id, stationProfile);
    }

    /**
     * Use this endpoint to get all stations. The controller returns list of {@code List<StationResponse>}.
     *
     * @return {@code List<StationResponse>}.
     */
    @GetMapping
    public List<StationResponse> getStations() {
        return stationService.getListOfStations();
    }

    /**
     * Use this endpoint to get stations by name. The controller returns list of {@code List<StationResponse>}.
     *
     * @param name - put city name.
     * @return {@code List<StationResponse>}.
     */
    @GetMapping("/{name}")
    public List<StationResponse> getStationsByCityName(@PathVariable String name) {
        return stationService.getListOfStationsByCityName(name);
    }

    @PostMapping("/district")
    public List<StationResponse> getStationsByDistrictNameAndCityName(@RequestBody StationProfile stationProfile) {
        return stationService.getAllByDistrictNameAndCityName(stationProfile);
    }

    /**
     * Use this endpoint to delete station by id. The controller returns {@code StationResponse}.
     *
     * @param id - put station id.
     * @return {@code StationResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{id}")
    public StationResponse deleteStation(@PathVariable Long id) {
        return stationService.deleteStationById(id);
    }
}
