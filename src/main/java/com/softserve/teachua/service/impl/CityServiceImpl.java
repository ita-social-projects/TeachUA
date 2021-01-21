package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.model.City;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityResponse getCityById(Long id) {
        City city = cityRepository.getById(id);

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .clubs(city.getClubs())
                .build();
    }

    @Override
    public SuccessCreatedCity addCity(String name) {
        City city = cityRepository.save(City
                .builder()
                .name(name)
                .build());

        return SuccessCreatedCity.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public List<CityResponse> getListOfCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> new CityResponse(city.getId(), city.getName(), city.getClubs()))
                .collect(Collectors.toList());
    }
}
