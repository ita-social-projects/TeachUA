package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/news")
    public SuccessCreatedNews addNews(@RequestBody NewsProfile newsProfile) {
        return newsService.addNews(newsProfile);
    }

    /**
     * The method to update News
     *
     * @param id
     * @param newsProfile
     * @return NewsProfile
     */
    @PutMapping("/news/{id}")
    public NewsProfile updateNewsById(@PathVariable Long id, @RequestBody NewsProfile newsProfile) {
        return newsService.updateNewsProfileById(id, newsProfile);
    }

    /**
     * The method to delete News
     *
     * @param id
     * @return NewsResponse
     */
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
