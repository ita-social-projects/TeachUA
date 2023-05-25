package com.softserve.teachua.tests;

import com.softserve.teachua.pages.HomePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HomeTest extends TestRunner {

    @Test
    public void checkHome() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
        //
        presentationSleep();
        System.out.println("HomeTest checkHome() done"); // Use Logging
    }

    @Test
    public void checkSlickDotsHomeBanners() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        //for (int i = 0; i< homePage.getHomeBannerContainer().getHomeBannerComponentsCount(); i++) {
        for (int i = homePage.getHomeBannerContainer().getHomeBannerComponentsCount() - 1; i >= 0; i--) {
            homePage.chooseHomeBannerComponentByNumber(i);
            //Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
            presentationSleep();
        }
        //
        presentationSleep();
        System.out.println("HomeTest checkSlickDotsHomeBanners() done"); // Use Logging
    }

    @Test
    public void checkClickInvalidHomeBanner() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        homePage = homePage.chooseHomeBannerComponentByNumber(8);
        presentationSleep();
        //
        //takeScreenShot("check");
        takePageSource("check");

        //
//        for (int i = 0; i< homePage.getHomeBannerContainer().getHomeBannerComponentsCount(); i++) {
//            homePage.chooseHomeBannerComponentByNumber(i);
//            presentationSleep();
//        }
        //
        System.out.println("TITLES: " + homePage.getHomeBannerContainer().getHomeBannerComponentTitles());
        //
//        homePage.getHomeBannerContainer()
//                .getHomeBannerComponentByPartialTitle("Тестовий банер")
//                .clickDetailsButton();
        presentationSleep();
        //
        presentationSleep();
        System.out.println("HomeTest checkSlickDotsHomeBanners() done"); // Use Logging
    }

    //@Test
    public void checkHomeBannerDescription(String homeBannerTitle, String homeBannerDescription) {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        //for (int i = 0; i< homePage.getHomeBannerContainer().getHomeBannerComponentsCount(); i++) {
        for (int i = homePage.getHomeBannerContainer().getHomeBannerComponentsCount() - 1; i >= 0; i--) {
            homePage.chooseHomeBannerComponentByNumber(i);
            //Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
            presentationSleep();
        }
        //
        presentationSleep();
        System.out.println("HomeTest checkSlickDotsHomeBanners() done"); // Use Logging
    }
}
