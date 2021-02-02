package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.controller.CityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCity;
import com.softserve.teachua.dto.service.CityProfile;
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

    private final DtoConverter dtoConverter;
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(DtoConverter dtoConverter, CityRepository cityRepository) {
        this.dtoConverter = dtoConverter;
        this.cityRepository = cityRepository;
    }

    /**
     * The method returns dto {@code CityResponse} of city by id.
     *
     * @param id - put city id.
     * @return new {@code CityResponse}.
     */
    @Override
    public CityResponse getCityProfileById(Long id) {
        return dtoConverter.convertToDto(getCityById(id), CityResponse.class);
    }

    /**
     * The method returns entity {@code City} of city by id.
     *
     * @param id - put city id.
     * @return new {@code City}.
     * @throws NotExistException if city not exists.
     */
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

    /**
     * The method returns entity {@code City} of city by name.
     *
     * @param name - put center name.
     * @return new {@code City}.
     * @throws NotExistException if city not exists.
     */
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

    /**
     * The method returns dto {@code SuccessCreatedCity} if city successfully added.
     *
     * @param name - place city name.
     * @return new {@code SuccessCreatedCity}.
     * @throws AlreadyExistException if city already exists.
     */
    @Override
    public SuccessCreatedCity addCity(String name) {
        if (isCityExistByName(name)) {
            String cityAlreadyExist = String.format(CITY_ALREADY_EXIST, name);
            log.error(cityAlreadyExist);
            throw new AlreadyExistException(cityAlreadyExist);
        }

        City city = cityRepository.save(new City(name));

        log.info("**/adding new city = " + city);
        return dtoConverter.convertToDto(city, SuccessCreatedCity.class);
    }

    /**
     * The method returns list of dto {@code List<CityResponse>} of all cities.
     *
     * @return new {@code List<CityResponse>}.
     */
    @Override
    public List<CityResponse> getListOfCities() {
        List<CityResponse> cityResponses = cityRepository.findAll()
                .stream()
                .map(city -> (CityResponse) dtoConverter.convertToDto(city, CityResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of cities = " + cityResponses);
        return cityResponses;
    }

    /**
     * The method returns dto {@code CityProfile} of updated club.
     *
     * @param cityProfile - place body of dto {@code CityProfile}.
     * @return new {@code CityProfile}.
     */
    @Override
    public CityProfile updateCity(CityProfile cityProfile) {
        City city = cityRepository.save(dtoConverter.convertToEntity(cityProfile, new City()));
        log.info("**/updating city = " + city);
        return dtoConverter.convertToDto(city, CityProfile.class);
    }

    private boolean isCityExistById(Long id) {
        return cityRepository.existsById(id);
    }

    private boolean isCityExistByName(String name) {
        return cityRepository.existsByName(name);
    }
}
