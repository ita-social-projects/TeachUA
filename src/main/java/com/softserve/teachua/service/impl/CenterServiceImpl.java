package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.controller.CenterResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCenter;
import com.softserve.teachua.dto.service.CenterProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CenterServiceImpl implements CenterService {
    private static final String CENTER_ALREADY_EXIST = "Center already exist with name: %s";
    private static final String CENTER_NOT_FOUND_BY_ID = "Center not found by id: %s";
    private static final String CENTER_NOT_FOUND_BY_NAME = "Center not found by name: %s";

    private final CenterRepository centerRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public CenterServiceImpl(CenterRepository centerRepository,DtoConverter dtoConverter) {

        this.centerRepository = centerRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public CenterResponse getCenterByProfileId(Long id) {
        return dtoConverter.convertToDto(getCenterById(id), CenterResponse.class);
    }

    @Override
    public Center getCenterById(Long id) {
        if (!isCenterExistById(id)) {
            String centerNotFoundById = String.format(CENTER_NOT_FOUND_BY_ID, id);
            log.error(centerNotFoundById);
            throw new NotExistException(centerNotFoundById);
        }

        Center center = centerRepository.getById(id);
        log.info("**/getting center by id = " + center);
        return center;
    }

    @Override
    public Center getCenterByName(String name) {
        if (!isCenterExistByName(name)) {
            String centerNotFoundById = String.format(CENTER_NOT_FOUND_BY_NAME, name);
            log.error(centerNotFoundById);
            throw new NotExistException(centerNotFoundById);
        }

        Center center = centerRepository.findByName(name);
        log.info("**/getting center by name = " + name);
        return center;
    }

    @Override
    public SuccessCreatedCenter addCenter(CenterProfile centerProfile){
        if (isCenterExistByName(centerProfile.getName())) {
            String centerAlreadyExist = String.format(CENTER_ALREADY_EXIST, centerProfile.getName());
            log.error(centerAlreadyExist);
            throw new AlreadyExistException(centerAlreadyExist);
        }

        Center center = centerRepository.save(dtoConverter.convertToEntity(centerProfile, new Center()));
        log.info("**/adding new center = " + centerProfile.getName());
        return dtoConverter.convertToDto(center, SuccessCreatedCenter.class);
    }

    @Override
    public List<CenterResponse> getListOfCenters() {
        List<CenterResponse> centerResponses = centerRepository.findAll()
                .stream()
                .map(center -> (CenterResponse) dtoConverter.convertToDto(center, CenterResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of centers = " + centerResponses);
        return centerResponses;
    }

    @Override
    public CenterProfile updateCenter(CenterProfile centerProfile) {
        Center center = centerRepository.save(dtoConverter.convertToEntity(centerProfile, new Center()));
        return dtoConverter.convertToDto(center, CenterProfile.class);
    }

    private boolean isCenterExistById(Long id) {
        return centerRepository.existsById(id);
    }

    private boolean isCenterExistByName(String name) {
        return centerRepository.existsByName(name);
    }
}
