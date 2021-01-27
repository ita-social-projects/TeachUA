package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ClubController {
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * The controller returns information {@code ClubResponse} about club.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @GetMapping("/club/{id}")
    public ClubResponse getClubById(@PathVariable Long id) {
        return clubService.getClubProfileById(id);
    }

    /**
     * The controller returns information {@code ClubResponse} about club.
     *
     * @param name - put club name.
     * @return new {@code ClubResponse}.
     */
    @GetMapping("/club/name/{name}")
    public ClubResponse getClubByName(@PathVariable String name) {
        return clubService.getClubProfileByName(name);
    }

    /**
     * The controller returns dto {@code SuccessCreatedClub} of created club
     *
     * @param clubProfile - Place dto with all parameters for adding new club.
     * @return new {@code SuccessCreatedClub}.
     */

    @PostMapping("/club")
    public SuccessCreatedClub addClub(
            @Valid
            @RequestBody ClubProfile clubProfile) {
        return clubService.addClub(clubProfile);
    }

    /**
     * The controller returns information {@code List <ClubResponse>} about clubs.
     *
     * @return new {@code List <ClubResponse>}.
     */
    @GetMapping("/clubs")
    public List<ClubResponse> getClubs() {
        return clubService.getListOfClubs();
    }

    /**
     * The controller returns id {@code ...} of deleted club.
     *
     * @param id - put club id.
     * @return new {@code ...}.
     */

    //TODO
    @DeleteMapping("/club{id}")
    public Object deleteClub(@PathVariable Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }

    /**
     * The controller returns id {@code ...} of updated club.
     *
     * @param id - put club id.
     * @return new {@code ...}.
     */
    //TODO
    @PutMapping("/club/{id}")
    public Object updateClub(@PathVariable long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }
}