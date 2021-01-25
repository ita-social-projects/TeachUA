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

    @GetMapping("/city/{id}")
    public CityResponse getCity(@PathVariable long id) {
        return cityService.getCityProfileById(id);
    }

    @PostMapping("/city")
    public SuccessCreatedCity addCity(
            @Valid
            @RequestParam String name) {
        return cityService.addCity(name);
    }

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

    //TODO
    @DeleteMapping("/cities")
    public String deleteCenteres() {
        return "Centeres was deleted";
    }

    //TODO
    @PutMapping("/cities")
    public String updateCenteres() {
        return "Centres was updated";
    }
}
