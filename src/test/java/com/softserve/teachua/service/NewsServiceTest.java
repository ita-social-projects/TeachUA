package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.District;
import com.softserve.teachua.model.News;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private UserService userService;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News news;
    private User user;
    private NewsResponse newsResponse;
    private SuccessCreatedNews successCreatedNews;
    private NewsProfile newsProfile;
    private HttpServletRequest httpServletRequest;

    public NewsRepository getNewsRepository() {
        return newsRepository;
    }

    // news
    private final Long CORRECT_NEWS_ID = 1L;
    private final Long WRONG_NEWS_ID = 100L;
    private final String CORRECT_NEWS_TITLE = "MyNews";
    private final String WRONG_NEWS_TITLE = "BadNews";
    private final String DESCRIPTION = "Descpirtion";
    private final String URL = "UrlTitleLogo";
    private final Boolean ISACTIVE = true;
    private final String NEW_TITLE = "New News Title";

    private final Long USER_EXISTING_ID = 1L;
    private final Long USER_NOT_EXISTING_ID = 100L;
    private final String USER_EXISTING_NAME = "User Existing Name";
    private final String USER_EXISTING_LASTNAME = "User Existing LastName";

    @BeforeEach
    public void setMocks() {

        user = User.builder()
                .id(USER_EXISTING_ID)
                .firstName(USER_EXISTING_NAME)
                .lastName(USER_EXISTING_LASTNAME)
                .build();

        news = News.builder()
                .id(CORRECT_NEWS_ID)
                .title(CORRECT_NEWS_TITLE)
                .user(user)
                .build();

        newsResponse = NewsResponse.builder()
                .id(CORRECT_NEWS_ID)
                .title(CORRECT_NEWS_TITLE)
                .build();

        successCreatedNews = SuccessCreatedNews.builder()
                .id(CORRECT_NEWS_ID)
                .title(CORRECT_NEWS_TITLE)
                .build();

        newsProfile = NewsProfile.builder()
                .title(CORRECT_NEWS_TITLE)
                .description(DESCRIPTION)
                .urlTitleLogo(URL)
                .isActive(ISACTIVE)
                .build();
    }

    @Test
    public void getNewsByCorrectIdShouldReturnNews() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        News actual = newsService.getNewsById(CORRECT_NEWS_ID);
        assertThat(actual)
                .isEqualTo(news);
    }

    @Test
    public void getNewsByWrongIdShouldReturnNull() {
        when(newsRepository.findById(WRONG_NEWS_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            newsService.getNewsById(WRONG_NEWS_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteNewsByExistingIdShouldReturnCorrectNewsResponse() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(archiveService.saveModel(news)).thenReturn(news);
        doNothing().when(newsRepository).deleteById(CORRECT_NEWS_ID);
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);
        NewsResponse actual = newsService.deleteNewsById(CORRECT_NEWS_ID);
        assertEquals(news.getTitle(), actual.getTitle());
    }

    @Test
    void deleteNewsByNotExistingIdShouldThrowNotExistException() {
        when(newsRepository.findById(WRONG_NEWS_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            newsService.deleteNewsById(WRONG_NEWS_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getListOfNewsShouldReturnList() {
        when(newsRepository.findAll()).thenReturn(Arrays.asList(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class))
                .thenReturn(newsResponse);
        assertThat(newsService.getAllNews())
                .isNotEmpty()
                .isEqualTo(Arrays.asList(newsResponse));
    }

    @Test
    public void updateNewsShouldReturnSuccessCreatedNews() {
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(newsRepository.save(news)).thenReturn(news);
        when(dtoConverter.convertToDto(news, SuccessCreatedNews.class)).thenReturn(successCreatedNews);
        assertThat(newsService.updateNewsProfileById(CORRECT_NEWS_ID, newsProfile))
                .isEqualTo(successCreatedNews);
    }
    @Test
    public void createNewsShouldReturnSuccessCreatedNews(){
        when(dtoConverter.convertToEntity(newsProfile, new News())).thenReturn(news);
        when(newsRepository.save(news)).thenReturn(news);
        when(dtoConverter.convertToDto(news, SuccessCreatedNews.class)).thenReturn(successCreatedNews);
        assertThat(newsService.addNews(newsProfile, httpServletRequest))
                .isEqualTo(successCreatedNews);
    }

    @Test
    public void getNewsProfileByIdShouldReturnNewsResponse(){
        when(newsRepository.findById(CORRECT_NEWS_ID)).thenReturn(Optional.of(news));
        when(dtoConverter.convertToDto(news, NewsResponse.class)).thenReturn(newsResponse);
        NewsResponse actual = newsService.getNewsProfileById(CORRECT_NEWS_ID);
        assertThat(actual)
                .isEqualTo(newsResponse);
    }

}
