package com.softserve.teachua.tests.service.home;

import com.softserve.teachua.data.home.BannerItem;
import com.softserve.teachua.data.home.BannerRepository;
import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tests.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class HomeBannerFunctionTest extends TestRunner {

    private static Stream<Arguments> getSlickDotsValidNumbers() {
//        return Stream.of(
//                Arguments.of(1),
//                Arguments.of(3),
//                Arguments.of(8)
//        );
        /*
        List<Arguments> listArguments = new ArrayList<>();
        for (int i = 0; i < BannerRepository.getBannerBox().getCount(); i++) {
            listArguments.add(Arguments.of(i));
        }
        return listArguments.stream();
        */
        return Stream.iterate(Arguments.of(0), x -> Arguments.of((int)x.get()[0] + 1))
                .limit(BannerRepository.getBannerBox().getCount());
    }

    // TODO move to smoke
    @ParameterizedTest(name = "{index} => dotsNumber={0}")
    @MethodSource("getSlickDotsValidNumbers")
    public void checkSlickDotsHomeBanners(int dotsNumber) {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        //
        // Step
        homePage.chooseHomeBannerComponentByNumber(dotsNumber);
        presentationSleep();
        // Check
        Assertions.assertTrue(!homePage.getHomeBannerContainer().getHomeBannerComponentTitles().isEmpty());
        presentationSleep();
        //
        presentationSleep();
        System.out.println("HomeTest checkSlickDotsHomeBanners() done"); // Use Logging
    }

    private static Stream<Arguments> getBannerItems() {
        return Stream.of(
                Arguments.of(BannerRepository.getLast()),
                Arguments.of(BannerRepository.getSecond())
        );
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public void checkHomeBannerTitle(BannerItem bannerItem) {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        homePage = homePage.chooseHomeBannerComponentByNumber(bannerItem.getNumber());
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getHomeBannerComponents().get(0) = "
//                + homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getTitleLabelText());
        Assertions.assertEquals(bannerItem.getTitle(),
                    homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getTitleLabelText());
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerTitle() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public void checkHomeBannerDescription(BannerItem bannerItem) {
         // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem.getNumber());
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(bannerItem.getDescription(),
                    homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getDescriptionText());
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerDescription() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public void checkHomeBannerButtonUrl(BannerItem bannerItem) {
        // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem.getNumber());
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getDetailsButtonUrlText() = "
//                + homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getDetailsButtonUrlText());
        Assertions.assertTrue(bannerItem.getUrlButton().contains(
                        homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getDetailsButtonUrlText()));
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerButtonUrl() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public void checkHomeBannerPictureUrl(BannerItem bannerItem) {
        //testName = String.format("checkHomeBannerPictureUrl(BannerItem %s)", bannerItem);
        testName = "checkHomeBannerPictureUrl";
        //
        // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem.getNumber());
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getPictureUrlText() = "
//                + homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getPictureUrlText());
        Assertions.assertTrue(bannerItem.getUrlPicture().contains(
                homePage.getHomeBannerContainer().getHomeBannerComponents().get(0).getPictureUrlText()));
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerPictureUrl() done"); // Use Logging
        //
        isTestSuccess = true;
    }

}
