package com.softserve.teachua.tests.smoke;

import com.softserve.teachua.pages.*;
import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tests.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainMenuTest extends TestRunner {

    @Test
    public void checkMenuLinks() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
        presentationSleep();
        //
        // Steps
        ClubPage clubPage = homePage.gotoClubPage();
        presentationSleep();
        //
        // Check
        //Assertions.assertEquals(ClubPage.CLUB_LABEL_TEXT, clubPage.getClubKyivLabelText());
        Assertions.assertTrue(clubPage.getClubLabelText().contains(ClubPage.CLUB_LABEL_TEXT));
        presentationSleep();
        //
        // Steps
        NewsPage newsPage = clubPage.gotoNewsPage();
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(NewsPage.NEWS_TEXT, newsPage.getNewsLabelText());
        presentationSleep();
        //
        // Steps
        AboutUsPage aboutUsPage = newsPage.gotoAboutUsPage();
        presentationSleep();
        //
        // Check
        Assertions.assertTrue(aboutUsPage.getStudyUkrainianLabelText().contains(AboutUsPage.STUDY_UKRAINIAN_PARTIAL_TEXT));
        presentationSleep();
        //
        // Steps
        UkrainianFavorPage ukrainianFavorPage = aboutUsPage.gotoUkrainianFavorPage();
        presentationSleep();
        //
        // Check
        Assertions.assertTrue(ukrainianFavorPage.getStudyUkrainianLabelText().contains(UkrainianFavorPage.STUDY_UKRAINIAN_PARTIAL_TEXT));
        presentationSleep();
    }
}
