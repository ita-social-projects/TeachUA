package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.club.*;
import com.softserve.teachua.dto.search.AdvancedSearchClubProfile;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.UserService;
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
public class ClubController implements Api {
    private static final int CLUBS_PER_PAGE = 8;
    private static final int CLUBS_PER_USER_PAGE = 3;

    private final ClubService clubService;
    private final JwtProvider jwtProvider;

    @Autowired
    public ClubController(ClubService clubService, JwtProvider jwtProvider) {
        this.clubService = clubService;
        this.jwtProvider = jwtProvider;
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

    /**
     * The controller returns information {@code Page<ClubResponse>} about clubs by id of user-owner
     *
     * @param id - put user id.
     * @return new {@code Page<ClubResponse>}.
     */
    @GetMapping("/clubs/{id}")
    public Page<ClubResponse> getClubsByUserId(
            @PathVariable Long id,
            @PageableDefault(
                    value = CLUBS_PER_USER_PAGE,
                    sort = "id") Pageable pageable) {
        return clubService.getClubsByUserId(id, pageable);
    }

    @GetMapping("/clubs/user/{id}")
    public List<ClubResponse> getListClubsByUserId(@PathVariable Long id){
        return clubService.getListClubsByUserId(id);
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
     * The controller returns dto {@code {@link ClubProfile}} of the club.
     *
     * @param advancedSearchClubProfile - Place dto with all parameters to get possible club.
     * @return new {@code ClubProfile}.
     */
    @GetMapping("/clubs/search/advanced")
    public Page<ClubResponse> getAdvancedSearchClubs(
            AdvancedSearchClubProfile advancedSearchClubProfile,
            @PageableDefault(
                    value = 6,
                    sort = "id") Pageable pageable) {
        return clubService.getAdvancedSearchClubs(advancedSearchClubProfile, pageable);
    }

    @GetMapping("/clubs/search/simple")
    public List<ClubResponse> getClubsByCategoryAndCity(SearchClubProfile searchClubProfile) {
        return clubService.getClubByCategoryAndCity(searchClubProfile);
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
            @RequestBody ClubResponse clubProfile,
            HttpServletRequest httpServletRequest) throws WrongAuthenticationException{
        User userFromClub = clubService.getClubById(id).getUser();
        Long userIdFromRequest = jwtProvider.getUserIdFromToken(jwtProvider.getJwtFromRequest(httpServletRequest));

        if(userFromClub == null || !userIdFromRequest.equals(userFromClub.getId())){
            throw new WrongAuthenticationException("A user cannot update club that does not belong to the user");
        }
        return clubService.updateClub(id, clubProfile);
    }

    @PatchMapping("/club/{id}")
    public ClubResponse changeClubOwner(
            @PathVariable Long id,
            @Valid
            @RequestBody ClubOwnerProfile clubOwnerProfile,
            HttpServletRequest httpServletRequest) throws WrongAuthenticationException{
        User userFromClub = clubService.getClubById(id).getUser();
        Long userIdFromRequest = jwtProvider.getUserIdFromToken(jwtProvider.getJwtFromRequest(httpServletRequest));

        if(userFromClub == null || !userIdFromRequest.equals(userFromClub.getId())){
            throw new WrongAuthenticationException("A user cannot change owner of a club that does not belong to the user");
        }
        return clubService.changeClubOwner(id, clubOwnerProfile);
    }


    /**
     * The controller returns dto {@code ClubResponse} of deleted club by id.
     *
     * @param id - put club id.
     * @return new {@code ClubResponse}.
     */
    @DeleteMapping("/club/{id}")
    public ClubResponse deleteClub(@PathVariable Long id,
                                   HttpServletRequest httpServletRequest) throws WrongAuthenticationException {
        User userFromClub = clubService.getClubById(id).getUser();
        Long userIdFromRequest = jwtProvider.getUserIdFromToken(jwtProvider.getJwtFromRequest(httpServletRequest));

        if(userFromClub == null || !userIdFromRequest.equals(userFromClub.getId())){
            throw new WrongAuthenticationException("A user cannot delete a club that does not belong to the user");
        }

        return clubService.deleteClubById(id);
    }
}
