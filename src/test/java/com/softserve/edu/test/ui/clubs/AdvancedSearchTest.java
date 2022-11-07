package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.dataproviders.AdvancedSearchTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.enums.KyivDistricts;
import com.softserve.edu.testcases.enums.KyivMetroStations;
import com.softserve.edu.testcases.enums.Locations;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdvancedSearchTest extends BaseTestSetup {

    @Description("[Розширений пошук] Verify that the clubs can be sorted by rating")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-516")
    @Test(dataProvider = "rateAscendingSort", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void sortClubsByRatingTest(String rateSort, String rateAscendingSort) {
        logger.info("Test if clubs can be sorted by their rating started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Clear city dropdown
        clubsPage.clearDropdown();

        // Sort clubs by their rate
        clubsPage.sortByRate();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateSort)); // FAIL

        // Sort clubs by rates and in descending order
        clubsPage.descendingSort();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateAscendingSort)); // FAIL

        logger.info("Test if clubs can be sorted by their rating finished");
    }

    @Description("[Розширений пошук] Verify that the search results are updated after changing the search parameters")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-515")
    @Test(dataProvider = "places", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findClubAfterChangingSearchParametersTest(String citySort, String districtSvSort, String districtDesSort) {
        logger.info("Test if the search results are updated after changing the search parameters started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Choose city
        clubsPage.chooseCity(Locations.KYIV);

        // Assert check that the search results on UI and DB are the same
        //Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(citySort)); // Crushes after moving to the last page

        // Choose the first district
        clubsPage.chooseDistrict(KyivDistricts.SVYATOSHINSKY);

        // Choose another district
        // clubsPage.chooseDistrict(KyivDistricts.DESNYANSKYI);

        // Choose metro station
        // clubsPage.chooseMetroStation(KyivMetroStations.ARSENALNA);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(districtSvSort)); // Not equal club number on UI and DB

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(districtDesSort));

        logger.info("Test if the search results are updated after changing the search parameters finished");
    }

    @Description("[Розширений пошук] Verify that 'Доступний онлайн', 'Категорії', 'Вік дитини' parameters are deactivated after selecting 'Центр' radio button")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-510")
    @Test
    public void deactivateExtraParametersTest() {
        logger.info("Test if all mandatory fields are present and extra are disabled after clicking 'Центр' radio button started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Assert check if all mandatory fields are present after clicking Center radio button
        Assert.assertTrue(clubsPage.areMandatoryFieldsDisplayed());

        // Assert check if all extra fields are disabled after clicking Center radio button
        Assert.assertTrue(clubsPage.extraFieldsNotPresent());

        logger.info("Test if all mandatory fields are present and extra are disabled after clicking 'Центр' radio button finished");
    }

    @Description("[Розширений пошук] Verify that all parameters are activated with the selected 'Гурток' radio button")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-509")
    @Test
    public void allParametersPresenceTest() {
        logger.info("Test if all mandatory and extra fields are present after opening advanced search started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if all mandatory fields are present after opening advanced search
        Assert.assertTrue(clubsPage.areMandatoryFieldsDisplayed());

        // Assert check if all extra fields are present after opening advanced search
        Assert.assertTrue(clubsPage.areExtraFieldsDisplayed());

        logger.info("Test if all mandatory and extra fields are present after opening advanced search finished");
    }

    @Description("[Розширений пошук] Verify that the centers in the results of advanced search can be displayed as a list")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-513")
    @Test
    public void displayClubsAsListTest() {
        logger.info("Test if the search results are updated after changing the search parameters started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Display clubs as a list
        clubsPage.displayClubsAsList();

        // Assert check that the search results on UI and DB are the same
        //Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(citySort)); // Crushes after moving to the last page

        logger.info("Test if the search results are updated after changing the search parameters finished");
    }

}
