package com.softserve.teachua.controller.club;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.club.SuccessDeletedClubRating;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClubRatingController {

    /**
     * The controller returns information {@code ...} about club's rating.
     *
     * @param clubId - put club id.
     * @param ratingId - put club's rating id.
     * @return new {@code ...}.
     * @author Denis Burko
     */
    @GetMapping("/club/{clubId}/rating/{ratingId}")
    public Object getClubRating(@PathVariable Integer clubId,
                              @PathVariable Integer ratingId) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"clubId\" : "+ clubId +", \"ratingId\" : "+ ratingId +" }", Object.class);
    }

    /**
     * The controller returns dto {@code ...} of created club's rating.
     *
     * @param id - put club id.
     * @param rating - insert rating.
     * @return new {@code ...}.
     * @author Denis Burko
     */
    @PostMapping("/club/{id}/rating")
    public Object addClubRating(
            @PathVariable Integer id,
            @RequestParam Integer rating) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : "+ id +", \"rating\" : "+ rating +" }", Object.class);
    }

    /**
     * The controller returns dto {@code SuccessDeletedClubRating} of deleted club's rating.
     *
     * @param clubId - put club id.
     * @param ratingId - put rating id.
     * @return new {@code ...}.
     * @author Denis Burko
     */
    @DeleteMapping("/club/{clubId}/rating")
    public SuccessDeletedClubRating deletedClubRating(
            @PathVariable Integer clubId,
            @RequestParam Integer ratingId) {
        return SuccessDeletedClubRating.builder()
                .clubId(clubId)
                .ratingId(ratingId)
                .build();
    }
}
