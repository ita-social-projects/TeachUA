package com.softserve.teachua.repository;

import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.utils.club_test_data.ClubAdvancedSearchRepository;
import com.softserve.teachua.repository.utils.club_test_data.ClubSearchTestEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ClubRepositoryIT {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 500L;

    private static final String EXISTING_NAME = "IT освіта: курси \"ГРАНД\"";
    private static final String NOT_EXISTING_NAME = "Неіснуючі курси";

    private static final Long EXISTING_USER_ID = 3L;
    private static final Long NOT_EXISTING_USER_ID = 500L;

    private static final Long EXISTING_CENTER_ID = 2L;
    private static final Long NOT_EXISTING_CENTER_ID = 500L;
    private static final String NOT_EXISTING_CENTER_NAME = "Неіснуючий центр";

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Test
    void findByExistingIdShouldReturnOptionalOfCorrectClubEntity() {
        assertThat(clubRepository.findById(EXISTING_ID)).isInstanceOf(Optional.class);
        assertThat(clubRepository.findById(EXISTING_ID).orElseThrow()).isInstanceOf(Club.class);
        assertThat(clubRepository.findById(EXISTING_ID).orElseThrow().getId()).isEqualTo(EXISTING_ID);
    }

    @Test
    void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(clubRepository.findById(NOT_EXISTING_ID)).isEmpty();
    }

    @Test
    void findByExistingNameShouldReturnOptionalOfCorrectClubEntity() {
        assertThat(clubRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(clubRepository.findByName(EXISTING_NAME).orElseThrow()).isInstanceOf(Club.class);
        assertThat(clubRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(clubRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void findAllByOrderByIdAscTest() {
        List<Club> list = clubRepository.findAllByOrderByIdAsc();

        assertThat(list).isNotEmpty();

        for (int i = 1; i < list.size(); i++) {
            assertThat(list.get(i).getId()).isGreaterThan(list.get(i - 1).getId());
        }
    }

    @Test
    void existsByExistingNameShouldReturnTrue() {
        assertTrue(clubRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void existsByNotExistingNameShouldReturnFalse() {
        assertFalse(clubRepository.existsByName(NOT_EXISTING_NAME));
    }

    @Test
    void existsByExistingIdShouldReturnTrue() {
        assertTrue(clubRepository.existsById(EXISTING_ID));
    }

    @Test
    void existsByNotExistingIdShouldReturnFalse() {
        assertFalse(clubRepository.existsById(NOT_EXISTING_ID));
    }

    @Test
    void findAllByExistingUserIdShouldReturnListOfCorrectClubEntities() {
        List<Club> list = clubRepository.findAllByUserId(EXISTING_USER_ID);

        assertThat(list).isNotEmpty();

        for (Club club : list) {
            assertThat(club.getUser().getId()).isEqualTo(EXISTING_USER_ID);
        }
    }

    @Test
    void findAllByNotExistingUserIdShouldReturnEmptyList() {
        assertThat(clubRepository.findAllByUserId(NOT_EXISTING_USER_ID)).isEmpty();
    }

    @Test
    void findAllByExistingUserIdShouldReturnPageOfCorrectClubEntities() {
        Pageable pageable = PageRequest.of(0, 6);

        Page<Club> page = clubRepository.findAllByUserId(EXISTING_USER_ID, pageable);

        assertThat(page.getContent().size()).isPositive().isLessThanOrEqualTo(6);

        for (Club club : page.getContent()) {
            assertThat(club.getUser().getId()).isEqualTo(EXISTING_USER_ID);
        }
    }

    @Test
    void findAllByNotExistingUserIdShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 6);

        assertThat(clubRepository.findAllByUserId(NOT_EXISTING_USER_ID, pageable).getContent()).isEmpty();
    }

    @Test
    void findClubsByExistingCenterShouldReturnCorrectClubEntitiesList() {
        assertThat(centerRepository.findById(EXISTING_CENTER_ID)).isInstanceOf(Optional.class);
        assertThat(centerRepository.findById(EXISTING_CENTER_ID).orElseThrow()).isInstanceOf(Center.class);
        assertThat(centerRepository.findById(EXISTING_CENTER_ID).orElseThrow().getId()).isEqualTo(EXISTING_CENTER_ID);

        Center center = centerRepository.findById(EXISTING_CENTER_ID).orElseThrow();

        List<Club> clubs = clubRepository.findClubsByCenter(center);

        assertThat(clubs).isNotEmpty();

        for (Club club : clubs) {
            assertThat(club.getCenter()).isEqualTo(center);
        }
    }

    @Test
    void findClubsByNotExistingCenterShouldReturnEmptyList() {
        Center center = Center.builder().id(NOT_EXISTING_CENTER_ID).name(NOT_EXISTING_CENTER_NAME).build();

        List<Club> clubs = clubRepository.findClubsByCenter(center);

        assertThat(clubs).isEmpty();
    }

    //@Test
    void advancedSearchWithCorrectAgeShouldReturnListOfCorrectClubEntities() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectAge();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, testClub.getAge(), null, null, null, null, null, null).getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertThat(testClub.getAge()).isGreaterThanOrEqualTo(club.getAgeFrom())
                .isLessThanOrEqualTo(club.getAgeTo()));
    }

    @Test
    void advancedSearchWithAgeThatIsLessThanIsAllowedShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithLessAge();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, testClub.getAge(), null, null, null, null, null, null).getContent();

        assertThat(clubs).isEmpty();
    }

    @Test
    void advancedSearchWithAgeThatIsGreaterThanIsAllowedShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithGreaterAge();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, testClub.getAge(), null, null, null, null, null, null).getContent();

        assertThat(clubs).isEmpty();
    }

    //@Test
    void advancedSearchWithOnlineClubsShouldReturnAllClubsWitchIsOnline() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getOnlineClub();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, null, null, testClub.isOnline(), null).getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getIsOnline()));
    }

    //@Test
    void advancedSearchWithNotOnlineClubsShouldReturnAllClubsWitchIsNotOnline() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getNotOnlineClub();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, null, null, testClub.isOnline(), null).getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertFalse(club.getIsOnline()));
    }

    @Test
    void advancedSearchWithOneCategoryNameShouldReturnAllClubsWithThatCategory() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithOneCategoryName();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, null, testClub.getCategoriesNames(), null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getCategories().stream().map(Category::getName)
                .anyMatch(testClub.getCategoriesNames()::contains)));
    }

    @Test
    void advancedSearchWithManyCategoryNameShouldReturnAllClubsWithThatCategories() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithManyCategoryName();
        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, null, testClub.getCategoriesNames(), null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getCategories().stream().map(Category::getName)
                .anyMatch(testClub.getCategoriesNames()::contains)));
    }

    //@Test
    void advancedSearchWithZeroCategoryNamesShouldReturnAllClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithZeroCategoryName();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, null, testClub.getCategoriesNames(), null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();
    }

    //@Test
    void advancedSearchWithExistingCityName1ShouldReturnListOfClubsFromThatCity() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithExistingCityName1();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, testClub.getCityName(), null, null, null, null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getLocations().stream().map(location -> location.getCity().getName())
                .anyMatch(testClub.getCityName()::contains)));
    }

    //@Test
    void advancedSearchWithExistingCityName2ShouldReturnListOfClubsFromThatCity() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithExistingCityName2();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, testClub.getCityName(), null, null, null, null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getLocations().stream().map(location -> location.getCity().getName())
                .anyMatch(testClub.getCityName()::contains)));
    }

    @Test
    void advancedSearchWithNotExistingCityNameShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithNotExistingCityName();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, testClub.getCityName(), null, null, null, null, null)
                .getContent();

        assertThat(clubs).isEmpty();
    }

    //@Test
    void advancedSearchWithExistingDistrictShouldReturnCorrectListOfClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectDistrict();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, testClub.getDistrictName(), null, null, null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getLocations().stream().map(location -> location.getDistrict().getName())
                .anyMatch(testClub.getDistrictName()::contains)));
    }

    @Test
    void advancedSearchWithNotExistingDistrictShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithIncorrectDistrict();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, testClub.getDistrictName(), null, null, null, null)
                .getContent();

        assertThat(clubs).isEmpty();
    }

    //@Test
    void advancedSearchWithExistingStationNameShouldReturnCorrectListOfClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectStationName();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, testClub.getStationName(), null, null, null)
                .getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> assertTrue(club.getLocations().stream().map(location -> location.getStation().getName())
                .anyMatch(testClub.getStationName()::contains)));
    }

    @Test
    void advancedSearchWithNotExistingStationNameShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithIncorrectStationName();

        List<Club> clubs = clubRepository
                .findAllBylAdvancedSearch(null, null, null, null, testClub.getStationName(), null, null, null)
                .getContent();

        assertThat(clubs).isEmpty();
    }

    @Test
    void advancedSearchWithAllCorrectParamsShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubAsListWithAllCorrectData();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(testClub.getName(), testClub.getAge(),
                testClub.getCityName(), testClub.getDistrictName(), testClub.getStationName(),
                testClub.getCategoriesNames(), null, null).getContent();

        assertThat(clubs).isNotEmpty();

        clubs.forEach(club -> {
            assertThat(testClub.getAge()).isGreaterThanOrEqualTo(club.getAgeFrom())
                    .isLessThanOrEqualTo(club.getAgeTo());
            assertTrue(club.getLocations().stream().map(location -> location.getCity().getName())
                    .anyMatch(testClub.getCityName()::contains));
            assertTrue(club.getLocations().stream().map(location -> location.getDistrict().getName())
                    .anyMatch(testClub.getDistrictName()::contains));
            assertTrue(club.getLocations().stream().map(location -> location.getStation().getName())
                    .anyMatch(testClub.getStationName()::contains));
            assertTrue(club.getCategories().stream().map(Category::getName)
                    .anyMatch(testClub.getCategoriesNames()::contains));
        });
    }

    //@Test
    void advancedSearchWithPageableShouldReturnPageWithSomeClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubAsPage();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(null, null, null, null, null, null, null,
                PageRequest.of(testClub.getPageNumber(), 6)).getContent();

        assertThat(clubs).hasSize(6);
    }
}
