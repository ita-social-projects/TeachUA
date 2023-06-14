package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.city.CityProfile;
import com.softserve.club.dto.city.CityResponse;
import com.softserve.club.dto.city.SuccessCreatedCity;
import com.softserve.club.service.CityService;
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
 * This controller is for managing the cities.
 */

@RestController
@RequestMapping("/api/v1/club/city")
public class CityController implements Api {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Use this endpoint to get city by id. The controller returns {@code CityResponse}.
     *
     * @param id
     *            - put city id.
     *
     * @return new {@code CityResponse}.
     */
    @GetMapping("/{id}")
    public CityResponse getCity(@PathVariable long id) {
        return cityService.getCityProfileById(id);
    }

    /**
     * Use this endpoint to create city. The controller returns dto {@code SuccessCreatedCity} of created city.
     *
     * @param cityProfile
     *            - place body to {@link CityProfile}.
     *
     * @return new {@code SuccessCreatedCity}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public SuccessCreatedCity addCity(@Valid @RequestBody CityProfile cityProfile) {
        return cityService.addCity(cityProfile);
    }

    /**
     * Use this endpoint to update city by id. The controller returns dto {@code CityProfile} about city.
     *
     * @param id
     *            - put city id here.
     * @param cityProfile
     *            - put city information here.
     *
     * @return new {@code CityProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public CityProfile updateCity(@PathVariable Long id, @Valid @RequestBody CityProfile cityProfile) {
        return cityService.updateCity(id, cityProfile);
    }

    /**
     * Use this endpoint to get all cities. The controller returns list of {@code List<CityResponse>}.
     *
     * @return new {@code List<CityResponse>}.
     */
    @GetMapping("/all")
    public List<CityResponse> getCities() {
        return cityService.getListOfCities();
    }

    /**
     * Use this endpoint to delete city by id. The controller returns list of dto {@code List<CityResponse>} of city.
     *
     * @param id
     *            - put city id here.
     *
     * @return new {@code List<CityResponse>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{id}")
    public CityResponse deleteCity(@PathVariable long id) {
        return cityService.deleteCityById(id);
    }
}
