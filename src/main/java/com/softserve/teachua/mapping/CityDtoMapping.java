package com.softserve.teachua.mapping;

import com.softserve.teachua.dto.CityDto;
import com.softserve.teachua.entity.City;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class CityDtoMapping extends AbstractConverter<City, CityDto> {
    @Override
    protected CityDto convert(City city) {
        return CityDto.builder()
            .id(city.getId())
            .city(city.getCity())
            .build();
    }
}
