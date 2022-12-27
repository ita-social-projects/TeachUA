package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.dataproviders.AdvancedSearchTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import com.softserve.edu.testcases.enums.Categories;
import com.softserve.edu.testcases.enums.KyivDistricts;
import com.softserve.edu.testcases.enums.KyivMetroStations;
import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.testcases.listeners.TestListener;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

// Add Listeners annotation to add custom test listener class with video recorder
@Listeners(TestListener.class)
public class AdvancedSearchTest extends BaseTestSetup {

    @Description("[Розширений пошук] Verify that the clubs can be sorted by rating")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-516")
    @Test(dataProvider = "sortClubByRate", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void sortClubsByRatingTest(String rateSort, String rateDescendingSort) {
        logger.info("Test if clubs can be sorted by their rating started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Sort clubs by their rate
        clubsPage.sortByRating();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateSort)); // Not equal clubs number on UI and DB

        // Sort clubs by rates and in descending order
        clubsPage.descendingSort();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(rateDescendingSort));

        logger.info("Test if clubs can be sorted by their rating finished");
    }

    @Description("[Розширений пошук] Verify that the clubs can be sorted alphabetically")
    @Severity(SeverityLevel.MINOR)
    @Test(dataProvider = "sortClubAlphabetically", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void sortClubsAlphabeticallyTest(String ascSort, String descSort) {
        logger.info("Test if clubs can be sorted alphabetically started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Sort clubs by their rate
        clubsPage.alphabeticSort();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(ascSort));

        // Sort clubs by rates and in descending order
        clubsPage.descendingSort();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(descSort));

        logger.info("Test if clubs can be sorted alphabetically finished");
    }

    @Description("[Розширений пошук] Verify that the search results are updated after changing the search parameters")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-515")
    @Test(dataProvider = "places", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
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
    public void deactivateExtraCenterParametersTest() {
        logger.info("Test if all center mandatory fields are present and extra are disabled after clicking 'Центр' radio button started");

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
        Assert.assertTrue(clubsPage.areExtraFieldsNotPresent());

        logger.info("Test if all center mandatory fields are present and extra are disabled after clicking 'Центр' radio button finished");
    }

    @Description("[Розширений пошук] Verify that all parameters are activated with the selected 'Гурток' radio button")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-509")
    @Test
    public void allClubParametersPresenceTest() {
        logger.info("Test if all club mandatory and extra fields are present after opening advanced search started");

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

        logger.info("Test if all club mandatory and extra fields are present after opening advanced search finished");
    }

    // TODO Double-check once the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1562
    @Description("[Розширений пошук] Verify that the centers in the results of advanced search can be displayed as a list")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-513")
    @Test
    public void displayCentersAsListTest() {
        logger.info("Test if center component view was changed after selecting list view started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Select Center radio button
        clubsPage.selectCenters();

        // Display clubs as a list
        clubsPage.displayComponentsAsList();

        // Assert check if all component fields are present after switching to the list view
        Assert.assertTrue(clubsPage.areAllCenterFieldsPresentInListView());

        logger.info("Test if center component view was changed after selecting list view finished");
    }

    @Description("[Розширений пошук] Verify that the clubs in the results of advanced search can be displayed as a list")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void displayClubsAsListTest() {
        logger.info("Test if club component view was changed after selecting list view started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Display clubs as a list
        clubsPage.displayComponentsAsList();

        // Assert check if all component fields are present after switching to the list view
        Assert.assertTrue(clubsPage.areAllClubFieldsPresentInListView());

        logger.info("Test if club component view was changed after selecting list view finished");
    }

    @Description("[Розширений пошук] Verify that the system doesn't accept invalid data in the text field of the 'Вік дитини' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-495")
    @Test(dataProvider = "childAge", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
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

    // TODO Check once the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1569
    @Description("[Розширений пошук] Verify that the user can find a center in a certain location using the 'Найближча станція метро' parameter")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-456")
    @Test(dataProvider = "stations", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
    public void findCenterUsingMetroStationTest(String city, String beresteyska, String stationBer, String vyrlitsa, String stationVyr) {
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
        clubsPage.chooseMetroStation(beresteyska);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(stationBer));

        // Choose another metro station
        clubsPage.chooseMetroStation(vyrlitsa);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(stationVyr)); // Does not show any club

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
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(districtQuery)); // Unknown sort is used

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
        clubsPage.chooseMetroStation(KyivMetroStations.BORYSPILSKA);

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

    // TODO Double-check once the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1562
    @Description("[Розширений пошук] Verify that the user can find a center in a certain location using the 'Місто' parameter")
    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "centersByLocation", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
    public void findCentersByLocationTest(String city, String kyivLocation, String kharkivLocation) {
        logger.info("Test if centers that belongs to a certain location will be found correctly started");

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

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(kyivLocation));

        // Choose another metro station
        clubsPage.chooseCity(Locations.KHARKIV);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(kharkivLocation)); // Fails after unreasonable page switch

        logger.info("Test if centers that belongs to a certain location will be found correctly finished");
    }

    // TODO Double-check once the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1569
    @Description("[Розширений пошук] Verify that the user can sort the search results by rating after clicking on the 'Центр' radio button")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-449")
    @Test(dataProvider = "sortCentersByRating", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
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
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(ascRating));

        // Sort clubs by rates in descending order
        clubsPage.descendingSort();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(descRating));

        logger.info("Test if sort by rating functionality works properly finished");
    }

    // TODO Double-check once the following bug is fixed: https://github.com/ita-social-projects/TeachUA/issues/1562
    @Description("[Розширений пошук] Verify that the user can sort the search results alphabetically after clicking on the 'Центр' radio button")
    @Severity(SeverityLevel.MINOR)
    @TmsLink(value = "TUA-440")
    @Test(dataProvider = "alphabeticCenterSort", dataProviderClass = AdvancedSearchTestDataProvider.class, enabled = false)
    public void findCenterSortedAlphabeticallyTest(String city, String ascAlphabet, String descAlphabet) {
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
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(ascAlphabet));

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

    @Description("Verify that user can find club using 'Категорії' check box")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-294")
    @Test(dataProvider = "clubsByCategories", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findClubsUsingCategoriesCheckbox(String city, String programmingCategory, Categories... category) {
        logger.info("Test if clubs with selected category will be found started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Select category
        clubsPage.selectCategories(category);

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(programmingCategory));

        logger.info("Test if clubs with selected category will be found finished");
    }

    @Description("[Розширений пошук] Verify that the user can find a clubs that are available remotely")
    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "remoteClubs", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findRemoteClubs(String city, String remoteOption) {
        logger.info("Test if all clubs with Remote option will be found started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Select category
        clubsPage.checkRemoteOption();

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(remoteOption));

        logger.info("Test if all clubs with Remote option will be found finished");
    }

    @Description("[Розширений пошук] Verify that the user can select 'Центр' option to see all the centers")
    @Severity(SeverityLevel.BLOCKER)
    @Test(dataProvider = "centersInCity", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void findCentersInCityTest(String city, String centers) {
        logger.info("Test if there is the same number of centers on UI and DB started");

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

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllCenterTitles(), db.getList(centers));

        logger.info("Test if there is the same number of centers on UI and DB finished");
    }

    @Description("[Розширений пошук] Verify that when user type valid child age the system show clubs in that age range")
    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "validChildAge", dataProviderClass = AdvancedSearchTestDataProvider.class)
    public void validDataForChildAgeTest(String city, String age, String checkAge) {
        logger.info("Test if the system finds clubs with valid child age data started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Assert check if default city is set as Kyiv
        Assert.assertEquals(clubsPage.getCityName(), city);

        // Set child's age
        clubsPage.setChildAge(age);

        // Assert check that child's age was entered correctly
        Assert.assertTrue(clubsPage.isChildAgeCorrect());

        // Assert check that the search results on UI and DB are the same
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(checkAge));

        logger.info("Test if the system finds clubs with valid child age data finished");
    }

}
