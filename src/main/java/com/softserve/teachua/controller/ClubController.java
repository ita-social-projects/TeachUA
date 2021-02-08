package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ClubController implements Api {
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

    @GetMapping("/clubs/search/similar")
    public List<ClubResponse> getSimilarClubs(SimilarClubProfile similarClubProfile) {
        return clubService.getSimilarClubsByCategoryName(similarClubProfile);
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
     * The controller returns dto {@code {@link ClubProfile}} of updated club.
     *
     * @param clubProfile - Place dto with all parameters for updating existed club.
     * @return new {@code ClubProfile}.
     */
    @PutMapping("/club/{id}")
    public SuccessUpdatedClub updateClub(
            @PathVariable Long id,
            @Valid
            @RequestBody ClubProfile clubProfile) {
        return clubService.updateClub(id, clubProfile);
    }

    /**
     * The controller returns dto {@code ClubResponse} of deleted club by id.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @DeleteMapping("/club/{id}")
    public ClubResponse deleteClub(@PathVariable Long id) {
        return clubService.deleteClubById(id);
    }
}
