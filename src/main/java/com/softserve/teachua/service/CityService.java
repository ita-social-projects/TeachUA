package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;

import java.util.List;

public interface CityService {
    CityResponse getCityById(Long id);
    SuccessCreatedCity addCity(String name);
    List<CityResponse> getListOfCities();
}
