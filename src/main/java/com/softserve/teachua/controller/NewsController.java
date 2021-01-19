package com.softserve.teachua.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class NewsController {


    @GetMapping("/news/{id}")
    public String getNews(@PathVariable Long id) {
        // TODO
        return "GetMapping, Method getNews, news id: " + id;
    }

    @PostMapping("/news/{id}")
    public String addNews() {
        // TODO
        return "PostMapping, Method addNews";
    }

    @DeleteMapping("/news/{id}")
    public String deleteNews(@PathVariable Long id) {
        // TODO
        return "DeleteMapping, Method deleteNews, news id: " + id;
    }

    @GetMapping("/newslist")
    public String getNewsList() {
        // TODO
        return "GetMapping, Method getNewsList";
    }


    @PostMapping("/newslist")
    public String addNewsList() {
        // TODO
        return "PostMapping, Method addNewsList";
    }


    @DeleteMapping("/newslist")
    public String deleteNewsList() {
        // TODO
        return "DeleteMapping, Method deleteNewsList";
    }
}
