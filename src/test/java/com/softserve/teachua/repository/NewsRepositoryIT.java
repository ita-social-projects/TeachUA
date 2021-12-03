package com.softserve.teachua.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class NewsRepositoryIT {

    private static final String EXISTING_TITLE = "title1";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    private static final Integer RETURN_LIST_SIZE = 3;

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void findByExistingIdShouldReturnCorrectNewsEntity() {
        assertThat(newsRepository.findById(EXISTING_ID).get().getTitle()).isEqualTo(EXISTING_TITLE);
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(newsRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllShouldReturnList() {
        assertThat(newsRepository.findAll()).hasSize(RETURN_LIST_SIZE);
        assertThat(newsRepository.findAll().get(0).getId()).isEqualTo(1L);
        assertThat(newsRepository.findAll().get(1).getId()).isEqualTo(2L);
        assertThat(newsRepository.findAll().get(2).getId()).isEqualTo(3L);
    }

}
