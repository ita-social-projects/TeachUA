package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.security.UserPrincipal;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SimmilarNewsProfile;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.model.News;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.NewsService;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND_BY_ID = "News not found by id: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationship";

    private final NewsRepository newsRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final ArchiveMQMessageProducer<News> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, DtoConverter dtoConverter,
                           ObjectMapper objectMapper, ArchiveMQMessageProducer<News> archiveMQMessageProducer,
                           ArchiveClient archiveClient) {
        this.newsRepository = newsRepository;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
    }

    @Override
    public NewsResponse getNewsProfileById(Long id) {
        return dtoConverter.convertToDto(getNewsById(id), NewsResponse.class);
    }

    @Override
    public News getNewsById(Long id) {
        Optional<News> optionalNews = getOptionalNewsById(id);
        if (optionalNews.isEmpty()) {
            throw new NotExistException(String.format(NEWS_NOT_FOUND_BY_ID, id));
        }

        News news = optionalNews.get();
        log.debug("get news by id - " + news);
        return news;
    }

    @Override
    public SuccessCreatedNews addNews(NewsProfile newsProfile) {
        Long userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        News news = newsRepository.save(dtoConverter.convertToEntity(newsProfile, new News())
                .withUserId(userId));
        return dtoConverter.convertToDto(news, SuccessCreatedNews.class);
    }

    @Override
    public List<NewsResponse> getAllNews() {
        List<NewsResponse> newsResponses = newsRepository.findAll().stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .toList();
        log.debug("get list of news = " + newsResponses);
        return newsResponses;
    }

    public List<NewsResponse> getAllCurrentNews() {
        return newsRepository.getAllCurrentNews().stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .toList();
    }

    @Override
    public List<NewsResponse> getSimilarNewsByTitle(SimmilarNewsProfile newsProfile) {
        return newsRepository.getNewsByTitle(newsProfile.getTitle()).stream()
                .map(news -> (NewsResponse) dtoConverter.convertToDto(news, NewsResponse.class))
                .toList();
    }

    @Override
    public Page<NewsResponse> getListOfNews(Pageable pageable) {
        Page<News> newsResponses = newsRepository.findAll(pageable);
        return new PageImpl<>(newsResponses.stream()
                .map(category -> (NewsResponse) dtoConverter.convertToDto(category, NewsResponse.class))
                .toList(), newsResponses.getPageable(), newsResponses.getTotalElements());
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

    private void archiveModel(News news) {
        archiveMQMessageProducer.publish(news);
    }

    @Override
    public void restoreModel(Long id) {
        var news = objectMapper.convertValue(
                archiveClient.restoreModel(News.class.getName(), id),
                News.class);

        newsRepository.save(news);
    }
}
