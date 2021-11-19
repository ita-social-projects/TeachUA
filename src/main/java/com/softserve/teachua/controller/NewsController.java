package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
public class NewsController implements Api {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * The method to get News by id
     *
     * @param id of News
     * @return NewsResponse
     */
    @GetMapping("/news/{id}")
    public NewsResponse getNews(@PathVariable Long id) {
        return newsService.getNewsProfileById(id);
    }

    /**
     * The method to create a new News
     *
     * @param newsProfile - object of DTO class
     * @return SuccessCrreatedNews
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PostMapping("/news")
    public SuccessCreatedNews addNews(@RequestBody NewsProfile newsProfile, HttpServletRequest httpServletRequest) {
        return newsService.addNews(newsProfile, httpServletRequest);
    }

    /**
     * The method to update News
     *
     * @param id
     * @param newsProfile
     * @return NewsProfile
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PutMapping("/news/{id}")
    public SuccessCreatedNews updateNewsById(@PathVariable Long id, @RequestBody NewsProfile newsProfile) {
        return newsService.updateNewsProfileById(id, newsProfile);
    }

    /**
     * The method to delete News
     *
     * @param id
     * @return NewsResponse
     */
    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @DeleteMapping("/news/{id}")
    public NewsResponse deleteNews(@PathVariable Long id) {

        return newsService.deleteNewsById(id);
    }

    /**
     * The method to get all News
     *
     * @return List of NewsResponse
     */
    @GetMapping("/newslist")
    public List<NewsResponse> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/newslist/search")
    public Page<NewsResponse> getListOfNews(Pageable pageable) {
        return newsService.getListOfNews(pageable);
    }


}
