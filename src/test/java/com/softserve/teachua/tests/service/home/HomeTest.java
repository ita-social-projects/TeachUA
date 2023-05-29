package com.softserve.teachua.tests.service.home;

import com.softserve.teachua.data.home.BannerRepository;
import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tests.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HomeTest extends TestRunner {

    @Test
    public void checkSmokeHome() {
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

    // TODO move to smoke
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
        //homePage = homePage.chooseHomeBannerComponentByNumber(BannerRepository.getFirst().getNumber());
        homePage = homePage.chooseHomeBannerComponentByNumber(BannerRepository.getSecond().getNumber());
        presentationSleep();
        //
        //takeScreenShot("check");
        //takePageSource("check");

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
        Assertions.assertEquals(BannerRepository.getSecond().getTitle(),
                homePage.getHomeBannerContainer()
                        .getHomeBannerComponentTitles()
                        .get(BannerRepository.getSecond().getNumber()));
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
