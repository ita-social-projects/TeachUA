package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.location.AddressProfile;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AddressServiceImpl implements AddressService {

    private final LocationRepository locationRepository;
    private  final CityRepository cityRepository;

    @Autowired
    public AddressServiceImpl(LocationRepository locationRepository, CityRepository cityRepository) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<AddressProfile> getNotRelativeAddress() {

        List<Location> locationList = locationRepository
                .findAll()
                .stream()
                .filter(location ->
//                        !location
//                        .getAddress()
//                        .contains(location.getCity().getName())
//                        &&
//                        !location.getAddress().matches(".*("+location.getCity().getName()+")[^а-яА-ЯіІїЇєЄ].+")
//                        &&
                                ( hasAddressAnyCity(location.getAddress())
                                        &&
                                        !location.getAddress().contains(location.getCity().getName()))
                            || location.getAddress().isEmpty()

                ).collect(Collectors.toList());
//

        System.out.println(locationList.size());
        List<AddressProfile> addressProfileList = new LinkedList<>();

        for (Location location: locationList) {
            addressProfileList.add(new AddressProfile()
                    .withId(location.getId())
                    .withAddressText(location.getAddress())
                    .withRealCity(cityRepository.getOne(location.getCity().getId()).getName()));
        }


        return addressProfileList;
    }




    public boolean hasAddressAnyCity(String address){

        AtomicBoolean hasCity = new AtomicBoolean(false);

        cityRepository.findAll().forEach(city -> {
            if (address.contains(city.getName()) && (address.matches(".*("+city.getName()+")[^а-яА-ЯіІїЇєЄ].*"))) {
                hasCity.set(true);
            }
        });

        return hasCity.get();
    }

}
