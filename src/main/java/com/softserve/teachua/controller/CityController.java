package com.softserve.teachua.controller;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CityController {
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
     * @param name - enter city name.
     * @return new {@code SuccessCreatedCategory}.
     */
    @PostMapping("/city")
    public SuccessCreatedCity addCity(
            @Valid
            @RequestParam String name) {
        return cityService.addCity(name);
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

    //TODO
    @PutMapping("/city/{id}")
    public String updateCenter(@PathVariable long id) {
        return "City " + id + " is updated";
    }

    //TODO have to known we're archiving cities or deleting
    @DeleteMapping("/city/{id}")
    public String deleteCenter(@PathVariable long id) {
        return "City " + id + " is deleted";
    }
}
