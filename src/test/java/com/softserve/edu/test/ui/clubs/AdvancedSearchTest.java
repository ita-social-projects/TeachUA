package com.softserve.edu.test.ui.clubs;

import com.softserve.edu.data.Locations;
import com.softserve.edu.data.dataproviders.AdvancedSearchTestDataProvider;
import com.softserve.edu.data.dataproviders.BasicSearchTestDataProvider;
import com.softserve.edu.pages.guest.clubs.ClubsPage;
import com.softserve.edu.pages.guest.home.HomePage;
import com.softserve.edu.testcases.BaseTestSetup;
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

}
