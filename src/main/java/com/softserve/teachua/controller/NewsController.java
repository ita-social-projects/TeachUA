package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.service.NewsService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller is for managing the news.
 * */

@RestController
@Tag(name = "news", description = "the News API")
@SecurityRequirement(name = "api")
public class NewsController implements Api {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Use this endpoint to get News by id.
     * The controller returns {@code NewsResponse}.
     *
     * @param id - put News id here.
     * @return {@code NewsResponse}
     */
    @GetMapping("/news/{id}")
    public NewsResponse getNews(@PathVariable Long id) {
        return newsService.getNewsProfileById(id);
    }

    /**
     * Use this endpoint to create new News.
     * The controller returns {@code SuccessCreatedNews}.
     *
     * @param newsProfile - object of DTO class.
     * @return new {@code SuccessCreatedNews}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/news")
    public SuccessCreatedNews addNews(@RequestBody NewsProfile newsProfile) {
        return newsService.addNews(newsProfile);
    }

    /**
     * Use this endpoint to update News
     * The controller returns {@code NewsProfile}.
     *
     * @param id          - put news id here.
     * @param newsProfile - put news information here.
     * @return {@code NewsProfile}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/news/{id}")
    public SuccessCreatedNews updateNewsById(@PathVariable Long id, @RequestBody NewsProfile newsProfile) {
        return newsService.updateNewsProfileById(id, newsProfile);
    }

    /**
     * Use this endpoint to delete News.
     * The controller returns {@code NewsResponse}.
     *
     * @param id - put news id here.
     * @return NewsResponse {@code NewsResponse}
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/news/{id}")
    public NewsResponse deleteNews(@PathVariable Long id) {
        return newsService.deleteNewsById(id);
    }

    /**
     * Use this endpoint to get all News.
     * The controller returns {@code List<NewsResponse>}.
     *
     * @return {@code List<NewsResponse>}
     */
    @GetMapping("/newslist")
    public List<NewsResponse> getAllNews() {
        return newsService.getAllNews();
    }

    /**
     * Use this endpoint to get news search result with pagination.
     * The controller returns {@code Page<NewsResponse>}.
     *
     * @return {@code Page<NewsResponse>}
     */
    @GetMapping("/newslist/search")
    public Page<NewsResponse> getListOfNews(Pageable pageable) {
        return newsService.getListOfNews(pageable);
    }
}
