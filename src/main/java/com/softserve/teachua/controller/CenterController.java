package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.search.AdvancedSearchCenterProfile;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the centers.
 */

@Slf4j
@RestController
@Tag(name = "center", description = "the Center API")
@SecurityRequirement(name = "api")
public class CenterController implements Api {
    private static final int CENTERS_PER_USER_PAGE = 9;
    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    /**
     * Use this endpoint to get a center by its id. The controller returns {@code CenterResponse}.
     *
     * @param id - put center id.
     * @return new {@code CenterResponse}.
     */
    @GetMapping("/center/{id}")
    public CenterResponse getCenter(@PathVariable Long id) {
        return centerService.getCenterProfileById(id);
    }

    /**
     * Use this endpoint to get information about centers by id of user-owner with pagination. The controller returns
     * {@code List<CenterResponse>} about centers by id of user-owner.
     *
     * @param id - put user id.
     * @return new {@code Page<CenterResponse>}.
     */
    @GetMapping("centers/{id}")
    public Page<CenterResponse> getCentersByUserId(@PathVariable Long id,
                                                   @PageableDefault(value = CENTERS_PER_USER_PAGE, sort = "id")
                                                   Pageable pageable) {
        return centerService.getCentersByUserId(id, pageable);
    }

    /**
     * Use this endpoint to get clubs of center by id of center with pagination. The controller returns
     * {@code List<ClubResponse>} about center clubs by id of center.
     *
     * @param id - put user id.
     * @param size - put size of clubs in one page.
     *
     * @return new {@code Page<ClubResponse>}.
     */
    @GetMapping("centers/clubs/{id}")
    public Page<ClubResponse> getCenterClubsByCenterId(@PathVariable Long id,
                                                       @RequestParam int size, // size of pagination
                                                       @PageableDefault(sort = "id")
                                                       Pageable pageable) {
        return centerService.getCenterClubsByCenterId(id, pageable);
    }

    @GetMapping("centers/allclubs/{id}")
    public Object getAllCenterClubsByCenterId(@PathVariable Long id) {
        return centerService.getAllCenterClubsByCenterId(id);
    }

    /**
     * Use this endpoint to create a center. The controller returns {@code SuccessCreatedCenter}.
     *
     * @return new {@code SuccessCreatedCenter}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PostMapping("/center")
    public SuccessCreatedCenter addCenter(@Valid @RequestBody CenterProfile centerProfile) {
        return centerService.addCenterRequest(centerProfile);
    }

    /**
     * Use this endpoint to update the center. The controller returns {@code  CenterProfile}.
     *
     * @param id            - put center id here.
     * @param centerProfile - put center information here.
     * @return new {@code CenterProfile}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PutMapping("/center/{id}")
    public CenterProfile updateCenter(@PathVariable Long id, @Valid @RequestBody CenterProfile centerProfile) {
        return centerService.updateCenter(id, centerProfile);
    }

    /**
     * Use this endpoint to get information about all centers. The controller returns {@code List <CenterResponse>}.
     *
     * @return new {@code List <CenterResponse>}.
     */
    @GetMapping("/centers")
    public List<CenterResponse> getCenters() {
        return centerService.getListOfCenters();
    }

    /**
     * Use this endpoint to get the advanced search result for center with pagination. The controller returns
     * {@code {@link CenterProfile }}.
     *
     * @param advancedSearchCenterProfile - Place dto with all parameters for searched club.
     * @return new {@code ClubProfile}.
     */
    @GetMapping("/centers/search/advanced")
    public Page<CenterResponse> getAdvancedSearchClubs(AdvancedSearchCenterProfile advancedSearchCenterProfile,
                                                       @PageableDefault(value = 6, sort = "id") Pageable pageable) {
        log.debug("===== centerController started ======");
        return centerService.getAdvancedSearchCenters(advancedSearchCenterProfile, pageable);
    }

    /**
     * Use this endpoint to delete center by id. The controller returns dto {@code CenterResponse} of deleted center.
     *
     * @param id - put category id.
     * @return new {@code CenterResponse}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @DeleteMapping("/center/{id}")
    public CenterResponse deleteCenter(@PathVariable Long id) {
        return centerService.deleteCenterById(id);
    }

    /**
     * Call this endpoint to update all centers rating.
     *
     * @return list of updated centers
     */
    @AllowedRoles({RoleData.ADMIN})
    @PatchMapping("/centers/rating")
    public List<CenterResponse> updateCentersRating() {
        return centerService.updateRatingForAllCenters();
    }
}
