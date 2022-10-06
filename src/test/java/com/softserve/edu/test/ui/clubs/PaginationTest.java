package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.data.Locations;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.annotations.Test;

public class PaginationTest extends BaseTestSetup {

    String SELECT_ALL_CLUBS = "SELECT COUNT(DISTINCT c.name)\n" +
            "FROM clubs as c\n" +
            "INNER JOIN locations as l ON c.id=l.club_id\n" +
            "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
            "WHERE ct.name='" + Locations.KYIV + "';";

    @Test
    public void paginationTest() {

        logger.info("Test pagination at clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage  = homePage.gotoClubsPage();

        // Save expected number of pages took from database
        int expectedResult = clubsPage.getTotalNumberOfPagesFromDatabase(db.getSingleValue(SELECT_ALL_CLUBS));

        // Save actual number of pages on Clubs page
        int actualResult = clubsPage.getActualNumberOfPages();

        // Assert compare how many pages were passed with total amount of pages on the page
        softAssert.assertEquals(actualResult, expectedResult);

        logger.info("Test pagination at clubs page finished");

    }

}
