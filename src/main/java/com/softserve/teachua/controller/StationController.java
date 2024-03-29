package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.service.StationService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the stations.
 */

@RestController
@Tag(name = "station", description = "the Station API")
@SecurityRequirement(name = "api")
public class StationController implements Api {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * Use this endpoint to get station by id. The controller returns {@code StationResponse}.
     *
     * @param id - put station id.
     * @return {@code StationResponse}.
     */
    @GetMapping("/station/{id}")
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
    @PostMapping("/station")
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
    @PutMapping("/station/{id}")
    public StationProfile updateStation(@PathVariable Long id, @Valid @RequestBody StationProfile stationProfile) {
        return stationService.updateStation(id, stationProfile);
    }

    /**
     * Use this endpoint to get all stations. The controller returns list of {@code List<StationResponse>}.
     *
     * @return {@code List<StationResponse>}.
     */
    @GetMapping("/stations")
    public List<StationResponse> getStations() {
        return stationService.getListOfStations();
    }

    /**
     * Use this endpoint to get stations by name. The controller returns list of {@code List<StationResponse>}.
     *
     * @param name - put city name.
     * @return {@code List<StationResponse>}.
     */
    @GetMapping("/stations/{name}")
    public List<StationResponse> getStationsByCityName(@PathVariable String name) {
        return stationService.getListOfStationsByCityName(name);
    }

    @PostMapping("/district/stations")
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
    @DeleteMapping("/station/{id}")
    public StationResponse deleteStation(@PathVariable Long id) {
        return stationService.deleteStationById(id);
    }
}
