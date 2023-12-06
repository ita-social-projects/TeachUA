package com.softserve.teachua.repository;

import com.softserve.teachua.model.BannerItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BannerItemRepositoryIT {
    private static final String BANNER_ITEM_1 = "Banner item 1";
    private static final String BANNER_ITEM_2 = "Banner item 2";
    private static final String BANNER_ITEM_3 = "Banner item 3";

    private static final Integer BANNER_ITEM_SEQUENCE_NUMBER_1 = 100;
    private static final Integer BANNER_ITEM_SEQUENCE_NUMBER_2 = 1;
    private static final Integer BANNER_ITEM_SEQUENCE_NUMBER_3 = 50;

    private final BannerItem bannerItem1 = BannerItem.builder().title(BANNER_ITEM_1).subtitle(BANNER_ITEM_1)
            .picture(BANNER_ITEM_1).sequenceNumber(BANNER_ITEM_SEQUENCE_NUMBER_1).build();
    private final BannerItem bannerItem2 = BannerItem.builder().title(BANNER_ITEM_2).subtitle(BANNER_ITEM_2)
            .picture(BANNER_ITEM_2).sequenceNumber(BANNER_ITEM_SEQUENCE_NUMBER_2).build();
    private final BannerItem bannerItem3 = BannerItem.builder().title(BANNER_ITEM_3).subtitle(BANNER_ITEM_3)
            .picture(BANNER_ITEM_3).sequenceNumber(BANNER_ITEM_SEQUENCE_NUMBER_3).build();

    private final List<BannerItem> sortedBannerItems = Arrays.asList(bannerItem2, bannerItem3, bannerItem1);
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BannerItemRepository bannerItemRepository;

    @BeforeEach
    void setUp() {
        entityManager.persist(bannerItem1);
        entityManager.persist(bannerItem2);
        entityManager.persist(bannerItem3);
    }

    @Test
    void findAllByOrderBySequenceNumberAscShouldReturnSortedListBySequenceNumber() {
        assertThat(bannerItemRepository.findAllByOrderBySequenceNumberAsc()).isEqualTo(sortedBannerItems);
    }
}
