package com.softserve.teachua.repository.utils.club_test_data;

public class ClubAdvancedSearchRepository {
    public static ClubSearchTestEntity getClubWithCorrectAge(){
        return ClubSearchTestEntity
                .builder()
                .age(2)
                .build();
    }
}
