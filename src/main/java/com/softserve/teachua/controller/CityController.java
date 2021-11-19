package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CityController implements Api {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * The controller returns dto {@code CityResponse} about city.
     *
     * @param id - put city id.
     * @return new {@code CityResponse}.
     */
    @GetMapping("/city/{id}")
    public CityResponse getCity(@PathVariable long id) {
        return cityService.getCityProfileById(id);
    }

    /**
     * The controller returns dto {@code SuccessCreatedCity} of created city.
     *
     * @param cityProfile - place body to {@link CityProfile}.
     * @return new {@code SuccessCreatedCity}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/city")
    public SuccessCreatedCity addCity(
            @Valid
            @RequestBody CityProfile cityProfile) {
        return cityService.addCity(cityProfile);
    }

    /**
     * The controller returns dto {@code CityProfile} about city.
     *
     * @return new {@code CityProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/city/{id}")
    public CityProfile updateCity(
            @PathVariable Long id,
            @Valid
            @RequestBody CityProfile cityProfile) {
        return cityService.updateCity(id, cityProfile);
    }

    /**
     * The controller returns list of dto {@code List<CityResponse>} of city.
     *
     * @return new {@code List<CityResponse>}.
     */
    @GetMapping("/cities")
    public List<CityResponse> getCities() {
        return cityService.getListOfCities();
    }

    
    /**
     * Use this endpoint to archive city.
     *
     * @param id - put city id in path
     * @return - dto of deleted city
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/city/{id}")
    public CityResponse deleteCity(@PathVariable long id) {
        return cityService.deleteCityById(id);
    }
}
