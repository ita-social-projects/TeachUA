package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.search.AdvancedSearchCenterProfile;
import com.softserve.teachua.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class CenterController implements Api {
    private static final int CENTERS_PER_USER_PAGE = 9;


    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    /**
     * The controller returns information {@code CenterResponse} about center.
     *
     * @param id - put center id.
     * @return new {@code CenterResponse}.
     */
    @GetMapping("/center/{id}")
    public CenterResponse getCenter(@PathVariable Long id) {
        return centerService.getCenterByProfileId(id);
    }


    /**
     * The controller returns information {@code List<CenterResponse>} about centers by id of user-owner
     *
     * @param id - put user id.
     * @return new {@code Page<CenterResponse>}.
     */
    @GetMapping("centers/{id}")
    public Page<CenterResponse> getCentersByUserId(
            @PathVariable Long id,
            @PageableDefault(
                    value = CENTERS_PER_USER_PAGE,
                    sort = "id") Pageable pageable) {
        return centerService.getCentersByUserId(id, pageable);
    }


    /**
     * The controller returns dto {@code SuccessCreatedCenter} of created center.
     *
     * @return new {@code SuccessCreatedCenter}.
     */
    @PostMapping("/center")
    public SuccessCreatedCenter addCenter(
            @Valid
            @RequestBody CenterProfile centerProfile,
            HttpServletRequest httpServletRequest) {
        return centerService.addCenterRequest(centerProfile, httpServletRequest);
    }

    /**
     * The controller returns dto {@code  CenterProfile} about center.
     *
     * @return new {@code CenterProfile}.
     */
    @PutMapping("/center/{id}")
    public CenterProfile updateCenter(
            @PathVariable Long id,
            @Valid
            @RequestBody CenterProfile centerProfile) {
        return centerService.updateCenter(id, centerProfile);
    }

    /**
     * The controller returns information {@code List <CenterResponse>} about center.
     *
     * @return new {@code List <CenterResponse>}.
     */
    @GetMapping("/centers")
    public List<CenterResponse> getCenters() {
        return centerService.getListOfCenters();
    }

    /**
     * The controller returns dto {@code {@link CenterProfile }} of the club.
     *
     * @param advancedSearchCenterProfile - Place dto with all parameters for searched club.
     * @return new {@code ClubProfile}.
     */
    @GetMapping("/centers/search/advanced")
    public Page<CenterResponse> getAdvancedSearchClubs(
            AdvancedSearchCenterProfile advancedSearchCenterProfile,
            @PageableDefault(
                    value = 6,
                    sort = "id") Pageable pageable) {
        log.debug("===== centerController started ======");
        return centerService.getAdvancedSearchCenters(advancedSearchCenterProfile, pageable);
    }

    /**
     * The controller returns dto {@code ...} of deleted center.
     *
     * @param id - put category id.
     * @return new {@code ...}.
     */
    //TODO
    @DeleteMapping("/center/{id}")
    public CenterResponse deleteCenter(@PathVariable Long id) {
        return centerService.deleteCenterById(id);
    }

    @PatchMapping("/centers/rating")
    public List<CenterResponse> updateCentersRating(){
        return centerService.updateRatingForAllCenters();
    }

}
