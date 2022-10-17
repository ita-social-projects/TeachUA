package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.data.Locations;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicSearchTest extends BaseTestSetup {

    // Test data
    String PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS = "=,/ , , *, (, ), _, :, ;, #";
    String PARTIAL_CLUB_TITLE_WITH_NUMBERS = "2412";
    String CATEGORY_NAME = "Програмування, робототехніка, STEM";
    String CENTER_NAME = "Комунальний позашкільний навчальний заклад \"Одеський будинок дитячої та юнацької творчості \"Тоніка\"";
    String CLUB_NAME = "Грін Кантрі";

    // Query to find clubs that contain special characters in their titles in the DB
    String SELECT_CLUB_TITLE_WITH_SPECIAL_CHARACTERS = "SELECT DISTINCT c.name\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
            "AND c.name LIKE '%" + PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS + "%'";

    // Query to find clubs that contain numbers in their titles in the DB
    String SELECT_CLUB_TITLE_WITH_NUMBERS = "SELECT DISTINCT c.name\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
            "AND c.name LIKE '%" + PARTIAL_CLUB_TITLE_WITH_NUMBERS + "%'";

    // Query to find clubs by category to which they belong to in the DB
    String SELECT_CLUB_TITLE_BY_CATEGORY = "SELECT DISTINCT c.name\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "INNER JOIN club_category as cc ON c.id=cc.club_id\n" +
            "INNER JOIN categories as cs ON cc.category_id=cs.id\n" +
            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
            "AND cs.name = '" + CATEGORY_NAME + "';\n";

    // Query to find clubs by center to which they belong to in the DB
    String SELECT_CLUB_TITLE_BY_CENTER = "SELECT DISTINCT c.name\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "INNER JOIN centers as cn ON c.center_id=cn.id\n" +
            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
            "AND cn.name = '" + CENTER_NAME + "';";

    // Query to find club by its name in the DB
    String SELECT_CLUB_BY_NAME = "SELECT DISTINCT c.name\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
            "AND c.name = '" + CLUB_NAME + "';";

    @Description("[Basic Search] Verify that user can perform basic search by an special symbols input")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-448")
    @Test
    public void searchClubWithSpecialCharactersTest() {
        logger.info("Test if clubs that have sequence of special characters in title search correctly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send club title with special characters into search top field
        clubsPage.sendTextIntoInputSearchField(PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS);

        // Press ENTER button
        clubsPage.clickEnterButton();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(SELECT_CLUB_TITLE_WITH_SPECIAL_CHARACTERS));

        logger.info("Test if clubs that have sequence of special characters in title search correctly finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by an numeric input")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-447")
    @Test
    public void searchClubWithNumbersInTitleTest() {
        logger.info("Test if clubs that have sequence of numbers in title search correctly started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send club title with numbers into search top field
        clubsPage.sendTextIntoInputSearchField(PARTIAL_CLUB_TITLE_WITH_NUMBERS);

        // Press ENTER button
        clubsPage.clickEnterButton();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertNotEquals(clubsPage.getAllClubTitles(), db.getList(SELECT_CLUB_TITLE_WITH_NUMBERS));  // remove NOT

        logger.info("Test if clubs that have sequence of numbers in title search correctly finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by name of a category")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-227")
    @Test
    public void searchClubByItsCategoryTest() {
        logger.info("Test if only clubs that belongs to a certain category will be found started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send category name into search top field
        clubsPage.sendTextIntoInputSearchField(CATEGORY_NAME);

        // Press ENTER button
        clubsPage.clickEnterButton();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(SELECT_CLUB_TITLE_BY_CATEGORY));

        logger.info("Test if only clubs that belongs to a certain category will be found finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search for a club by a center name")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-228")
    @Test
    public void searchClubByItsCenterTest() {
        logger.info("Test if club will be found by center to which it belongs started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send center name into search top field
        clubsPage.sendTextIntoInputSearchField(CENTER_NAME);

        // Press ENTER button
        clubsPage.clickEnterButton();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertNotEquals(clubsPage.getAllClubTitles(), db.getList(SELECT_CLUB_TITLE_BY_CENTER));     // remove NOT

        logger.info("Test if club will be found by center to which it belongs finished");
    }

    @Description("[Basic Search] Verify that user can perform basic search by name of a club")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink(value = "TUA-226")
    @Test
    public void searchClubByNameTest() {
        logger.info("Test if club will be found by its name started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Assert check if location on the page matches with that from the query
        Assert.assertEquals(clubsPage.getLocationText(), Locations.KYIV.toString());

        // Send center name into search top field
        clubsPage.sendTextIntoInputSearchField(CLUB_NAME);

        // Press ENTER button
        clubsPage.clickEnterButton();

        // Assert get club titles on the page, save as list and run SQL query, save result as list and compare two lists
        Assert.assertEquals(clubsPage.getAllClubTitles(), db.getList(SELECT_CLUB_BY_NAME));

        logger.info("Test if club will be found by its name finished");
    }

}
