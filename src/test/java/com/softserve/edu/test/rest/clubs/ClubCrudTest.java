package com.softserve.edu.test.rest.clubs;

import com.softserve.edu.models.placeholder.ClubDto;
import com.softserve.edu.services.api.Services;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.testdata.ApiTestData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class ClubCrudTest extends BaseTestSetup {

    @Test
    public void testGettingClubById() {
        ClubDto clubDto = ApiTestData.clubTestData().randomClub();
        int clubId = ApiTestData.clubTestData().randomClub().getId();
        ClubDto club = Services.placeHolderApi().club().getClubById(clubId);

        // Verification
        Assertions.assertThat(club)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(clubDto);
    }

}
