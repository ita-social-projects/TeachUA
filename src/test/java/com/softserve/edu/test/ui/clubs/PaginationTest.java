package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.annotations.Test;

public class PaginationTest extends BaseTestSetup {

    @Test
    public void paginationTest() {

        logger.info("Test pagination at clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage  = homePage.gotoClubsPage();

        // Save expected result
        int expected = clubsPage.getExpectedNumberOfPages();
        // Save actual result
        int actual = clubsPage.getActualNumberOfPages();

        // Write down expected result
        logger.info("Expected result: " + expected);
        // Write down actual result
        logger.info("Actual result: " + actual);

        // Assert compare how many pages were passed with total amount of pages on the page
        softAssert.assertEquals(actual, expected);

        logger.info("Test pagination at clubs page finished");

    }

}
