package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.utils.club_test_data.ClubAdvancedSearchRepository;
import com.softserve.teachua.repository.utils.club_test_data.ClubSearchTestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ClubRepositoryIT {
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
    public void findByExistingIdShouldReturnOptionalOfCorrectClubEntity() {
        assertThat(clubRepository.findById(EXISTING_ID)).isInstanceOf(Optional.class);
        assertThat(clubRepository.findById(EXISTING_ID).get()).isInstanceOf(Club.class);
        assertThat(clubRepository.findById(EXISTING_ID).get().getId()).isEqualTo(EXISTING_ID);
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(clubRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findByExistingNameShouldReturnOptionalOfCorrectClubEntity() {
        assertThat(clubRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(clubRepository.findByName(EXISTING_NAME).get()).isInstanceOf(Club.class);
        assertThat(clubRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(clubRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllByOrderByIdAscTest() {
        List<Club> list = clubRepository.findAllByOrderByIdAsc();

        assertThat(list.size()).isGreaterThan(0);

        for (int i = 1; i < list.size(); i++) {
            assertThat(list.get(i).getId()).isGreaterThan(list.get(i - 1).getId());
        }
    }

    @Test
    public void existsByExistingNameShouldReturnTrue() {
        assertTrue(clubRepository.existsByName(EXISTING_NAME));
    }

    @Test
    public void existsByNotExistingNameShouldReturnFalse() {
        assertFalse(clubRepository.existsByName(NOT_EXISTING_NAME));
    }

    @Test
    public void existsByExistingIdShouldReturnTrue() {
        assertTrue(clubRepository.existsById(EXISTING_ID));
    }

    @Test
    public void existsByNotExistingIdShouldReturnFalse() {
        assertFalse(clubRepository.existsById(NOT_EXISTING_ID));
    }

    @Test
    public void findAllByExistingUserIdShouldReturnListOfCorrectClubEntities() {
        List<Club> list = clubRepository.findAllByUserId(EXISTING_USER_ID);

        assertThat(list.size()).isGreaterThan(0);

        for (Club club : list) {
            assertThat(club.getUser().getId()).isEqualTo(EXISTING_USER_ID);
        }
    }

    @Test
    public void findAllByNotExistingUserIdShouldReturnEmptyList() {
        assertThat(clubRepository.findAllByUserId(NOT_EXISTING_USER_ID).size()).isEqualTo(0);
    }

    @Test
    public void findAllByExistingUserIdShouldReturnPageOfCorrectClubEntities() {
        Pageable pageable = PageRequest.of(0, 6);

        Page<Club> page = clubRepository.findAllByUserId(EXISTING_USER_ID, pageable);

        assertThat(page.getContent().size()).isGreaterThan(0).isLessThanOrEqualTo(6);

        for (Club club : page.getContent()) {
            assertThat(club.getUser().getId()).isEqualTo(EXISTING_USER_ID);
        }
    }

    @Test
    public void findAllByNotExistingUserIdShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 6);

        assertThat(clubRepository.findAllByUserId(NOT_EXISTING_USER_ID, pageable).getContent().size()).isEqualTo(0);
    }

    @Test
    public void findClubsByExistingCenterShouldReturnCorrectClubEntitiesList() {
        assertThat(centerRepository.findById(EXISTING_CENTER_ID)).isInstanceOf(Optional.class);
        assertThat(centerRepository.findById(EXISTING_CENTER_ID).get()).isInstanceOf(Center.class);
        assertThat(centerRepository.findById(EXISTING_CENTER_ID).get().getId()).isEqualTo(EXISTING_CENTER_ID);

        Center center = centerRepository.findById(EXISTING_CENTER_ID).get();

        List<Club> clubs = clubRepository.findClubsByCenter(center);

        assertThat(clubs.size()).isGreaterThan(0);

        for (Club club : clubs) {
            assertThat(club.getCenter()).isEqualTo(center);
        }
    }

    @Test
    public void findClubsByNotExistingCenterShouldReturnEmptyList() {
        Center center = Center.builder().id(NOT_EXISTING_CENTER_ID).name(NOT_EXISTING_CENTER_NAME).build();

        List<Club> clubs = clubRepository.findClubsByCenter(center);

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithCorrectAgeShouldReturnListOfCorrectClubEntities() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectAge();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                testClub.getAge(),
                null,
                null,
                null,
                null,
                null,
                null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertThat(testClub.getAge())
                    .isGreaterThanOrEqualTo(club.getAgeFrom())
                    .isLessThanOrEqualTo(club.getAgeTo());
        });
    }

    @Test
    public void advancedSearchWithAgeThatIsLessThanIsAllowedShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithLessAge();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                testClub.getAge(),
                null, null,
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithAgeThatIsGreaterThanIsAllowedShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithGreaterAge();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                testClub.getAge(),
                null, null,
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithOnlineClubsShouldReturnAllClubsWitchIsOnline() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getOnlineClub();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                null, null,
                testClub.isOnline(), null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(club.getIsOnline());
        });
    }

    @Test
    public void advancedSearchWithNotOnlineClubsShouldReturnAllClubsWitchIsNotOnline() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getNotOnlineClub();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                null, null,
                testClub.isOnline(), null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertFalse(club.getIsOnline());
        });
    }

    @Test
    public void advancedSearchWithOneCategoryNameShouldReturnAllClubsWithThatCategory() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithOneCategoryName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                null, testClub.getCategoriesNames(),
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(club.getCategories().stream()
                    .map(category -> category.getName())
                    .anyMatch(testClub.getCategoriesNames()::contains));
        });
    }

    @Test
    public void advancedSearchWithManyCategoryNameShouldReturnAllClubsWithThatCategories() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithManyCategoryName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                null, testClub.getCategoriesNames(),
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(club.getCategories().stream()
                    .map(category -> category.getName())
                    .anyMatch(testClub.getCategoriesNames()::contains));
        });
    }

    @Test
    public void advancedSearchWithZeroCategoryNamesShouldReturnAllClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithZeroCategoryName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                null, testClub.getCategoriesNames(),
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);
    }

    @Test
    public void advancedSearchWithExistingCityName1ShouldReturnListOfClubsFromThatCity() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithExistingCityName1();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, testClub.getCityName(), null,
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(
                    club.getLocations().stream()
                            .map(location -> location.getCity().getName())
                            .anyMatch(testClub.getCityName()::contains));
        });
    }

    @Test
    public void advancedSearchWithExistingCityName2ShouldReturnListOfClubsFromThatCity() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithExistingCityName2();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, testClub.getCityName(), null,
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(
                    club.getLocations().stream()
                            .map(location -> location.getCity().getName())
                            .anyMatch(testClub.getCityName()::contains));
        });
    }

    @Test
    public void advancedSearchWithNotExistingCityNameShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithNotExistingCityName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, testClub.getCityName(), null,
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithExistingDistrictShouldReturnCorrectListOfClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectDistrict();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, testClub.getDistrictName(),
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(club.getLocations().stream()
                    .map(location -> location.getDistrict().getName())
                    .anyMatch(testClub.getDistrictName()::contains));
        });
    }

    @Test
    public void advancedSearchWithNotExistingDistrictShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithIncorrectDistrict();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, testClub.getDistrictName(),
                null, null,
                null, null).getContent();

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithExistingStationNameShouldReturnCorrectListOfClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithCorrectStationName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                testClub.getStationName(), null,
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertTrue(club.getLocations().stream()
                    .map(location -> location.getStation().getName())
                    .anyMatch(testClub.getStationName()::contains));
        });
    }

    @Test
    public void advancedSearchWithNotExistingStationNameShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubWithIncorrectStationName();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null,
                testClub.getStationName(), null,
                null, null).getContent();

        assertThat(clubs.size()).isEqualTo(0);
    }

    @Test
    public void advancedSearchWithAllCorrectParamsShouldReturnEmptyList() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubAsListWithAllCorrectData();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                testClub.getName(),
                testClub.getAge(),
                testClub.getCityName(),
                testClub.getDistrictName(),
                testClub.getStationName(),
                testClub.getCategoriesNames(),
                null, null).getContent();

        assertThat(clubs.size()).isGreaterThan(0);

        clubs.forEach(club -> {
            assertThat(testClub.getAge())
                    .isGreaterThanOrEqualTo(club.getAgeFrom())
                    .isLessThanOrEqualTo(club.getAgeTo());
            assertTrue(club.getLocations().stream()
                    .map(location -> location.getCity().getName())
                    .anyMatch(testClub.getCityName()::contains));
            assertTrue(club.getLocations().stream()
                    .map(location -> location.getDistrict().getName())
                    .anyMatch(testClub.getDistrictName()::contains));
            assertTrue(club.getLocations().stream()
                    .map(location -> location.getStation().getName())
                    .anyMatch(testClub.getStationName()::contains));
            assertTrue(club.getCategories().stream()
                    .map(category -> category.getName())
                    .anyMatch(testClub.getCategoriesNames()::contains));
        });
    }

    @Test
    public void advancedSearchWithPageableShouldReturnPageWithSomeClubs() {
        ClubSearchTestEntity testClub = ClubAdvancedSearchRepository.getClubAsPage();

        List<Club> clubs = clubRepository.findAllBylAdvancedSearch(
                null,
                null, null, null, null, null, null,
                PageRequest.of(testClub.getPageNumber(), 6)).getContent();

        assertThat(clubs.size()).isEqualTo(6);
    }
}
