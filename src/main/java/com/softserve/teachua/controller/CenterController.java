package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.CenterResponse;
import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCenter;
import com.softserve.teachua.dto.service.CenterProfile;
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
     * The controller returns information {@code StudioResponse} about center.
     *
     * @param id - put center id.
     * @return new {@code StudioResponse}.
     */
    @GetMapping("/center/{id}")
    public CenterResponse getCenter(@PathVariable Long id) {
        return centerService.getCenterByProfileId(id);
    }

    /**
     * The controller returns dto {@code SuccessCreatedStudio} of created center.
     *
     * @param name - place Center name for add new Center.
     * @return new {@code SuccessCreatedStudio}.
     */
    @PostMapping("/center")
    public SuccessCreatedCenter addCenter(
            @Valid
            @RequestBody CenterProfile centerProfile) {
        return centerService.addCenter(centerProfile);
    }

    /**
     * The controller returns information {@code List <StudioResponse>} about center.
     *
     * @return new {@code List <StudioResponse>}.
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

    /**
     * The controller returns id {@code ...} of updated center.
     *
     * @param id - put center id.
     * @return new {@code ...}.
     */
    //TODO
    @PutMapping("/center/{id}")
    public Object updateCenter(@PathVariable long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }
}
