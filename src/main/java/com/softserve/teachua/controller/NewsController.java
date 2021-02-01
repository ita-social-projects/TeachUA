package com.softserve.teachua.controller;

import com.softserve.teachua.dto.controller.NewsResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedNews;
import com.softserve.teachua.dto.service.NewsProfile;
import com.softserve.teachua.service.NewsService;
import com.softserve.teachua.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NewsController {

    NewsService newsService;

    @Autowired
    NewsController(NewsServiceImpl newsService){
        this.newsService = newsService;
    }

    @GetMapping("/news/{id}")
    public NewsResponse getNews(@PathVariable Long id) {
        return newsService.getNewsProfileById(id);
    }

    @PostMapping("/news")
    public SuccessCreatedNews addNews( @RequestBody NewsProfile newsProfile) {
        return newsService.addNews(newsProfile);
    }

    @PutMapping("/news/{id}")
    public NewsProfile updateNewsById(@PathVariable Long id, @RequestBody NewsProfile newsProfile){
        return newsService.updateNewsProfileById(id,newsProfile);
    }

    @DeleteMapping("/news/{id}")
    public NewsResponse deleteNews(@PathVariable Long id) {

        return newsService.deleteNewsById(id);
    }

    @GetMapping("/newslist")
    public List<NewsResponse> getNewsList() {
        return newsService.getListOfNews();
    }


}
