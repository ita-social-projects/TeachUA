package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the districts.
 */

@RestController
@Tag(name = "district", description = "the District API")
@SecurityRequirement(name = "api")
public class DistrictController implements Api {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    /**
     * Use this endpoint to get district by id. The controller returns {@code DistrictResponse}.
     *
     * @param id
     *            - put district id.
     *
     * @return new {@code DistrictResponse}.
     */
    @GetMapping("/district/{id}")
    public DistrictResponse getDistrict(@PathVariable long id) {
        return districtService.getDistrictProfileById(id);
    }

    /**
     * Use this endpoint to create district. The controller returns {@code SuccessCreatedDistrict}.
     *
     * @param districtProfile
     *            - place body to {@link DistrictProfile}.
     *
     * @return new {@code SuccessCreatedDistrict}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/district")
    public SuccessCreatedDistrict addDistrict(@Valid @RequestBody DistrictProfile districtProfile) {
        return districtService.addDistrict(districtProfile);
    }

    /**
     * Use this endpoint to update district by id. The controller returns {@code DistrictProfile}.
     *
     * @param id
     *            - put district id here.
     * @param districtProfile
     *            - put district profile information here.
     *
     * @return new {@code DistrictProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/district/{id}")
    public DistrictProfile updateDistrict(@PathVariable Long id, @Valid @RequestBody DistrictProfile districtProfile) {
        return districtService.updateDistrict(id, districtProfile);
    }

    /**
     * Use this endpoint to get districts by name. The controller returns list {@code List<DistrictResponse>}.
     *
     * @param name
     *            - put district name here.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @GetMapping("/districts/{name}")
    public List<DistrictResponse> getDistrictsByCityName(@PathVariable String name) {
        return districtService.getListOfDistrictsByCityName(name);
    }

    /**
     * Use this endpoint to get all districts. The controller returns list of {@code List<DistrictResponse>}.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @GetMapping("/districts")
    public List<DistrictResponse> getDistricts() {
        return districtService.getListOfDistricts();
    }

    /**
     * Use this endpoint to delete district by id. The controller returns {@code DistrictResponse}.
     *
     * @param id
     *            - put district id here.
     *
     * @return new {@code List<DistrictResponse>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/district/{id}")
    public DistrictResponse deleteDistrict(@PathVariable Long id) {
        return districtService.deleteDistrictById(id);
    }
}
