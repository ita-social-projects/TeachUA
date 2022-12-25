package com.softserve.edu.test.rest.clubs;

import com.softserve.edu.models.placeholder.ClubResponseDto;
import com.softserve.edu.services.api.Services;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.dataproviders.ApiDataProvider;
import com.softserve.edu.testcases.testdata.ApiTestData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

public class ClubCrudTest extends BaseTestSetup {

    @Test
    public void testGettingClubById() {
        // Get correct club data as expected result
        ClubResponseDto clubResponseDto = ApiTestData.clubTestData().randomClub();
        // Get certain club id to use it in getting data about it from backend
        int clubId = ApiTestData.clubTestData().randomClub().getId();
        // Get data about club with a certain id as actual result
        ClubResponseDto club = Services.placeHolderApi().club().getClubById(clubId);

        // Verification
        Assertions.assertThat(club)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(clubResponseDto);
    }

    @Test(dataProvider = "allClubs", dataProviderClass = ApiDataProvider.class)
    public void testGettingAllClubs(String allClubNames) {
        // Get data about all clubs as actual result
        List<ClubResponseDto> clubs = Services.placeHolderApi().club().getAllClubs();

        // Verification
        Assertions.assertThat(clubs)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .comparingOnlyFields("name")
                .isEqualTo(db.getList(allClubNames));
    }

    @Test(dataProvider = "clubsByCenterId", dataProviderClass = ApiDataProvider.class)
    public void testGettingClubsByCenterId(String clubByCenterId) {
        // Get certain center id to use it in getting data about all clubs which belongs to such center
        int centerId = ApiTestData.clubTestData().certainCenter().getId();
        // Get data about club with a certain id as actual result
        List<ClubResponseDto> club = Services.placeHolderApi().club().getClubsByCenterId(centerId);

        // Verification
        Assertions.assertThat(club)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .comparingOnlyFields("id")
                .isEqualTo(db.getList(clubByCenterId));
    }

}
