package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.testcases.repositories.club.Club;
import com.softserve.edu.testcases.dataproviders.SearchClubTestDataProvider;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchClubTest extends BaseTestSetup {

    @Test(dataProvider = "clubData", dataProviderClass = SearchClubTestDataProvider.class)
    public void basicClubSearchTest(Club club) {
        logger.info("Test if club that is send to the search field in basic search will be found on the clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Send text into search top field and press search button
        clubsPage.searchClub(club.getTitle());

        // Assert check if searched club is present on the page
        Assert.assertTrue(clubsPage.isClubPresentOnThePage(club.getTitle()));   // Application crashes after 7 second wait

        logger.info("Test if club that is send to the search field in basic search will be found on the clubs page finished");
    }

    @Test(dataProvider = "clubData", dataProviderClass = SearchClubTestDataProvider.class)
    public void advancedClubSearchTest(Club club) {
        logger.info("Test if club that is send to the search field in advanced search will be found on the clubs page started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Go to clubs page
        ClubsPage clubsPage  = homePage.gotoClubsPage();

        // Open advanced search
        clubsPage.openAdvancedSearchPart();

        // Send text into search top field and press search button
        clubsPage.searchClub(club.getTitle());

        // Assert check if searched club is present on the page
        Assert.assertTrue(clubsPage.isClubPresentOnThePage(club.getTitle()));   // Application crashes after 7 second wait

        logger.info("Test if club that is send to the search field in advanced search will be found on the clubs page finished");
    }

}
