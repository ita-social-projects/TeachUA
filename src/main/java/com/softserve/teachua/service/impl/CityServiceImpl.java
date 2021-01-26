package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityServiceImpl implements CityService {
    private static final String CITY_ALREADY_EXIST = "City already exist with name: %s";
    private static final String CITY_NOT_FOUND_BY_ID = "City not found by id: %s";
    private static final String CITY_NOT_FOUND_BY_NAME = "City not found by name: %s";


    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityResponse getCityProfileById(Long id) {
        City city = getCityById(id);
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public City getCityById(Long id) {
        if (!isCityExistById(id)) {
            String cityNotFoundById = String.format(CITY_NOT_FOUND_BY_ID, id);
            log.error(cityNotFoundById);
            throw new NotExistException(cityNotFoundById);
        }

        City city = cityRepository.getById(id);
        log.info("**/getting city by id = " + city);
        return city;
    }

    @Override
    public City getCityByName(String name) {
        if (!isCityExistByName(name)) {
            String cityNotFoundById = String.format(CITY_NOT_FOUND_BY_NAME, name);
            log.error(cityNotFoundById);
            throw new NotExistException(cityNotFoundById);
        }

        City city = cityRepository.findByName(name);
        log.info("**/getting city by id = " + city);
        return city;
    }

    @Override
    public SuccessCreatedCity addCity(String name) {
        if (isCityExistByName(name)) {
            String cityAlreadyExist = String.format(CITY_ALREADY_EXIST, name);
            log.error(cityAlreadyExist);
            throw new AlreadyExistException(cityAlreadyExist);
        }

        City city = cityRepository.save(City
                .builder()
                .name(name)
                .build());

        log.info("**/adding new city = " + city);
        return SuccessCreatedCity.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public List<CityResponse> getListOfCities() {
        List<CityResponse> cityResponses = cityRepository.findAll()
                .stream()
                .map(city -> new CityResponse(city.getId(), city.getName()))
                .collect(Collectors.toList());

        log.info("**/getting list of cities = " + cityResponses);
        return cityResponses;
    }

    private boolean isCityExistById(Long id) {
        return cityRepository.existsById(id);
    }
    private boolean isCityExistByName(String name) {
        return cityRepository.existsByName(name);
    }
}
