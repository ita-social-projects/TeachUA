package com.softserve.teachua.service;

import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.District;
import java.util.List;
import java.util.Optional;

/**
 * This interface contains all needed methods to manage districs.
 */

public interface DistrictService {
    /**
     * The method returns dto {@code DistrictResponse} of district by id.
     *
     * @param id
     *            - put district id.
     *
     * @return new {@code DistrictResponse}.
     */
    DistrictResponse getDistrictProfileById(Long id);

    /**
     * The method returns entity {@code District} of district by id.
     *
     * @param id
     *            - put district id.
     *
     * @return new {@code District}.
     *
     * @throws NotExistException
     *             if district not exists.
     */
    District getDistrictById(Long id);

    /**
     * The method returns entity {@code District} of district by name.
     *
     * @param name
     *            - put center name.
     *
     * @return new {@code District}.
     *
     * @throws NotExistException
     *             if district not exists.
     */
    District getDistrictByName(String name);

    /**
     * The method returns optional {@code Optional<District>} of district by name.
     *
     * @param name
     *            - put center name.
     *
     * @return new {@code Optional<District>}.
     *
     * @throws NotExistException
     *             if district not exists.
     */
    Optional<District> getOptionalDistrictByName(String name);

    /**
     * The method returns dto {@code SuccessCreatedDistrict} if district successfully added.
     *
     * @param districtProfile
     *            - place place body of dto {@code DistrictProfile}.
     *
     * @return new {@code SuccessCreatedDistrict}.
     *
     * @throws AlreadyExistException
     *             if district already exists.
     */
    SuccessCreatedDistrict addDistrict(DistrictProfile districtProfile);

    /**
     * The method returns list of dto {@code List<DistrictResponse>} of all cities.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    List<DistrictResponse> getListOfDistricts();

    /**
     * The method returns list of dto {@code List<DistrictResponse>} of all cities.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    List<DistrictResponse> getListOfDistrictsByCityName(String name);

    /**
     * The method returns dto {@code DistrictProfile} of updated district.
     *
     * @param districtProfile
     *            - place body of dto {@code DistrictProfile}.
     *
     * @return new {@code DistrictProfile}.
     */
    DistrictProfile updateDistrict(Long id, DistrictProfile districtProfile);

    /**
     * The method deletes district {@link District}.
     *
     * @param id
     *            - id of district to delete
     *
     * @return new {@code  DistrictResponse}.
     *
     * @throws NotExistException
     *             if the district doesn't exist.
     */
    DistrictResponse deleteDistrictById(Long id);
}
