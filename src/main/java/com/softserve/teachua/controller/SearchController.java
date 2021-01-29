package com.softserve.teachua.controller;

import com.softserve.teachua.dto.search.CombinedPossibleResponse;
import com.softserve.teachua.service.CategoryService;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    private final CategoryService categoryService;
    private final ClubService clubService;

    @Autowired
    public SearchController(CategoryService categoryService, ClubService clubService) {
        this.categoryService = categoryService;
        this.clubService = clubService;
    }

    @GetMapping("/search")
    public CombinedPossibleResponse possibleResponses(@RequestParam String text) {
        return CombinedPossibleResponse.builder()
                .categories(categoryService.getPossibleCategoryByName(text))
                .clubs(clubService.getPossibleClubByName(text))
                .build();
    }

}
