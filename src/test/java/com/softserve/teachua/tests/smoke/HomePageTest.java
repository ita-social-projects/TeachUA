package com.softserve.teachua.tests.smoke;

import com.softserve.teachua.data.home.BannerRepository;
import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tests.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class HomePageTest extends TestRunner  {
    // check_Home_Web_Elements

    @Test
    public void checkClubDirectionLabel() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
        //
        presentationSleep();
        System.out.println("HomeTest checkClubDirectionLabel() done"); // Use Logging
    }

    private static Stream<Arguments> getSlickDotsNumbers() {
        return Stream.of(
                Arguments.of(BannerRepository.getBannerBox().getCount())
        );
    }

    // TODO move to smoke
    @ParameterizedTest(name = "{index} => dotsNumber={0}")
    @MethodSource("getSlickDotsNumbers")
    public void checkSlickDotsHomeBanners(int dotsNumber) {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Check
        //for (int i = 0; i< homePage.getHomeBannerContainer().getHomeBannerComponentsCount(); i++) {
        for (int i = 0; i < dotsNumber; i++) {
            homePage.chooseHomeBannerComponentByNumber(i);
            Assertions.assertTrue(!homePage.getHomeBannerContainer().getHomeBannerComponentTitles().isEmpty());
            presentationSleep();
        }
        //
        presentationSleep();
        System.out.println("HomeTest checkSlickDotsHomeBanners() done"); // Use Logging
    }

}
