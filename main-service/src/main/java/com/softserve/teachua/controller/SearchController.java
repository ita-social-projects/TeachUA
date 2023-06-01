package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.search.CombinedPossibleResponse;
import com.softserve.teachua.service.CategoryService;
import com.softserve.teachua.service.ClubService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the searching process.
 */

@RestController
@Validated
@Tag(name = "search", description = "the Search API")
@SecurityRequirement(name = "api")
public class SearchController implements Api {
    private final CategoryService categoryService;
    private final ClubService clubService;

    @Autowired
    public SearchController(CategoryService categoryService, ClubService clubService) {
        this.categoryService = categoryService;
        this.clubService = clubService;
    }

    /**
     * Use this endpoint to get possible results of search by entered text. The controller returns
     * {@code CombinedPossibleResponse}.
     *
     * @param text
     *            - put search text.
     *
     * @return {@link CombinedPossibleResponse }
     */
    @GetMapping("/search")
    public CombinedPossibleResponse possibleResponses(@RequestParam @Length(max = 50) String text,
            @RequestParam String cityName) {
        return CombinedPossibleResponse.builder().categories(categoryService.getPossibleCategoryByName(text))
                .clubs(clubService.getPossibleClubByName(text, cityName)).build();
    }
}
