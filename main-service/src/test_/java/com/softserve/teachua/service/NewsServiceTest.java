package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SimmilarNewsProfile;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.News;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.NewsArch;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.impl.NewsServiceImpl;
import java.util.Collections;
import java.util.Optional;
import jakarta.validation.ValidationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;

    @Mock
    private UserService userService;

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private NewsServiceImpl newsService;

    private News news;
    private User user;
    private NewsResponse newsResponse;
    private SuccessCreatedNews successCreatedNews;
    private NewsProfile newsProfile;
    private NewsArch newsArch;

    private final Long CORRECT_NEWS_ID = 1L;
    private static final Long WRONG_NEWS_ID = 100L;
    private static final String CORRECT_NEWS_TITLE = "MyNews";
    private static final String DESCRIPTION = "Description";
    private static final String URL = "UrlTitleLogo";
    private static final Boolean IS_ACTIVE = true;
    private static final Long USER_EXISTING_ID = 1L;
    private static final String USER_EXISTING_NAME = "User Existing Name";
    private static final String USER_EXISTING_LASTNAME = "User Existing LastName";

    @BeforeEach
    void setMocks() {

        user = User.builder().id(USER_EXISTING_ID).firstName(USER_EXISTING_NAME).lastName(USER_EXISTING_LASTNAME)
                .build();

        news = News.builder().id(CORRECT_NEWS_ID).title(CORRECT_NEWS_TITLE).user(user).build();

        newsResponse = NewsResponse.builder().id(CORRECT_NEWS_ID).title(CORRECT_NEWS_TITLE).build();

        successCreatedNews = SuccessCreatedNews.builder().id(CORRECT_NEWS_ID).title(CORRECT_NEWS_TITLE).build();

        newsProfile = NewsProfile.builder().title(CORRECT_NEWS_TITLE).description(DESCRIPTION).urlTitleLogo(URL)
                .isActive(IS_ACTIVE).build();

        newsArch = NewsArch.builder().userId(user.getId()).title(CORRECT_NEWS_TITLE).description(DESCRIPTION)
                .urlTitleLogo(URL).isActive(IS_ACTIVE).build();
    }

    @Test
    void getNewsByCorrectIdShouldReturnNews() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        News actual = newsService.getNewsById(CORRECT_NEWS_ID);
        assertThat(actual).isEqualTo(news);
    }

    @Test
    void getNewsByWrongIdShouldReturnNull() {
        when(newsRepository.findById(WRONG_NEWS_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> newsService.getNewsById(WRONG_NEWS_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteNewsByExistingIdShouldReturnCorrectNewsResponse() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(dtoConverter.convertToDto(news, NewsArch.class)).thenReturn(newsArch);
        when(archiveService.saveModel(newsArch)).thenReturn(Archive.builder().build());
        doNothing().when(newsRepository).deleteById(CORRECT_NEWS_ID);
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);
        NewsResponse actual = newsService.deleteNewsById(CORRECT_NEWS_ID);
        assertEquals(news.getTitle(), actual.getTitle());
    }

    @Test
    void deleteNewsByNotExistingIdShouldThrowNotExistException() {
        when(newsRepository.findById(WRONG_NEWS_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> newsService.deleteNewsById(WRONG_NEWS_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteNewsOnDataAccessExceptionShouldThrowDatabaseRepositoryException() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        doThrow(ValidationException.class).when(newsRepository).deleteById(CORRECT_NEWS_ID);
        assertThatThrownBy(() -> newsService.deleteNewsById(CORRECT_NEWS_ID))
                .isInstanceOf(DatabaseRepositoryException.class);
    }

    @Test
    void getListOfNewsShouldReturnList() {
        when(newsRepository.findAll()).thenReturn(Collections.singletonList(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);
        assertThat(newsService.getAllNews()).isNotEmpty().isEqualTo(Collections.singletonList(newsResponse));
    }

    @Test
    void getAllCurrentNewsShouldReturnList() {
        when(newsRepository.getAllCurrentNews()).thenReturn(Collections.singletonList(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);

        assertThat(newsService.getAllCurrentNews())
                .isNotEmpty()
                .isEqualTo(Collections.singletonList(newsResponse));
    }

    @Test
    void getSimilarNewsByTitleShouldReturnList() {
        SimmilarNewsProfile simmilarNewsProfile = new SimmilarNewsProfile(news.getId(), news.getTitle());

        when(newsRepository.getNewsByTitle(simmilarNewsProfile.getTitle())).
                thenReturn(Collections.singletonList(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class))
                .thenReturn(newsResponse);

        assertThat(newsService.getSimilarNewsByTitle(simmilarNewsProfile))
                .isNotEmpty()
                .isEqualTo(Collections.singletonList(newsResponse));
    }

    @Test
    void getListOfNewsShouldReturnPage() {
        Pageable pageable = mock(Pageable.class);
        when(newsRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(news)));
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);

        assertThat(newsService.getListOfNews(pageable))
                .isNotEmpty()
                .isEqualTo(new PageImpl<>(Collections.singletonList(newsResponse)));
    }

    @Test
    void updateNewsShouldReturnSuccessCreatedNews() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(newsRepository.save(news)).thenReturn(news);
        when(dtoConverter.convertToDto(news, SuccessCreatedNews.class)).thenReturn(successCreatedNews);
        assertThat(newsService.updateNewsProfileById(CORRECT_NEWS_ID, newsProfile)).isEqualTo(successCreatedNews);
    }

    @Test
    void createNewsShouldReturnSuccessCreatedNews() {
        when(dtoConverter.convertToEntity(newsProfile, new News())).thenReturn(news);
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(dtoConverter.convertToDto(news, SuccessCreatedNews.class)).thenReturn(successCreatedNews);
        assertThat(newsService.addNews(newsProfile)).isEqualTo(successCreatedNews);
    }

    @Test
    void getNewsProfileByIdShouldReturnNewsResponse() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);
        NewsResponse actual = newsService.getNewsProfileById(CORRECT_NEWS_ID);
        assertThat(actual).isEqualTo(newsResponse);
    }

    @Test
    void restoreNewsModelShouldSaveNewsWithUserNull() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(NewsArch.class)))
                .thenReturn(newsArch);
        when(dtoConverter.convertToEntity(eq(newsArch), any(News.class)))
                .thenReturn(news);

        newsService.restoreModel(NOT_EMPTY_STRING);
        verify(newsRepository).save(any(News.class));
    }

    @Test
    void restoreNewsModelShouldSaveNewsWithUserById() throws JsonProcessingException {
        news.setUser(null);
        newsArch.setUserId(user.getId());

        when(objectMapper.readValue(anyString(), eq(NewsArch.class)))
                .thenReturn(newsArch);
        when(dtoConverter.convertToEntity(eq(newsArch), any(News.class)))
                .thenReturn(news);
        when(userService.getUserById(newsArch.getUserId()))
                .thenReturn(user);

        newsService.restoreModel(NOT_EMPTY_STRING);
        verify(newsRepository).save(any(News.class));
    }

    @Test
    void restoreCertificateModelOnException() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(NewsArch.class)))
                .thenThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> newsService.restoreModel(NOT_EMPTY_STRING))
                .isInstanceOf(JsonProcessingException.class);
        verify(newsRepository, never()).save(news);
    }
}
