package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.data.Locations;
import com.softserve.edu.data.club.Club;
import com.softserve.edu.data.club.ClubRepository;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchClubTest extends BaseTestSetup {

    // Test data
    @DataProvider
    public Object[][] clubData() {
        return new Object[][] {
                { ClubRepository.getJeromeITSchool() },                 // get data about Jerome IT School club
        };
    }

//    String CLUB_TITLE = "Jerome IT School";                             // club title
//
//    // Query to find club with a certain title in the DB
//    String SELECT_CLUB_BY_TITLE = "SELECT DISTINCT c.name\n" +
//            "FROM clubs as c\n" +
//            "INNER JOIN locations as l ON c.id=l.club_id\n" +
//            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
//            "WHERE ct.name = '" + Locations.KYIV + "'\n" +
//            "AND c.name = '" + CLUB_TITLE + "';";

    @Test(dataProvider = "clubData")
    public void searchClubTest(Club club) {
        logger.info("Test if club that is send to the search field will be found on the clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage  = homePage.gotoClubsPage();

        // Send text into search top field and press search button
        clubsPage.searchClub(club.getTitle());

        // Assert check if searched club is present on the page
        softAssert.assertTrue(clubsPage.isClubPresentOnThePage(club.getTitle()));

        logger.info("Test if club that is send to the search field will be found on the clubs page finished");
    }

}
