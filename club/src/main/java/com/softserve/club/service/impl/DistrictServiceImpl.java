package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.club.dto.district.DistrictProfile;
import com.softserve.club.dto.district.DistrictResponse;
import com.softserve.club.dto.district.SuccessCreatedDistrict;
import com.softserve.club.model.District;
import com.softserve.club.repository.DistrictRepository;
import com.softserve.club.service.CityService;
import com.softserve.club.service.DistrictService;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DistrictServiceImpl implements DistrictService {
    private static final String DISTRICT_ALREADY_EXIST = "District already exist with name: %s";
    private static final String DISTRICT_NOT_FOUND_BY_NAME = "District not found by name: %s";
    private static final String DISTRICT_DELETING_ERROR = "Can't delete district cause of relationship";

    private final DtoConverter dtoConverter;
    private final CityService cityService;
    private final DistrictRepository districtRepository;
    private final ArchiveMQMessageProducer<District> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;
    private final ObjectMapper objectMapper;

    public DistrictServiceImpl(DtoConverter dtoConverter, CityService cityService,
                               DistrictRepository districtRepository,
                               ArchiveMQMessageProducer<District> archiveMQMessageProducer, ArchiveClient archiveClient,
                               ObjectMapper objectMapper) {
        this.dtoConverter = dtoConverter;
        this.cityService = cityService;
        this.districtRepository = districtRepository;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DistrictResponse getDistrictProfileById(Long id) {
        return dtoConverter.convertToDto(getDistrictById(id), DistrictResponse.class);
    }

    @Override
    public District getDistrictById(Long id) {
        Optional<District> optionalDistrict = id == null ? Optional.empty() : getOptionalDistrictById(id);
        if (optionalDistrict.isEmpty()) {
            return null;
        }

        District district = optionalDistrict.get();
        log.debug("**/getting district by id = " + district);
        return district;
    }

    @Override
    public District getDistrictByName(String name) {
        Optional<District> optionalDistrict = getOptionalDistrictByName(name);
        if (optionalDistrict.isEmpty()) {
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
        List<DistrictResponse> districtResponses = districtRepository.findAllByOrderByIdAsc().stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .toList();

        log.debug("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    @Override
    public List<DistrictResponse> getListOfDistrictsByCityName(String name) {
        List<DistrictResponse> districtResponses = districtRepository.findAllByCityName(name).stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .toList();

        log.debug("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    @Override
    public DistrictProfile updateDistrict(Long id, DistrictProfile districtProfile) {
        District district = getDistrictById(id);
        District newDistrict = dtoConverter.convertToEntity(districtProfile, district).withId(id)
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

    private void archiveModel(District district) {
        archiveMQMessageProducer.publish(district);
    }

    @Override
    public void restoreModel(Long id) {
        var district = objectMapper.convertValue(
                archiveClient.restoreModel(District.class.getName(), id),
                District.class);

        districtRepository.save(district);
    }
}
