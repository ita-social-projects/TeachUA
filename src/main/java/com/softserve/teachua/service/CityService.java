package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.dto.service.CityProfile;
import com.softserve.teachua.model.City;

import java.util.List;

public interface CityService {
    CityResponse getCityResponseById(Long id);

    City getCityById(Long id);

    City getCityByName(String name);

    SuccessCreatedCity addCity(String name);

    List<CityResponse> getListOfCities();

    CityProfile updateCity(CityProfile cityProfile);
}
