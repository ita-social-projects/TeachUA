package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.testcases.repositories.club.Club;
import com.softserve.edu.testcases.dataproviders.BasicSearchTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicSearchTest extends BaseTestSetup {

    @Description("[Basic Search] Verify that user can perform basic search by an special symbols input")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-448")
    @Test(dataProvider = "specialCharactersTitle", dataProviderClass = BasicSearchTestDataProvider.class)
    public void searchClubWithSpecialCharactersTest(String specialCharacters, String specialCharactersTitle) {
        logger.info("Test if clubs that have sequence of special characters in title search correctly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send club title with special characters into search top field
        clubsPage.typeClubTitle(specialCharacters);

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(specialCharactersTitle));

        logger.info("Test if clubs that have sequence of special characters in title search correctly finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by an numeric input")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-447")
    @Test(dataProvider = "numberTitle", dataProviderClass = BasicSearchTestDataProvider.class)
    public void searchClubWithNumbersInTitleTest(String numbers, String numberTitle) {
        logger.info("Test if clubs that have sequence of numbers in title search correctly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send club title with numbers into search top field
        clubsPage.typeClubTitle(numbers);

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertNotEquals(clubsPage.getAllClubTitles(), db.getList(numberTitle));          // remove NOT after bug fix

        logger.info("Test if clubs that have sequence of numbers in title search correctly finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by name of a category")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-227")
    @Test(dataProvider = "clubsByCategory", dataProviderClass = BasicSearchTestDataProvider.class)
    public void searchClubByItsCategoryTest(Club club, String clubsByCategory) {
        logger.info("Test if only clubs that belongs to a certain category will be found started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send category name into search top field
        clubsPage.typeClubTitle(club.getCategory());

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(clubsByCategory));

        logger.info("Test if only clubs that belongs to a certain category will be found finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search for a club by a center name")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-228")
    @Test(dataProvider = "clubsByCenter", dataProviderClass = BasicSearchTestDataProvider.class)
    public void searchClubByItsCenterTest(String centerName, String clubsByCenter) {
        logger.info("Test if club will be found by center to which it belongs started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send center name into search top field
        clubsPage.typeClubTitle(centerName);

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(clubsByCenter));

        logger.info("Test if club will be found by center to which it belongs finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by name of a club")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink(value = "TUA-226")
    @Test(dataProvider = "clubByTitle", dataProviderClass = BasicSearchTestDataProvider.class)
    public void searchClubByNameTest(Club club, String clubByTitle) {
        logger.info("Test if club will be found by its name started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Assert check if location on the page matches with that from the query
        Assert.assertEquals(clubsPage.getLocationText(), Locations.KYIV.toString());

        // Send center name into search top field
        clubsPage.typeClubTitle(club.getTitle());

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(clubByTitle));

        logger.info("Test if club will be found by its name finished");
    }

}
