package com.softserve.edu.test.rest.clubs;

import com.softserve.edu.models.placeholder.ClubResponseDto;
import com.softserve.edu.services.api.Services;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.dataproviders.ApiDataProvider;
import com.softserve.edu.testcases.testdata.ApiTestData;
import com.softserve.edu.testcases.testdata.TestData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

public class ClubCrudTest extends BaseTestSetup {

    // TODO Check again after the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1599
    @Test
    public void testCreatingNewClub() {
        ClubResponseDto clubResponseDto = TestData.clubTestData().newClub();
        ClubResponseDto createdClubResponse = Services.placeHolderApi().club().create(clubResponseDto);

        // Verification
        Assertions.assertThat(createdClubResponse)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .comparingOnlyFields("name", "description", "ageFrom", "ageTo");
    }

    @Test
    public void testFullClubUpdate() {
        int clubId = TestData.clubTestData().updatedWholeClub().getId();
        ClubResponseDto clubResponseDto = TestData.clubTestData().updatedWholeClub();
        ClubResponseDto updateClubResponse = Services.placeHolderApi().club().updateAllFields(clubId, clubResponseDto);

        // Verification
        Assertions.assertThat(updateClubResponse)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .ignoringCollectionOrder()
                .ignoringFields("id", "locations", "contacts")
                .isEqualTo(clubResponseDto);
    }

    @Test
    public void testPartialClubUpdate() {
        int clubId = TestData.clubTestData().patchClub().getId();
        ClubResponseDto clubResponseDto = TestData.clubTestData().patchClub();
        ClubResponseDto patchClubResponse = Services.placeHolderApi().club().updateSomeFields(clubId, clubResponseDto);

        // Verification
        Assertions.assertThat(patchClubResponse)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .comparingOnlyFields("ageFrom", "ageTo")
                .isEqualTo(clubResponseDto);
    }

    @Test
    public void testGettingClubById() {
        // Get correct club data as expected result
        ClubResponseDto clubResponseDto = ApiTestData.clubTestData().randomClub();
        // Get certain club id to use it in getting data about it from backend
        int clubId = ApiTestData.clubTestData().randomClub().getId();
        // Get data about club with a certain id as actual result
        ClubResponseDto club = Services.placeHolderApi().club().getById(clubId);

        // Verification
        Assertions.assertThat(club)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(clubResponseDto);
    }

    @Test(dataProvider = "allClubs", dataProviderClass = ApiDataProvider.class)
    public void testGettingAllClubs(String allClubNames) {
        // Get data about all clubs as actual result
        List<ClubResponseDto> clubs = Services.placeHolderApi().club().getAll();

        // Verification
        Assertions.assertThat(clubs)
                .extracting("name")
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
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
                .extracting("name")
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(db.getList(clubByCenterId));
    }

    @Test
    public void testClubDeletion() {
        int clubId = TestData.clubTestData().deleteClub().getId();
        ClubResponseDto clubResponseDto = TestData.clubTestData().deleteClub();
        ClubResponseDto deleteClubResponse = Services.placeHolderApi().club().deleteById(clubId);

        // Verification
        Assertions.assertThat(deleteClubResponse)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .comparingOnlyFields("id", "name", "ageFrom", "ageTo")
                .isEqualTo(clubResponseDto);
    }

}
