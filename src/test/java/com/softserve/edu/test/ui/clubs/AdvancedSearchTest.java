package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.dataproviders.AdvancedSearchTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.enums.KyivDistricts;
import com.softserve.edu.testcases.enums.KyivMetroStations;
import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.testcases.listeners.TestListener;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

// Add Listeners annotation to add custom test listener class with video recorder
@Listeners(TestListener.class)
public class AdvancedSearchTest extends BaseTestSetup {

    @Description("[Розширений пошук] Verify that the clubs can be sorted by rating")
    @Severity(SeverityLevel.MINOR)
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
        clubsPage.sortByRating();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateSort)); // Not equal clubs number on UI and DB

        // Sort clubs by rates and in descending order
        clubsPage.descendingSort();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateAscendingSort));

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
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(citySort)); // Not equal clubs number on UI and DB

        // Choose the first district
        clubsPage.chooseDistrict(KyivDistricts.SVYATOSHINSKY);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(districtSvSort)); // Not equal clubs number on UI and DB

        // Choose another district
        clubsPage.chooseDistrict(KyivDistricts.DESNYANSKYI);

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

        logger.info("Test if the search results are updated after changing the search parameters finished");
    }

    @Description("[Розширений пошук] Verify that the system doesn't accept invalid data in the text field of the 'Вік дитини' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-495")
    @Test(dataProvider = "childAge", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void invalidDataForChildAgeTest(String age) {
        logger.info("Test if the system accepts invalid data entered into child age field or not started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Scroll to child age field
        clubsPage.scrolltoChildAgeField();

        // Set child's age
        clubsPage.setChildAge(age);

        // Assert check that child's age was entered correctly
        Assert.assertTrue(clubsPage.isChildAgeCorrect());

        logger.info("Test if the system accepts invalid data entered into child age field or not finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a center in a certain location using the 'Найближча станція метро' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-456")
    @Test(dataProvider = "stations", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findCenterUsingMetroStationTest(String city, String stationSv, String stationVyr) {
        logger.info("Test if the search results are updated after changing the search parameters started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Choose metro station
        clubsPage.chooseMetroStation(KyivMetroStations.ARSENALNA);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(stationSv));

        // Choose another metro station
        clubsPage.chooseMetroStation(KyivMetroStations.VYRLITSA);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(stationVyr));

        logger.info("Test if the search results are updated after changing the search parameters finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a center in a certain location using the 'Район міста' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-455")
    @Test(dataProvider = "district", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findCenterSelectingDistrictTest(String city, String district, String districtQuery) {
        logger.info("Test if the search results are updated after adding district in search parameters started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Choose metro station
        clubsPage.chooseDistrict(district);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(districtQuery)); // Spelling mistake on DB center title

        logger.info("Test if the search results are updated after adding district in search parameters finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a club in a certain location using the 'Найближча станція метро' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-453")
    @Test(dataProvider = "clubStations", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findClubsSelectingMetroStationTest(String city, String stationSv, String stationVyr) {
        logger.info("Test if the search results are updated after changing nearest metro station in search parameters started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Choose metro station
        clubsPage.chooseMetroStation(KyivMetroStations.ARSENALNA);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(stationSv)); // Not equal clubs number on UI and DB

        // Choose another metro station
        clubsPage.chooseMetroStation(KyivMetroStations.VYRLITSA);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(stationVyr)); // Not equal clubs number on UI and DB

        logger.info("Test if the search results are updated after changing nearest metro station in search parameters finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a club in a certain location using the 'Район міста' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-452")
    @Test(dataProvider = "clubDistricts", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findClubAfterAddingDistrictTest(String city, String districtQuery) {
        logger.info("Test if the search results are updated after adding district started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Choose another district
        clubsPage.chooseDistrict(KyivDistricts.DESNYANSKYI);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(districtQuery));

        logger.info("Test if the search results are updated after adding district finished");
    }

    @Description("[Розширений пошук] Verify that the user can sort the search results by rating after clicking on the 'Центр' radio button")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-449")
    @Test(dataProvider = "sortCentersByRating", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findCenterSortedByRatingTest(String city, String ascRating, String descRating) {
        logger.info("Test if sort by rating functionality works properly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Sort centers by rate
        clubsPage.sortByRating();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(ascRating)); // Fails due to incorrect sort logic

        // Sort clubs by rates in descending order
        clubsPage.descendingSort();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(descRating));

        logger.info("Test if sort by rating functionality works properly finished");
    }

    @Description("[Розширений пошук] Verify that the user can sort the search results alphabetically after clicking on the 'Центр' radio button")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-440")
    @Test(dataProvider = "alphabeticCenterSort", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findCenterSortedByTitleTest(String city, String ascAlphabet, String descAlphabet) {
        logger.info("Test if alphabetic center sort functionality works properly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Sort centers by alphabet
        clubsPage.alphabeticSort();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(ascAlphabet)); // Fails due to long content update

        // Sort clubs by rates in descending order
        clubsPage.descendingSort();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(descAlphabet));

        logger.info("Test if alphabetic center sort functionality works properly finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a club in a certain location using the 'Місто' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-245")
    @Test(dataProvider = "clubsByLocation", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findClubsByLocationTest(String city, String kyivLocation, String kharkivLocation) {
        logger.info("Test if clubs that belongs to a certain location will be found correctly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(kyivLocation)); // Not equal clubs number on UI and DB

        // Choose another metro station
        clubsPage.chooseCity(Locations.KHARKIV);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(kharkivLocation));

        logger.info("Test if clubs that belongs to a certain location will be found correctly finished");
    }

}
