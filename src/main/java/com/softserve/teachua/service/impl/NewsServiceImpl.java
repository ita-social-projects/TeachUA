package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.News;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND_BY_ID = "News not found by id: %s";

    private final NewsRepository newsRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    NewsServiceImpl(NewsRepository newsRepository, DtoConverter dtoConverter) {
        this.newsRepository = newsRepository;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Method find {@link News}, and convert it to object of DTO class
     *
     * @param id
     * @return NewsResponce
     **/
    @Override
    public NewsResponse getNewsProfileById(Long id) {
        return dtoConverter.convertToDto(getNewsById(id), NewsResponse.class);
    }

    /**
     * Method find {@link News}
     *
     * @param id
     * @return News
     **/
    @Override
    public News getNewsById(Long id) {
        Optional<News> optionalNews = getOptionalNewsById(id);
        if (!optionalNews.isPresent()) {
            throw new NotExistException(String.format(NEWS_NOT_FOUND_BY_ID, id));
        }

        News news = optionalNews.get();
        log.info("get news by id - " + news);
        return news;
    }

    /**
     * Method add and save new {@link Feedback}
     *
     * @param newsProfile
     * @return SuccessCreatedNews
     **/
    @Override
    public SuccessCreatedNews addNews(NewsProfile newsProfile) {
        News news = newsRepository.save(dtoConverter.convertToEntity(newsProfile, new News()));
        return dtoConverter.convertToDto(news, SuccessCreatedNews.class);
    }

    /**
     * Method find all {@link News}
     *
     * @return List of {@link News}
     **/
    @Override
    public List<NewsResponse> getListOfNews() {
        List<NewsResponse> newsResponses = newsRepository.findAll()
                .stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .collect(Collectors.toList());
        log.info("get list of cities = " + newsResponses);
        return newsResponses;
    }

    /**
     * Method find {@link News} by id, and update data
     *
     * @param id
     * @param newsProfile
     * @return NewsProfile
     **/
    @Override
    public NewsProfile updateNewsProfileById(Long id, NewsProfile newsProfile) {
        News news = getNewsById(id);
        News newNews = dtoConverter.convertToEntity(newsProfile, news)
                .withId(id);

        log.info("**/updating club by id = " + newNews);
        return dtoConverter.convertToDto(newsRepository.save(newNews), NewsProfile.class);
    }

    /**
     * Method delete {@link News}
     *
     * @param id
     * @return NewsResponce
     **/
    @Override
    public NewsResponse deleteNewsById(Long id) {
        News deletedNews = getNewsById(id);

        newsRepository.deleteById(id);

        log.info("deleted feedback " + deletedNews);
        return dtoConverter.convertToDto(deletedNews, NewsResponse.class);
    }

    private Optional<News> getOptionalNewsById(Long id) {
        return newsRepository.findById(id);
    }
}
