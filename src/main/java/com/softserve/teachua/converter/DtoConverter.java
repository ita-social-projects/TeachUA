package com.softserve.teachua.converter;

import com.softserve.teachua.dto.marker.Convertible;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Class that helps to convert from DTO to Entity, from Entity to DTO and from DTO to DTO.
 *
 * Use @code convertToEntity(new Dto(), new Entity()) - to convert from DTO to Entity
 * Use @code convertToDto(new Entity(), Dto.class) - to convert from Entity to DTO
 * Use @code convertFromDtoToDto(new DtoConverted(), new DtoConverting()) - to convert from DTO to DTO
 *
 * All the converting classes(DTOs and Entities) must implement 'Convertible' interface.
 *
 * @author Denis Burko
 */
@Component
public class DtoConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public DtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T,D extends Convertible> D convertToEntity(T dto, D entity) {
        return modelMapper.map(dto, (Type) entity.getClass());
    }

    public <T,D extends Convertible> T convertToDto(D entity, Type dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <T,D extends Convertible> D convertFromDtoToDto(T dtoConverted, D dtoConverting) {
        return modelMapper.map(dtoConverted, (Type) dtoConverting.getClass());
    }
}
