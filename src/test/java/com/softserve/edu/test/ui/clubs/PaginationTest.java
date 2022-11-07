package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.dataproviders.PaginationTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PaginationTest extends BaseTestSetup {

    @Test(dataProvider = "pagination", dataProviderClass = PaginationTestDataProvider.class)
    public void paginationTest(String pagination) {

        logger.info("Test pagination at clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage  = homePage.gotoClubsPage();

        // Save expected number of pages took from database
        int expectedResult = clubsPage.getTotalNumberOfPagesFromDatabase(db.getSingleValue(pagination));

        // Save actual number of pages on Clubs page
        int actualResult = clubsPage.getActualNumberOfPages();

        // Assert compare how many pages were passed with total amount of pages on the page
        Assert.assertEquals(actualResult, expectedResult);

        logger.info("Test pagination at clubs page finished");

    }

}
