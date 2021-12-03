package com.softserve.teachua.converter;

import com.softserve.teachua.dto.marker.Convertible;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Class that helps to convert from DTO to Entity, from Entity to DTO and from DTO to DTO.
 * <p>Use @code convertToEntity(new Dto(), new Entity()) - to convert from DTO to Entity
 * Use @code convertToDto(new Entity(), Dto.class) - to convert from Entity to DTO
 * Use @code convertFromDtoToDto(new DtoConverted(), new DtoConverting()) - to convert from DTO to DTO
 * </p>
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

    /**
     * Convert to object that is not a DTO (DB model for example).
     *
     * @param dto    - put DTO
     * @param entity - entity object in which DTO will be converted
     * @param <T>    - DTO type
     * @param <D>    - entity type
     * @return object of {@code <D>} type
     */
    public <T, D extends Convertible> D convertToEntity(T dto, D entity) {
        return modelMapper.map(dto, (Type) entity.getClass());
    }

    /**
     * Convert to DTO (data transfer object).
     *
     * @param entity   - put entity object
     * @param dtoClass - put {@code Type.class} of DTO (example: {@code ClubResponse.class})
     * @param <T>      - DTO type
     * @param <D>      - entity type
     * @return object of DTO class
     */
    public <T, D extends Convertible> T convertToDto(D entity, Type dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    /**
     * Convert from one DTO to another.
     *
     * @param dtoConverted  - object of source DTO
     * @param dtoConverting - object of target DTO
     * @param <T>           - to be converted type
     * @param <D>           - result type
     * @return object of another DTO
     */
    public <T, D extends Convertible> D convertFromDtoToDto(T dtoConverted, D dtoConverting) {
        return modelMapper.map(dtoConverted, (Type) dtoConverting.getClass());
    }
}
