package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CityServiceImpl implements CityService {
    private static final String CITY_ALREADY_EXIST = "City already exist with name: %s";
    private static final String CITY_NOT_FOUND_BY_ID = "City not found by id: %s";
    private static final String CITY_NOT_FOUND_BY_NAME = "City not found by name: %s";
    private static final String CITY_DELETING_ERROR = "Can't city role cause of relationship";

    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService, CityRepository cityRepository) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
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
        Optional<City> optionalCity = getOptionalCityById(id);
        if (!optionalCity.isPresent()) {
            throw new NotExistException(String.format(CITY_NOT_FOUND_BY_ID, id));
        }

        City city = optionalCity.get();
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
        Optional<City> optionalCity = getOptionalCityByName(name);
        if (!optionalCity.isPresent()) {
            throw new NotExistException(String.format(CITY_NOT_FOUND_BY_NAME, name));
        }

        City city = optionalCity.get();
        log.info("**/getting city by id = " + city);
        return city;
    }

    /**
     * The method returns dto {@code SuccessCreatedCity} if city successfully added.
     *
     * @param cityProfile - place place body of dto {@code CityProfile}.
     * @return new {@code SuccessCreatedCity}.
     * @throws AlreadyExistException if city already exists.
     */
    @Override
    public SuccessCreatedCity addCity(CityProfile cityProfile) {
        if (isCityExistByName(cityProfile.getName())) {
            throw new AlreadyExistException(String.format(CITY_ALREADY_EXIST, cityProfile.getName()));
        }
        City city = cityRepository.save(dtoConverter.convertToEntity(cityProfile, new City()));
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
     * The method returns dto {@code CityProfile} of updated city.
     *
     * @param cityProfile - place body of dto {@code CityProfile}.
     * @return new {@code CityProfile}.
     */
    @Override
    public CityProfile updateCity(Long id, CityProfile cityProfile) {
        City city = getCityById(id);
        City newCity = dtoConverter.convertToEntity(cityProfile, city)
                .withId(id);

        log.info("**/updating city by id = " + newCity);
        return dtoConverter.convertToDto(cityRepository.save(newCity), CenterProfile.class);
    }

    /**
     * The method deletes city {@link  City}
     *
     * @param id - id of city to delete
     * @return CityResponse {@link  CityResponse}.
     * @throws NotExistException {@link NotExistException} if the city doesn't exist.
     */
    @Override
    public CityResponse deleteCityById(Long id) {
        City city = getCityById(id);

        archiveService.saveModel(city);

        try {
            cityRepository.deleteById(id);
            cityRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CITY_DELETING_ERROR);
        }

        log.info("city {} was successfully deleted", city);
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
}
