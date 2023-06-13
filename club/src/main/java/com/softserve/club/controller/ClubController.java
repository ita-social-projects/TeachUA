package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.club.ClubProfile;
import com.softserve.club.dto.club.ClubResponse;
import com.softserve.club.dto.club.SuccessUpdatedClub;
import com.softserve.club.dto.search.AdvancedSearchClubProfile;
import com.softserve.club.dto.search.SearchClubProfile;
import com.softserve.club.dto.search.SimilarClubProfile;
import com.softserve.club.dto.search.TopClubProfile;
import com.softserve.club.service.ClubService;
import com.softserve.club.util.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the clubs.
 */

@Slf4j
@RestController
//@Tag(name = "club", description = "the Club API")
//@SecurityRequirement(name = "api")
public class ClubController implements Api {
    private static final int CLUBS_PER_PAGE = 8;
    private static final int CLUBS_PER_USER_PAGE = 3;
    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * Use this endpoint to get club by id. The controller returns {@code ClubResponse}.
     *
     * @param id - put club id.
     * @return {@code ClubResponse}.
     */
    @GetMapping("/club/{id}")
    public ClubResponse getClubById(@PathVariable Long id) {
        return clubService.getClubProfileById(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/club/clubs-without-categories")
    public Page<ClubResponse> getClubsWithoutCategories(@PageableDefault(value = CLUBS_PER_PAGE, sort = "id")
                                                        Pageable pageable) {
        return clubService.getClubsWithoutCategories(pageable);
    }

    /**
     * Use this endpoint to get club by name The controller returns {@code ClubResponse}.
     *
     * @param name - put club name.
     * @return {@code ClubResponse}.
     */
    @GetMapping("/club/name/{name}")
    public ClubResponse getClubByName(@PathVariable String name) {
        return clubService.getClubProfileByName(name);
    }

    /**
     * Use this endpoint to get all clubs. The controller returns {@code List <ClubResponse>}.
     *
     * @return {@code List <ClubResponse>}.
     */
    @GetMapping("/clubs")
    public List<ClubResponse> getClubs() {
        return clubService.getListOfClubs();
    }

    /**
     * Use this endpoint to get all clubs by center id. The controller returns {@code List <ClubResponse>}.
     *
     * @return {@code List <ClubResponse>}.
     */
    @GetMapping("/clubsByCenterId/{id}")
    public List<ClubResponse> getClubsByCenterId(@PathVariable Long id) {
        return clubService.getListOfClubsByCenterId(id);
    }

    /**
     * Use this endpoint to create club.
     *
     * @param clubProfile - Place dto with all parameters for adding new club.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PostMapping("/club")
    public void addClub(@Valid @RequestBody ClubProfile clubProfile) {
        clubService.addClub(clubProfile);
    }

    /**
     * Use this endpoint to get all similar clubs. The controller returns {@code List <ClubResponse>}.
     *
     * @return {@code List <ClubResponse>}.
     */
    @GetMapping("/clubs/search/similar")
    public List<ClubResponse> getSimilarClubs(SimilarClubProfile similarClubProfile) {
        return clubService.getSimilarClubsByCategoryName(similarClubProfile);
    }

    /**
     * Use this endpoint to get clubs by id of user-owner with pagination. The controller returns information
     * {@code Page<ClubResponse>} about clubs by id of user-owner.
     *
     * @param id - put user id.
     * @return {@code Page<ClubResponse>}.
     */
    @GetMapping("/clubs/{id}")
    public Page<ClubResponse> getClubsByUserId(@PathVariable Long id,
                                               @PageableDefault(value = CLUBS_PER_USER_PAGE, sort = "id")
                                               Pageable pageable) {
        return clubService.getClubsByUserId(id, pageable);
    }

    /**
     * Use this endpoint to get a club by id of user-owner. The controller returns {@code List<ClubResponse>}.
     *
     * @param id - put user id.
     * @return {@code List<ClubResponse>}.
     */
    @GetMapping("/clubs/user/{id}")
    public List<ClubResponse> getListClubsByUserId(@PathVariable Long id) {
        return clubService.getListClubsByUserId(id);
    }

    /**
     * Use this endpoint to get clubs by search value with pagination. The controller returns
     * {@code Page<ClubResponse>}.
     *
     * @return {@code Page<ClubResponse>}.
     */
    @GetMapping("/clubs/search")
    public Page<ClubResponse> getClubsListOfClubs(SearchClubProfile searchClubProfile,
                                                  @PageableDefault(value = CLUBS_PER_PAGE, sort = "id")
                                                  Pageable pageable) {
        return clubService.getClubsBySearchParameters(searchClubProfile, pageable);
    }

    /**
     * Use this endpoint to get the advanced search results with pagination. The controller returns
     * {@code Page<ClubResponse>}.
     *
     * @param advancedSearchClubProfile - Place dto with all parameters to get possible club.
     * @return {@code ClubProfile}.
     */
    @GetMapping("/clubs/search/advanced")
    public Page<ClubResponse> getAdvancedSearchClubs(AdvancedSearchClubProfile advancedSearchClubProfile,
                                                     @PageableDefault(value = 6, sort = "id") Pageable pageable) {
        return clubService.getAdvancedSearchClubs(advancedSearchClubProfile, pageable);
    }

    /**
     * Use this endpoint to get the simple search result with pagination. The controller returns
     * {@code List<ClubResponse>}.
     *
     * @param searchClubProfile - Place dto with all parameters to get possible club.
     * @return {@code ClubProfile}.
     */
    @GetMapping("/clubs/search/simple")
    public List<ClubResponse> getClubsByCategoryAndCity(SearchClubProfile searchClubProfile) {
        return clubService.getClubByCategoryAndCity(searchClubProfile);
    }

    /**
     * Use this endpoint to update club by id. The controller returns {@code SuccessUpdatedClub}.
     *
     * @param id          - put club id here.
     * @param clubProfile - Place dto with all parameters for updating existing club.
     * @return new {@code ClubProfile}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.USER})
    @PutMapping("/club/{id}")
    public SuccessUpdatedClub updateClub(@PathVariable Long id, @Valid @RequestBody ClubResponse clubProfile) {
        return clubService.updateClub(id, clubProfile);
    }

    /**
     * Use this endpoint to change owner of club by id. The controller returns {@code ClubResponse}.
     *
     * @param id          - put club id here.
     * @param clubOwnerId - New owner id.
     * @return new {@code ClubProfile}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PatchMapping("/club/change-owner/{clubId}/{ownerId}")
    public ClubResponse changeClubOwner(@PathVariable("clubId") Long id,
                                        @PathVariable("ownerId") Long clubOwnerId) {
        return clubService.changeClubOwner(id, clubOwnerId);
    }

    /**
     * Use this endpoint to update some values of club by id. The controller returns {@code SuccessUpdatedClub}.
     *
     * @param id          - put club id here.
     * @param clubProfile - Place dto with all parameters for updating existing club.
     * @return new {@code SuccessUpdatedClub}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PatchMapping("/club/{id}")
    public SuccessUpdatedClub patchClub(@PathVariable Long id, @Valid @RequestBody ClubResponse clubProfile) {
        return clubService.updateClub(id, clubProfile);
    }

    /**
     * Use this endpoint to delete club by id. The controller returns {@code ClubResponse}.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @DeleteMapping("/club/{id}")
    public ClubResponse deleteClub(@PathVariable Long id) {
        return clubService.deleteClubById(id);
    }

    /**
     * Use this endpoint to update rating of all clubs. The controller returns {@code List<ClubResponse>}.
     *
     * @return new {@code List<ClubResponse>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PatchMapping("/clubs/rating")
    public List<ClubResponse> updateClubsRating() {
        return clubService.updateRatingForAllClubs();
    }

    /**
     * The controller updates contacts of club.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/club/updateContacts")
    public void updateContacts() {
        clubService.updateContacts();
    }

    @GetMapping("/clubs/top/search")
    public List<ClubResponse> getTopClubsByCity(TopClubProfile topClubProfile) {
        return clubService.getTopClubsByCity(topClubProfile);
    }
}
