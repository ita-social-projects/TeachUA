package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.location.AddressProfile;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.AddressService;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class AddressServiceImpl implements AddressService {
    public static final String UKRAINIAN_ALPHABET = "[^а-яА-ЯіІїЇєЄ]+).*";
    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;

    @Autowired
    public AddressServiceImpl(LocationRepository locationRepository, CityRepository cityRepository) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<AddressProfile> getNotRelativeAddress() {
        List<Location> locationList = locationRepository.findAll().stream()
                .filter(location -> (hasAddressOtherCity(location.getAddress()))
                        && !location.getAddress().matches(".*(" + location.getCity().getName() + UKRAINIAN_ALPHABET)
                ).toList();

        List<AddressProfile> addressProfileList = new LinkedList<>();

        for (Location location : locationList) {
            addressProfileList.add(new AddressProfile()
                    .withId(location.getId())
                    .withAddressText(location.getAddress())
                    .withRealCity(cityRepository.findById(location.getCity().getId())
                            .orElseThrow(NotExistException::new).getName()));
        }

        return addressProfileList;
    }

    public List<AddressProfile> replaceAllIncorrectCity(List<AddressProfile> addressProfileList) {
        List<City> cities = cityRepository.findAll();

        addressProfileList.forEach(address -> cities.forEach(city -> {
            if (address.getAddressText().matches(".*(" + city.getName() + UKRAINIAN_ALPHABET)
                    || address.getAddressText().matches("^" + city.getName() + "$")) {
                Location location =
                        locationRepository.findById(address.getId()).orElseThrow(NotExistException::new).withCity(city);
                address.setRealCity(locationRepository.save(location).getCity().getName());
            }
        }));
        return addressProfileList;
    }

    public boolean hasAddressOtherCity(String address) {
        AtomicBoolean hasCity = new AtomicBoolean(false);

        cityRepository.findAll().forEach(city -> {
            if ((city != null && !city.getName().isEmpty())
                    && (address != null && address.contains(city.getName())
                    && (address.matches(".*(" + city.getName() + UKRAINIAN_ALPHABET)))
                    || Objects.requireNonNull(address).matches("^" + city.getName() + "$")) {
                hasCity.set(true);
            }
        });

        return hasCity.get();
    }
}
