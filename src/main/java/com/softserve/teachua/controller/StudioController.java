package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StudioController {

    private final StudioService studioService;

    @Autowired
    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    /**
     * The controller returns information {@code StudioResponse} about center.
     *
     * @param id - put center id.
     * @return new {@code StudioResponse}.
     */
    @GetMapping("/studio/{id}")
    public StudioResponse getStudio(@PathVariable Long id) {
        return studioService.getStudioByProfileId(id);
    }

    /**
     * The controller returns dto {@code SuccessCreatedStudio} of created center.
     *
     * @param name - place Center name for add new Center.
     * @return new {@code SuccessCreatedStudio}.
     */
    @PostMapping("/studio")
    public SuccessCreatedStudio addStudio(
            @Valid
            @RequestBody String name) {
        return studioService.addStudio(name);
    }

    /**
     * The controller returns information {@code List <StudioResponse>} about center.
     *
     * @return new {@code List <StudioResponse>}.
     */
    @GetMapping("/studios")
    public List<StudioResponse> getStudios() {
        return studioService.getListOfStudios();
    }

    /**
     * The controller returns dto {@code ...} of deleted center.
     *
     * @param id - put category id.
     * @return new {@code ...}.
     */
    //TODO
    @DeleteMapping("/studio")
    public Object deleteStudio(@RequestParam Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }

    /**
     * The controller returns id {@code ...} of updated center.
     *
     * @param id - put center id.
     * @return new {@code ...}.
     */
    //TODO
    @PutMapping("/studio/{id}")
    public Object updateStudio(@PathVariable long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }
}
