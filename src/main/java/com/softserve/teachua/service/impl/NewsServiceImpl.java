package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.News;
import com.softserve.teachua.model.archivable.NewsArch;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.NewsService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class NewsServiceImpl implements NewsService, ArchiveMark<News> {
    private static final String NEWS_NOT_FOUND_BY_ID = "News not found by id: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationship";

    private final NewsRepository newsRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    NewsServiceImpl(NewsRepository newsRepository,
                    DtoConverter dtoConverter,
                    ArchiveService archiveService,
                    UserService userService,
                    ObjectMapper objectMapper) {
        this.newsRepository = newsRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public NewsResponse getNewsProfileById(Long id) {
        return dtoConverter.convertToDto(getNewsById(id), NewsResponse.class);
    }

    @Override
    public News getNewsById(Long id) {
        Optional<News> optionalNews = getOptionalNewsById(id);
        if (!optionalNews.isPresent()) {
            throw new NotExistException(String.format(NEWS_NOT_FOUND_BY_ID, id));
        }

        News news = optionalNews.get();
        log.debug("get news by id - " + news);
        return news;
    }

    @Override
    public SuccessCreatedNews addNews(NewsProfile newsProfile) {
        News news = newsRepository.save(dtoConverter.convertToEntity(newsProfile, new News())
                .withUser(userService.getCurrentUser()));
        return dtoConverter.convertToDto(news, SuccessCreatedNews.class);
    }

    @Override
    public List<NewsResponse> getAllNews() {
        List<NewsResponse> newsResponses = newsRepository.findAll()
                .stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .collect(Collectors.toList());
        log.debug("get list of news = " + newsResponses);
        return newsResponses;
    }

    public List<NewsResponse> getAllCurrentNews(){
        List<NewsResponse> newsResponses = newsRepository.getAllCurrentNews()
                .stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .collect(Collectors.toList());
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

    @Override
    public SuccessCreatedNews updateNewsProfileById(Long id, NewsProfile newsProfile) {
        News news = getNewsById(id);
        BeanUtils.copyProperties(newsProfile, news);
        return dtoConverter.convertToDto(newsRepository.save(news), SuccessCreatedNews.class);
    }

    @Override
    public NewsResponse deleteNewsById(Long id) {
        News deletedNews = getNewsById(id);

        try {
            newsRepository.deleteById(id);
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CATEGORY_DELETING_ERROR);
        }

        archiveModel(deletedNews);

        log.debug("news {} were successfully deleted", deletedNews);
        return dtoConverter.convertToDto(deletedNews, NewsResponse.class);
    }

    private Optional<News> getOptionalNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public void archiveModel(News news) {
        archiveService.saveModel(dtoConverter.convertToDto(news, NewsArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        NewsArch newsArch = objectMapper.readValue(archiveObject, NewsArch.class);
        News news = dtoConverter.convertToEntity(newsArch, News.builder().build())
                .withId(null)
                .withUser(Optional.ofNullable(newsArch.getUserId()).isPresent()
                        ? userService.getUserById(newsArch.getUserId()) : null);
        newsRepository.save(news);
    }
}
