package com.softserve.teachua.repository.utils.club_test_data;

import java.util.Arrays;
import java.util.List;

public class ClubAdvancedSearchRepository {
    public static ClubSearchTestEntity getClubWithCorrectAge() {
        return ClubSearchTestEntity.builder().age(5).build();
    }

    public static ClubSearchTestEntity getClubWithLessAge() {
        return ClubSearchTestEntity.builder().age(1).build();
    }

    public static ClubSearchTestEntity getClubWithGreaterAge() {
        return ClubSearchTestEntity.builder().age(19).build();
    }

    public static ClubSearchTestEntity getOnlineClub() {
        return ClubSearchTestEntity.builder().isOnline(true).build();
    }

    public static ClubSearchTestEntity getNotOnlineClub() {
        return ClubSearchTestEntity.builder().isOnline(false).build();
    }

    public static ClubSearchTestEntity getClubWithExistingCityName1() {
        return ClubSearchTestEntity.builder().cityName("Київ").build();
    }

    public static ClubSearchTestEntity getClubWithExistingCityName2() {
        return ClubSearchTestEntity.builder().cityName("Харків").build();
    }

    public static ClubSearchTestEntity getClubWithNotExistingCityName() {
        return ClubSearchTestEntity.builder().cityName("Івано-Франківськ").build();
    }

    public static ClubSearchTestEntity getClubWithOneCategoryName() {
        return ClubSearchTestEntity.builder().categoriesNames(List.of("Спортивні секції")).build();
    }

    public static ClubSearchTestEntity getClubWithManyCategoryName() {
        return ClubSearchTestEntity.builder().categoriesNames(Arrays.asList("Програмування, робототехніка, STEM",
                "Художні студії, мистецтво, дизайн", "Студії раннього розвитку")).build();
    }

    public static ClubSearchTestEntity getClubWithZeroCategoryName() {
        return ClubSearchTestEntity.builder().categoriesNames(null).build();
    }

    public static ClubSearchTestEntity getClubWithCorrectDistrict() {
        return ClubSearchTestEntity.builder().districtName("Деснянський").build();
    }

    public static ClubSearchTestEntity getClubWithIncorrectDistrict() {
        return ClubSearchTestEntity.builder().districtName("Франківський").build();
    }

    public static ClubSearchTestEntity getClubWithCorrectStationName() {
        return ClubSearchTestEntity.builder().stationName("Академмістечко").build();
    }

    public static ClubSearchTestEntity getClubWithIncorrectStationName() {
        return ClubSearchTestEntity.builder().stationName("Крива").build();
    }

    public static ClubSearchTestEntity getClubAsListWithAllCorrectData() {
        return ClubSearchTestEntity.builder().age(7).cityName("Київ").districtName("Деснянський")
                .stationName("Академмістечко").categoriesNames(List.of("Студії раннього розвитку")).build();
    }

    public static ClubSearchTestEntity getClubAsPage() {
        return ClubSearchTestEntity.builder().pageNumber(1).build();
    }
}
