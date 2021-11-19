package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DistrictController implements Api {
    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    /**
     * The controller returns dto {@code DistrictResponse} about district.
     *
     * @param id - put district id.
     * @return new {@code DistrictResponse}.
     */
    @GetMapping("/district/{id}")
    public DistrictResponse getDistrict(@PathVariable long id) {
        return districtService.getDistrictProfileById(id);
    }

    /**
     * The controller returns dto {@code SuccessCreatedDistrict} of created district.
     *
     * @param districtProfile - place body to {@link DistrictProfile}.
     * @return new {@code SuccessCreatedDistrict}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/district")
    public SuccessCreatedDistrict addDistrict(
            @Valid
            @RequestBody DistrictProfile districtProfile) {
        return districtService.addDistrict(districtProfile);
    }

    /**
     * The controller returns dto {@code DistrictProfile} about district.
     *
     * @return new {@code DistrictProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/district/{id}")
    public DistrictProfile updateDistrict(
            @PathVariable Long id,
            @Valid
            @RequestBody DistrictProfile districtProfile) {
        return districtService.updateDistrict(id, districtProfile);
    }

    /**
     * The controller returns list of dto {@code List<DistrictResponse>} of district.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @GetMapping("/districts/{name}")
    public List<DistrictResponse> getDistrictsByCityName(@PathVariable String name) {
        return districtService.getListOfDistrictsByCityName(name);
    }

    /**
     * The controller returns list of dto {@code List<DistrictResponse>} of district.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @GetMapping("/districts")
    public List<DistrictResponse> getDistricts() {
        return districtService.getListOfDistricts();
    }

    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/district/{id}")
    public DistrictResponse deleteDistrict(@PathVariable Long id) {
        return districtService.deleteDistrictById(id);
    }
}
