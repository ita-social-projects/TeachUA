package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.District;
import com.softserve.teachua.model.archivable.DistrictArch;
import com.softserve.teachua.repository.DistrictRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.DistrictService;
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
public class DistrictServiceImpl implements DistrictService, ArchiveMark<District> {
    private static final String DISTRICT_ALREADY_EXIST = "District already exist with name: %s";
    private static final String DISTRICT_NOT_FOUND_BY_ID = "District not found by id: %s";
    private static final String DISTRICT_NOT_FOUND_BY_NAME = "District not found by name: %s";
    private static final String DISTRICT_DELETING_ERROR = "Can't delete district cause of relationship";

    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final CityService cityService;
    private final DistrictRepository districtRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DistrictServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService,
                               CityService cityService, DistrictRepository districtRepository, ObjectMapper objectMapper) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.cityService = cityService;
        this.districtRepository = districtRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public DistrictResponse getDistrictProfileById(Long id) {
        return dtoConverter.convertToDto(getDistrictById(id), DistrictResponse.class);
    }

    @Override
    public District getDistrictById(Long id) {
        Optional<District> optionalDistrict = id == null ? Optional.empty() : getOptionalDistrictById(id);
        if (!optionalDistrict.isPresent()) {
            return null;
            // throw new NotExistException(String.format(DISTRICT_NOT_FOUND_BY_ID, id));
        }

        District district = optionalDistrict.get();
        log.debug("**/getting district by id = " + district);
        return district;
    }

    @Override
    public District getDistrictByName(String name) {
        Optional<District> optionalDistrict = getOptionalDistrictByName(name);
        if (!optionalDistrict.isPresent()) {
            throw new NotExistException(String.format(DISTRICT_NOT_FOUND_BY_NAME, name));
        }

        District district = optionalDistrict.get();
        log.debug("**/getting district by id = " + district);
        return district;
    }

    @Override
    public Optional<District> getOptionalDistrictByName(String name) {
        return districtRepository.findFirstByName(name);
    }

    @Override
    public SuccessCreatedDistrict addDistrict(DistrictProfile districtProfile) {
        if (isDistrictExistByName(districtProfile.getName())) {
            throw new AlreadyExistException(String.format(DISTRICT_ALREADY_EXIST, districtProfile.getName()));
        }

        District district = districtRepository.save(dtoConverter.convertToEntity(districtProfile, new District())
                .withCity(cityService.getCityByName(districtProfile.getCityName())));

        log.debug("**/adding new district = " + district);
        return dtoConverter.convertToDto(district, SuccessCreatedDistrict.class);
    }

    @Override
    public List<DistrictResponse> getListOfDistricts() {
        List<DistrictResponse> districtResponses = districtRepository.findAllByOrderByIdAsc()
                .stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    @Override
    public List<DistrictResponse> getListOfDistrictsByCityName(String name) {
        List<DistrictResponse> districtResponses = districtRepository.findAllByCityName(name)
                .stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    @Override
    public DistrictProfile updateDistrict(Long id, DistrictProfile districtProfile) {
        District district = getDistrictById(id);
        District newDistrict = dtoConverter.convertToEntity(districtProfile, district)
                .withId(id)
                .withCity(cityService.getCityByName(districtProfile.getCityName()));

        log.debug("**/updating district by id = " + newDistrict);
        return dtoConverter.convertToDto(districtRepository.save(newDistrict), DistrictProfile.class);
    }

    @Override
    public DistrictResponse deleteDistrictById(Long id) {
        District district = getDistrictById(id);

        try {
            districtRepository.deleteById(id);
            districtRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(DISTRICT_DELETING_ERROR);
        }

        archiveModel(district);

        log.debug("district {} was successfully deleted", district);
        return dtoConverter.convertToDto(district, DistrictResponse.class);
    }

    private boolean isDistrictExistByName(String name) {
        return districtRepository.existsByName(name);
    }

    private Optional<District> getOptionalDistrictById(Long id) {
        return districtRepository.findById(id);
    }

    @Override
    public void archiveModel(District district) {
        archiveService.saveModel(dtoConverter.convertToDto(district, DistrictArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        DistrictArch districtArch = objectMapper.readValue(archiveObject, DistrictArch.class);
        District district = dtoConverter.convertToEntity(districtArch, District.builder().build())
                .withId(null)
                .withCity(cityService.getCityById(districtArch.getCityId()));
        districtRepository.save(district);
    }
}
