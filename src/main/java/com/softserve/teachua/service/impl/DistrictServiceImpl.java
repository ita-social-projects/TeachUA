package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.District;
import com.softserve.teachua.repository.DistrictRepository;
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
public class DistrictServiceImpl implements DistrictService {
    private static final String DISTRICT_ALREADY_EXIST = "District already exist with name: %s";
    private static final String DISTRICT_NOT_FOUND_BY_ID = "District not found by id: %s";
    private static final String DISTRICT_NOT_FOUND_BY_NAME = "District not found by name: %s";
    private static final String DISTRICT_DELETING_ERROR = "Can't delete district cause of relationship";

    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final CityService cityService;
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService, CityService cityService, DistrictRepository districtRepository) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.cityService = cityService;
        this.districtRepository = districtRepository;
    }

    /**
     * The method returns dto {@code DistrictResponse} of district by id.
     *
     * @param id - put district id.
     * @return new {@code DistrictResponse}.
     */
    @Override
    public DistrictResponse getDistrictProfileById(Long id) {
        return dtoConverter.convertToDto(getDistrictById(id), DistrictResponse.class);
    }

    /**
     * The method returns entity {@code District} of district by id.
     *
     * @param id - put district id.
     * @return new {@code District}.
     * @throws NotExistException if district not exists.
     */
    @Override
    public District getDistrictById(Long id) {
        Optional<District> optionalDistrict = getOptionalDistrictById(id);
        if (!optionalDistrict.isPresent()) {
            throw new NotExistException(String.format(DISTRICT_NOT_FOUND_BY_ID, id));
        }

        District district = optionalDistrict.get();
        log.info("**/getting district by id = " + district);
        return district;
    }

    /**
     * The method returns entity {@code District} of district by name.
     *
     * @param name - put center name.
     * @return new {@code District}.
     * @throws NotExistException if district not exists.
     */
    @Override
    public District getDistrictByName(String name) {
        Optional<District> optionalDistrict = getOptionalDistrictByName(name);
        if (!optionalDistrict.isPresent()) {
            throw new NotExistException(String.format(DISTRICT_NOT_FOUND_BY_NAME, name));
        }

        District district = optionalDistrict.get();
        log.info("**/getting district by id = " + district);
        return district;
    }

    /**
     * The method returns dto {@code SuccessCreatedDistrict} if district successfully added.
     *
     * @param districtProfile - place place body of dto {@code DistrictProfile}.
     * @return new {@code SuccessCreatedDistrict}.
     * @throws AlreadyExistException if district already exists.
     */
    @Override
    public SuccessCreatedDistrict addDistrict(DistrictProfile districtProfile) {
        if (isDistrictExistByName(districtProfile.getName())) {
            throw new AlreadyExistException(String.format(DISTRICT_ALREADY_EXIST, districtProfile.getName()));
        }

        District district = districtRepository.save(dtoConverter.convertToEntity(districtProfile, new District())
                .withCity(cityService.getCityByName(districtProfile.getCityName())));

        log.info("**/adding new district = " + district);
        return dtoConverter.convertToDto(district, SuccessCreatedDistrict.class);
    }

    /**
     * The method returns list of dto {@code List<DistrictResponse>} of all cities.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @Override
    public List<DistrictResponse> getListOfDistricts() {
        List<DistrictResponse> districtResponses = districtRepository.findAllByOrderByIdAsc()
                .stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    /**
     * The method returns list of dto {@code List<DistrictResponse>} of all cities.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @Override
    public List<DistrictResponse> getListOfDistrictsByCityName(String name) {
        List<DistrictResponse> districtResponses = districtRepository.findAllByCityName(name)
                .stream()
                .map(district -> (DistrictResponse) dtoConverter.convertToDto(district, DistrictResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of districts = " + districtResponses);
        return districtResponses;
    }

    /**
     * The method returns dto {@code DistrictProfile} of updated district.
     *
     * @param districtProfile - place body of dto {@code DistrictProfile}.
     * @return new {@code DistrictProfile}.
     */
    @Override
    public DistrictProfile updateDistrict(Long id, DistrictProfile districtProfile) {
        District district = getDistrictById(id);
        District newDistrict = dtoConverter.convertToEntity(districtProfile, district)
                .withId(id)
                .withCity(cityService.getCityByName(districtProfile.getCityName()));

        log.info("**/updating district by id = " + newDistrict);
        return dtoConverter.convertToDto(districtRepository.save(newDistrict), DistrictProfile.class);
    }

    /**
     * The method deletes district {@link  District}
     *
     * @param id - id of district to delete
     * @return DistrictResponse {@link  DistrictResponse}.
     * @throws NotExistException {@link NotExistException} if the district doesn't exist.
     */
    @Override
    public DistrictResponse deleteDistrictById(Long id) {
        District district = getDistrictById(id);

        archiveService.saveModel(district);

        try {
            districtRepository.deleteById(id);
            districtRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(DISTRICT_DELETING_ERROR);
        }

        log.info("district {} was successfully deleted", district);
        return dtoConverter.convertToDto(district, DistrictResponse.class);
    }

    private boolean isDistrictExistByName(String name) {
        return districtRepository.existsByName(name);
    }

    private Optional<District> getOptionalDistrictById(Long id) {
        return districtRepository.findById(id);
    }

    private Optional<District> getOptionalDistrictByName(String name) {
        return districtRepository.findByName(name);
    }
}
