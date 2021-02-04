package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.search.CombinedPossibleResponse;
import com.softserve.teachua.service.CategoryService;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController implements Api {
    private final CategoryService categoryService;
    private final ClubService clubService;

    @Autowired
    public SearchController(CategoryService categoryService, ClubService clubService) {
        this.categoryService = categoryService;
        this.clubService = clubService;
    }

    /**
     * The method which return possible results of search by entered text.
     *
     * @param text - put search text.
     * @return {@link CombinedPossibleResponse }
     */
    @GetMapping("/search")
    public CombinedPossibleResponse possibleResponses(
            @RequestParam String text,
            @RequestParam String cityName) {
        return CombinedPossibleResponse.builder()
                .categories(categoryService.getPossibleCategoryByName(text))
                .clubs(clubService.getPossibleClubByName(text, cityName))
                .build();
    }
}
