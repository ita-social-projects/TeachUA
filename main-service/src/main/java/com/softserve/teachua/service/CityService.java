package com.softserve.teachua.service;

import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.City;
import java.util.List;

/**
 * This interface contains all needed methods to manage cities using CityRepository.
 */

public interface CityService {
    /**
     * The method returns dto {@code CityResponse} of city by id.
     *
     * @param id
     *            - put city id.
     *
     * @return new {@code CityResponse}.
     */
    CityResponse getCityProfileById(Long id);

    /**
     * The method returns entity {@code City} of city by id.
     *
     * @param id
     *            - put city id.
     *
     * @return new {@code City}.
     *
     * @throws NotExistException
     *             if city not exists.
     */
    City getCityById(Long id);

    /**
     * The method returns entity {@code City} of city by name.
     *
     * @param name
     *            - put center name.
     *
     * @return new {@code City}.
     *
     * @throws NotExistException
     *             if city not exists.
     */
    City getCityByName(String name);

    /**
     * The method returns dto {@code SuccessCreatedCity} if city successfully added.
     *
     * @param cityProfile
     *            - place place body of dto {@code CityProfile}.
     *
     * @return new {@code SuccessCreatedCity}.
     *
     * @throws AlreadyExistException
     *             if city already exists.
     */
    SuccessCreatedCity addCity(CityProfile cityProfile);

    /**
     * The method returns list of dto {@code List<CityResponse>} of all cities.
     *
     * @return new {@code List<CityResponse>}.
     */
    List<CityResponse> getListOfCities();

    /**
     * The method returns dto {@code CityProfile} of updated city.
     *
     * @param cityProfile
     *            - place body of dto {@code CityProfile}.
     *
     * @return new {@code CityProfile}.
     */
    CityProfile updateCity(Long id, CityProfile cityProfile);

    /**
     * The method deletes entity {@code City} and returns dto {@code CityResponse} of deleted city by id.
     *
     * @param id
     *            - id of city to delete
     *
     * @return CityResponse {@link CityResponse}.
     *
     * @throws NotExistException
     *             {@link NotExistException} if the city doesn't exist.
     */
    CityResponse deleteCityById(Long id);
}
