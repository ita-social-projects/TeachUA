package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CenterController {

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
     * The controller returns dto {@code SuccessCreatedCenter} of created center.
     *
     * @return new {@code SuccessCreatedCenter}.
     */
    @PostMapping("/center")
    public SuccessCreatedCenter addCenter(
            @Valid
            @RequestBody CenterProfile centerProfile) {
        return centerService.addCenter(centerProfile);
    }

    /**
     * The controller returns dto {@code  CenterProfile} about center.
     *
     * @return new {@code CenterProfile}.
     */
    @PutMapping("/center")
    public CenterProfile updateCenter(@Valid @RequestBody CenterProfile centerProfile){
        return centerService.updateCenter(centerProfile);
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
     * The controller returns dto {@code ...} of deleted center.
     *
     * @param id - put category id.
     * @return new {@code ...}.
     */
    //TODO
    @DeleteMapping("/center")
    public Object deleteCenter(@RequestParam Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }


}
