package com.softserve.teachua.service;

import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.model.City;

import java.util.List;

public interface CityService {
    CityResponse getCityProfileById(Long id);

    City getCityById(Long id);

    City getCityByName(String name);

    SuccessCreatedCity addCity(CityProfile cityProfile);

    List<CityResponse> getListOfCities();

    CityProfile updateCity(Long id, CityProfile cityProfile);

    CityResponse deleteCityById(Long id);
}
