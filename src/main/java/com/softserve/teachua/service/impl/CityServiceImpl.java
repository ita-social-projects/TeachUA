package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.CityDto;
import com.softserve.teachua.entity.City;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.service.CityService;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;
    private ModelMapper modelMapper;
    @Autowired
    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<CityDto> getAll() {
        List<City> city = cityRepository.findAll();
        List<CityDto> cityDtos = new ArrayList<>();
        for ( City c:city ){
            cityDtos.add(modelMapper.map(c,CityDto.class));
        }
        return cityDtos;
    }
    @Override
    public CityDto getCityById(Long id) {
        return modelMapper.map(cityRepository.getOne(id),CityDto.class);
    }
}
