package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.ClubResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedClub;
import com.softserve.teachua.dto.service.SearchClubProfile;
import com.softserve.teachua.dto.service.ClubProfile;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ClubController {
    private static final int CLUBS_PER_PAGE = 8;
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
     * The controller returns information {@code List <ClubResponse>} about clubs.
     *
     * @return new {@code List <ClubResponse>}.
     */
    @GetMapping("/clubs")
    public List<ClubResponse> getClubs() {
        return clubService.getListOfClubs();
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

    @GetMapping("/clubs/search")
    public Page<ClubResponse> getClubsListOfClubs(
            SearchClubProfile searchClubProfile,
            @PageableDefault(
                    value = CLUBS_PER_PAGE,
                    sort = "id") Pageable pageable) {
        return clubService.getClubsBySearchParameters(searchClubProfile, pageable);
    }

    /**
     * The controller returns dto {@code {@link ClubProfile}} about club.
     *
     * @return new {@code ClubResponse}.
     */
    @PutMapping("/club")
    public ClubProfile updateClub(@Valid @RequestBody ClubProfile clubProfile){
        return clubService.updateClub(clubProfile);
    }

    /**
     * The controller returns id {@code ...} of deleted club.
     *
     * @param id - put club id.
     * @return new {@code ...}.
     */
    //TODO
    @DeleteMapping("/club/{id}")
    public Object deleteClub(@PathVariable Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : " + id + " }", Object.class);
    }
}