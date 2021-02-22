package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.News;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND_BY_ID = "News not found by id: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationship";

    private final NewsRepository newsRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;

    @Autowired
    NewsServiceImpl(NewsRepository newsRepository, DtoConverter dtoConverter, ArchiveService archiveService) {
        this.newsRepository = newsRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
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
    public List<NewsResponse> getAllNews() {
        List<NewsResponse> newsResponses = newsRepository.findAll()
                .stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .collect(Collectors.toList());
        log.info("get list of cities = " + newsResponses);
        return newsResponses;
    }

    @Override
    public Page<NewsResponse> getListOfNews(Pageable pageable) {
        Page<News> newsResponses = newsRepository.findAll(pageable);
        return new PageImpl<>(newsResponses
                .stream()
                .map(category -> (NewsResponse) dtoConverter.convertToDto(category, NewsResponse.class))
                .collect(Collectors.toList()),
                newsResponses.getPageable(), newsResponses.getTotalElements());
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

        archiveService.saveModel(deletedNews);

        try {
            newsRepository.deleteById(id);
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CATEGORY_DELETING_ERROR);
        }

        log.info("news {} was successfully deleted", deletedNews);
        return dtoConverter.convertToDto(deletedNews, NewsResponse.class);
    }

    private Optional<News> getOptionalNewsById(Long id) {
        return newsRepository.findById(id);
    }
}
