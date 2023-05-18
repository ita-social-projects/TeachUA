package com.softserve.teachua.tests;

import com.softserve.teachua.pages.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SmokeTest extends TestRunner {

    @Test
    public void checkPages() {
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
        Assertions.assertEquals(ClubPage.CLUB_KYIV_TEXT, clubPage.getClubKyivLabelText());
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
