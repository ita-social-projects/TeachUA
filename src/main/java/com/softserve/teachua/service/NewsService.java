package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.NewsResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedNews;
import com.softserve.teachua.dto.service.NewsProfile;
import com.softserve.teachua.model.News;
import java.util.List;

public interface NewsService {
    NewsResponse getNewsProfileById(Long id);

    News getNewsById(Long id);

    SuccessCreatedNews addNews(NewsProfile newsProfile);

    List<NewsResponse> getListOfNews();

    NewsProfile updateNewsProfileById(Long id, NewsProfile newsProfile);

    NewsResponse deleteNewsById(Long id);
}
