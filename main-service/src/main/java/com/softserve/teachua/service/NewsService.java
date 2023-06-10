package com.softserve.teachua.service;

import com.softserve.teachua.dto.news.NewsProfile;
import com.softserve.teachua.dto.news.NewsResponse;
import com.softserve.teachua.dto.news.SimmilarNewsProfile;
import com.softserve.teachua.dto.news.SuccessCreatedNews;
import com.softserve.teachua.model.News;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface contains all needed methods to manage news.
 */

public interface NewsService {
    /**
     * Method finds {@code News}, and converts it to object of DTO class {@code NewsResponce}.
     *
     * @param id
     *            - put News id.
     *
     * @return new {@code NewsResponce}.
     **/
    NewsResponse getNewsProfileById(Long id);

    /**
     * Method finds {@link News}.
     *
     * @param id
     *            - put News id.
     *
     * @return new {@code News}
     **/
    News getNewsById(Long id);

    /**
     * Method add and save new {@link NewsProfile}, returns dto {@code SuccessCreatedNews}.
     *
     * @param newsProfile
     *            - place body of dto {@code NewsProfile}.
     *
     * @return new {@code SuccessCreatedNews}.
     **/
    SuccessCreatedNews addNews(NewsProfile newsProfile);

    /**
     * Method returns list of all news {@code List<NewsResponse>}.
     *
     * @return new {@code List<NewsResponse>}.
     **/
    List<NewsResponse> getAllNews();

    /**
     * Method returns page of news {@code Page<NewsResponse>}.
     *
     * @return new {@code Page<NewsResponse>}.
     **/
    Page<NewsResponse> getListOfNews(Pageable pageable);

    /**
     * Method find {@link News} by id, and update data.
     *
     * @param id
     *            - put News id.
     * @param newsProfile
     *            - place body of dto {@code NewsProfile}.
     *
     * @return new {@code SuccessCreatedNews}.
     **/
    SuccessCreatedNews updateNewsProfileById(Long id, NewsProfile newsProfile);

    /**
     * The method deletes news by id and returns dto {@code NewsResponse}.
     *
     * @param id
     *            - put News id.
     *
     * @return new {@code NewsResponse}.
     **/
    NewsResponse deleteNewsById(Long id);

    List<NewsResponse> getAllCurrentNews();

    List<NewsResponse> getSimilarNewsByTitle(SimmilarNewsProfile newsProfile);
}
