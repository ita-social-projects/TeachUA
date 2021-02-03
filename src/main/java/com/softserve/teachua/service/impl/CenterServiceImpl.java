package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    /**
     * The method returns dto {@code CenterResponse} of center by id.
     *
     * @param id - put center id.
     * @return new {@code CenterResponse}.
     */
    @Override
    public CenterResponse getCenterByProfileId(Long id) {
        return dtoConverter.convertToDto(getCenterById(id), CenterResponse.class);
    }

    /**
     * The method returns dto {@code SuccessCreatedCenter} if center successfully added.
     *
     * @param centerProfile - place body of dto {@code CenterProfile}.
     * @return new {@code SuccessCreatedCenter}.
     * @throws AlreadyExistException if center already exists.
     */
    @Override
    public SuccessCreatedCenter addCenter(CenterProfile centerProfile){
        if (isCenterExistByName(centerProfile.getName())) {
            throw new AlreadyExistException(String.format(CENTER_ALREADY_EXIST, centerProfile.getName()));
        }

        Center center = centerRepository.save(dtoConverter.convertToEntity(centerProfile, new Center()));
        log.info("**/adding new center = " + centerProfile.getName());
        return dtoConverter.convertToDto(center, SuccessCreatedCenter.class);
    }


    /**
     * The method returns entity {@code Center} of center by id.
     *
     * @param id - put center id.
     * @return new {@code Center}.
     * @throws NotExistException if center not exists.
     */
    @Override
    public Center getCenterById(Long id) {
        Optional<Center> optionalCenter = getOptionalCenterById(id);
        if (!optionalCenter.isPresent()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_ID, id));
        }

        Center center = optionalCenter.get();
        log.info("**/getting center by id = " + center);
        return center;
    }

    /**
     * The method returns dto {@code CenterProfile} of updated club.
     *
     * @param centerProfile - place body of dto {@code CenterProfile}.
     * @return new {@code CenterProfile}.
     */

    @Override
    public CenterProfile updateCenter(Long id, CenterProfile centerProfile) {
        Center center = getCenterById(id);
        Center newCenter = dtoConverter.convertToEntity(centerProfile, center)
                .withId(id);

        log.info("**/updating center by id = " + newCenter);
        return dtoConverter.convertToDto(centerRepository.save(newCenter), CenterProfile.class);
    }

    /**
     * The method returns entity {@code Center} of center by name.
     *
     * @param name - put center name.
     * @return new {@code Center}.
     * @throws NotExistException if center not exists.
     */
    @Override
    public Center getCenterByName(String name) {
        Optional<Center> optionalCenter = getOptionalCenterByName(name);
        if (!optionalCenter.isPresent()) {
            throw new NotExistException(String.format(CENTER_NOT_FOUND_BY_NAME, name));
        }

        Center center = optionalCenter.get();
        log.info("**/getting center by name = " + name);
        return center;
    }

    /**
     * The method returns list of dto {@code List<CenterResponse>} of all centers.
     *
     * @return new {@code List<CenterResponse>}.
     */
    @Override
    public List<CenterResponse> getListOfCenters() {
        List<CenterResponse> centerResponses = centerRepository.findAll()
                .stream()
                .map(center -> (CenterResponse) dtoConverter.convertToDto(center, CenterResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of centers = " + centerResponses);
        return centerResponses;
    }

    private boolean isCenterExistByName(String name) {
        return centerRepository.existsByName(name);
    }
    private Optional<Center> getOptionalCenterById(Long id) {
        return centerRepository.findById(id);
    }
    private Optional<Center> getOptionalCenterByName(String name) {
        return centerRepository.findByName(name);
    }
}
