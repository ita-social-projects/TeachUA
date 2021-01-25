package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.User;
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
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityResponse getCityProfileById(Long id) {
        if (!isCityExistById(id)) {
            String cityNotFoundById = String.format("City not found by id %s", id);
            log.error(cityNotFoundById);
            throw new NotExistException(cityNotFoundById);
        }

        City city = getCityById(id);
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.getById(id);
    }

    @Override
    public City getCityByName(String name) {
        return cityRepository.findByName(name);
    }

    @Override
    public SuccessCreatedCity addCity(String name) {
        if (isCityExistByName(name)) {
            String cityAlreadyExist = String.format("City already exist by name %s", name);
            log.error(cityAlreadyExist);
            throw new AlreadyExistException(cityAlreadyExist);
        }

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
                .map(city -> new CityResponse(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }

    private boolean isCityExistById(Long id) {
        return getCityById(id) != null;
    }

    private boolean isCityExistByName(String name) {
        return getCityByName(name) != null;
    }
}
