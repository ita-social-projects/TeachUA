package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.service.StationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name="station", description="the Station API")
@SecurityRequirement(name = "api")
public class StationController implements Api {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * Use this endpoint to get station by id.
     * The controller returns {@code StationResponse}.
     * @param id - put station id.
     * @return {@code StationResponse}.
     */
    @GetMapping("/station/{id}")
    public StationResponse getStation(@PathVariable long id) {
        return stationService.getStationProfileById(id);
    }

    /**
     * Use this endpoint to create station.
     * The controller returns {@code SuccessCreatedStation}.
     * @param stationProfile - place body to {@code StationProfile}.
     * @return new {@code SuccessCreatedStation}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/station")
    public SuccessCreatedStation addStation(
            @Valid
            @RequestBody StationProfile stationProfile) {
        return stationService.addStation(stationProfile);
    }

    /**
     * Use this endpoint to update station by id.
     * The controller returns {@code StationProfile}.
     * @param id             - put station id.
     * @param stationProfile - place body to {@link CityProfile}.
     * @return {@code StationProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/station/{id}")
    public StationProfile updateStation(
            @PathVariable Long id,
            @Valid
            @RequestBody StationProfile stationProfile) {
        return stationService.updateStation(id, stationProfile);
    }

    /**
     * Use this endpoint to get all stations.
     * The controller returns list of {@code List<StationResponse>}.
     * @return {@code List<StationResponse>}.
     */
    @GetMapping("/stations")
    public List<StationResponse> getStations() {
        return stationService.getListOfStations();
    }

    /**
     * Use this endpoint to get stations by name.
     * The controller returns list of {@code List<StationResponse>}.
     * @param name - put city name.
     * @return {@code List<StationResponse>}.
     */
    @GetMapping("/stations/{name}")
    public List<StationResponse> getStationsByCityName(@PathVariable String name) {
        return stationService.getListOfStationsByCityName(name);
    }

    /**
     * Use this endpoint to delete station by id.
     * The controller returns {@code StationResponse}.
     * @param id - put station id.
     * @return {@code StationResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/station/{id}")
    public StationResponse deleteStation(@PathVariable Long id) {
        return stationService.deleteStationById(id);
    }
}
