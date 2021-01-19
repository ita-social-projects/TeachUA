package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ClubController {

    /**
     * The controller returns information {@code ClubResponse} about club.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @GetMapping("/club/{id}")
    public ClubResponse getClub(@PathVariable Long id) {
        return ClubResponse.builder()
                .id(id)
                .ageFrom(4)
                .ageTo(10)
                .name("Zyma")
                .urlWeb("https://club-site.com")
                .build();
    }

    /**
     * The controller returns dto {@code SuccessCreatedClub} of created category.
     *
     * @param clubProfile - Place dto with all parameters for adding new club.
     * @return new {@code SuccessCreatedClub}.
     */
    @PostMapping("/club")
    public SuccessCreatedClub addClub(
            @Valid
            @RequestBody ClubProfile clubProfile) {
        return SuccessCreatedClub.builder()
                .ageFrom(clubProfile.getAgeFrom())
                .ageTo(clubProfile.getAgeTo())
                .name(clubProfile.getName())
                .build();
    }

    /**
     * The controller returns id {@code ...} of deleted club.
     *
     * @param id - put club id.
     * @return new {@code ...}.
     */
    @DeleteMapping("/club")
    public Object deleteClub(@RequestParam Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : "+ id +" }", Object.class);
    }
}
