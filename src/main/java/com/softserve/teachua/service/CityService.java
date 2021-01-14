package com.softserve.teachua.service;

import com.softserve.teachua.dto.CityDto;
import com.softserve.teachua.entity.City;
import java.util.List;

public interface CityService {
    List<CityDto> getAll();
    CityDto getCityById(Long id);
}
