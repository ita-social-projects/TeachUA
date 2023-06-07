package com.softserve.teachua.tests.service.home;

import com.softserve.teachua.data.home.BannerItem;
import com.softserve.teachua.data.home.BannerRepository;
import com.softserve.teachua.pages.ClubPage;
import com.softserve.teachua.pages.TopPart;
import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tests.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ClassUtils;

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
                //Arguments.of(BannerRepository.getLast())
                Arguments.of(BannerRepository.getSecond())
        );
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public <T extends TopPart> void checkHomeBannerTitle(BannerItem<T> bannerItem) {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        homePage = homePage.chooseHomeBannerComponentByNumber(bannerItem);
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getDefaultHomeBannerComponent() = "
//                + homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getTitleLabelText());
        Assertions.assertEquals(bannerItem.getTitle(),
                    homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getTitleLabelText());
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerTitle() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public <T extends TopPart> void checkHomeBannerDescription(BannerItem<T> bannerItem) {
         // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem);
        presentationSleep();
        //
        // Check
        Assertions.assertEquals(bannerItem.getDescription(),
                    homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getDescriptionText());
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerDescription() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public <T extends TopPart> void checkHomeBannerButtonUrl(BannerItem<T> bannerItem) {
        // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem.getNumber());
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getDetailsButtonUrlText() = "
//                + homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getDetailsButtonUrlText());
        Assertions.assertTrue(bannerItem.getUrlButton().contains(
                        homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getDetailsButtonUrlText()));
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerButtonUrl() done"); // Use Logging
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public <T extends TopPart> void checkHomeBannerPictureUrl(BannerItem<T> bannerItem) {
        //testName = String.format("checkHomeBannerPictureUrl(BannerItem %s)", bannerItem);
        //testName = "checkHomeBannerPictureUrl";
        //
        // Steps
        HomePage homePage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem);
        presentationSleep();
        //
        // Check
//        System.out.println("homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getPictureUrlText() = "
//                + homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getPictureUrlText());
        Assertions.assertTrue(bannerItem.getUrlPicture().contains(
                homePage.getHomeBannerContainer().getDefaultHomeBannerComponent().getPictureUrlText()));
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerPictureUrl() done"); // Use Logging
        //
        //isTestSuccess = true;
    }

    @ParameterizedTest(name = "{index} => bannerItem={0}")
    @MethodSource("getBannerItems")
    public void checkHomeBannerDetailsButton(BannerItem<ClubPage> bannerItem) {
        //
        // Steps
        ClubPage clubPage = loadApplication()
                .chooseHomeBannerComponentByNumber(bannerItem)
                .getHomeBannerContainer()
                .pressHomeBannerComponentDetailsButton(bannerItem);
        presentationSleep();
        //
        // Check
        Assertions.assertTrue(clubPage.getClubLabelText().contains(ClubPage.CLUB_LABEL_TEXT));
        presentationSleep();
        //
        System.out.println("HomeTest checkHomeBannerPictureUrl() done"); // Use Logging
        //
        //isTestSuccess = true;
    }

}
