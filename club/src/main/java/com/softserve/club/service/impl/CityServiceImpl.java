package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.club.dto.city.CityProfile;
import com.softserve.club.dto.city.CityResponse;
import com.softserve.club.dto.city.SuccessCreatedCity;
import com.softserve.club.model.City;
import com.softserve.club.repository.CityRepository;
import com.softserve.club.service.CityService;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CityServiceImpl implements CityService/*, ArchiveMark<City>*/ {
    private static final String CITY_ALREADY_EXIST = "City already exist with name: %s";
    private static final String CITY_NOT_FOUND_BY_ID = "City not found by id: %s";
    private static final String CITY_NOT_FOUND_BY_NAME = "City not found by name: %s";
    private static final String CITY_DELETING_ERROR = "Can't delete city cause of relationship";

    private final DtoConverter dtoConverter;
    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CityServiceImpl(DtoConverter dtoConverter, CityRepository cityRepository,
            ObjectMapper objectMapper) {
        this.dtoConverter = dtoConverter;
        this.cityRepository = cityRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public CityResponse getCityProfileById(Long id) {
        return dtoConverter.convertToDto(getCityById(id), CityResponse.class);
    }

    @Override
    public City getCityById(Long id) {
        Optional<City> optionalCity = getOptionalCityById(id);
        if (optionalCity.isEmpty()) {
            throw new NotExistException(String.format(CITY_NOT_FOUND_BY_ID, id));
        }
        City city = optionalCity.get();
        log.debug("**/getting city by id = " + city);
        return city;
    }

    @Override
    public City getCityByName(String name) {
        Optional<City> optionalCity = getOptionalCityByName(name);
        if (optionalCity.isEmpty()) {
            throw new NotExistException(String.format(CITY_NOT_FOUND_BY_NAME, name));
        }

        City city = optionalCity.get();
        log.debug("**/getting city by id = " + city);
        return city;
    }

    @Override
    public SuccessCreatedCity addCity(CityProfile cityProfile) {
        if (isCityExistByName(cityProfile.getName())) {
            throw new AlreadyExistException(String.format(CITY_ALREADY_EXIST, cityProfile.getName()));
        }
        City city = cityRepository.save(dtoConverter.convertToEntity(cityProfile, new City()));
        log.debug("**/adding new city = " + city);
        return dtoConverter.convertToDto(city, SuccessCreatedCity.class);
    }

    @Override
    public List<CityResponse> getListOfCities() {
        List<CityResponse> cityResponses = cityRepository.findAllByOrderByIdAsc().stream()
                .map(city -> (CityResponse) dtoConverter.convertToDto(city, CityResponse.class))
                .toList();

        log.debug("**/getting list of cities = " + cityResponses);
        return cityResponses;
    }

    @Override
    public CityProfile updateCity(Long id, CityProfile cityProfile) {
        City city = getCityById(id);
        City newCity = dtoConverter.convertToEntity(cityProfile, city).withId(id);

        log.debug("**/updating city by id = " + newCity);
        return dtoConverter.convertToDto(cityRepository.save(newCity), CityProfile.class);
    }

    @Override
    public CityResponse deleteCityById(Long id) {
        City city = getCityById(id);

        try {
            cityRepository.deleteById(id);
            cityRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CITY_DELETING_ERROR);
        }

        //archiveModel(city);

        log.debug("city {} was successfully deleted", city);
        return dtoConverter.convertToDto(city, CityResponse.class);
    }

    private boolean isCityExistByName(String name) {
        return cityRepository.existsByName(name);
    }

    private Optional<City> getOptionalCityById(Long id) {
        return cityRepository.findById(id);
    }

    private Optional<City> getOptionalCityByName(String name) {
        return cityRepository.findByName(name);
    }

    //todo
    /*
    @Override
    public void archiveModel(City city) {
        archiveService.saveModel(dtoConverter.convertToDto(city, CityArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        CityArch cityArch = objectMapper.readValue(archiveObject, CityArch.class);
        City city = dtoConverter.convertToEntity(cityArch, City.builder().build());
        cityRepository.save(city);
    }
    */
}
