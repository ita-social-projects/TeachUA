package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.service.CityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name="city", description="the City API")
public class CityController implements Api {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Use this endpoint to get city by id.
     * The controller returns {@code CityResponse}.
     * @param id - put city id.
     * @return new {@code CityResponse}.
     */
    @GetMapping("/city/{id}")
    public CityResponse getCity(@PathVariable long id) {
        return cityService.getCityProfileById(id);
    }

    /**
     * Use this endpoint to create city.
     * The controller returns dto {@code SuccessCreatedCity} of created city.
     * @param cityProfile - place body to {@link CityProfile}.
     * @return new {@code SuccessCreatedCity}.
     */
    @PostMapping("/city")
    public SuccessCreatedCity addCity(
            @Valid
            @RequestBody CityProfile cityProfile) {
        return cityService.addCity(cityProfile);
    }

    /**
     * Use this endpoint to update city by id.
     * The controller returns dto {@code CityProfile} about city.
     * @param id - put city id here.
     * @param cityProfile - put city information here.
     * @return new {@code CityProfile}.
     */
    @PutMapping("/city/{id}")
    public CityProfile updateCity(
            @PathVariable Long id,
            @Valid
            @RequestBody CityProfile cityProfile){
        return cityService.updateCity(id, cityProfile);
    }

    /**
     * Use this endpoint to get all cities.
     * The controller returns list of {@code List<CityResponse>}.
     * @return new {@code List<CityResponse>}.
     */
    @GetMapping("/cities")
    public List<CityResponse> getCities() {
        return cityService.getListOfCities();
    }

    /**
     * Use this endpoint to delete city by id.
     * The controller returns list of dto {@code List<CityResponse>} of city.
     * @param id - put city id here.
     * @return new {@code List<CityResponse>}.
     */
    @DeleteMapping("/city/{id}")
    public CityResponse deleteCity(@PathVariable long id) {
        return cityService.deleteCityById(id);
    }
}
