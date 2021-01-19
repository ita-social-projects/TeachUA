package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.ActivityResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedActivity;
import com.softserve.teachua.dto.service.ActivityProfile;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ActivityController {

    /**
     * The controller returns information {@code ActivityResponse} about activity.
     *
     * @param id - put activity id.
     * @return new {@code ActivityResponse}.
     */
    @GetMapping("/activity/{id}")
    public ActivityResponse getActivity(@PathVariable Long id) {
        return ActivityResponse.builder()
                .id(id)
                .name("My category")
                .build();
    }

    /**
     * The controller returns dto {@code SuccessCreatedActivity} of created activity.
     *
     * @param activityProfile - Place dto with all parameters for adding new activity.
     * @return new {@code SuccessCreatedActivity}.
     */
    @PostMapping("/activity")
    public SuccessCreatedActivity addActivity(
            @Valid
            @RequestBody ActivityProfile activityProfile) {
        return SuccessCreatedActivity.builder()
                .name(activityProfile.getName())
                .build();
    }

    /**
     * The controller returns id {@code ...} of deleted activity.
     *
     * @param id - put activity id.
     * @return new {@code ...}.
     */
    @DeleteMapping("/activity")
    public Object deleteActivity(@RequestParam Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }
}
