package com.softserve.teachua.pages.home;

import com.softserve.teachua.data.home.BannerItem;
import com.softserve.teachua.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;

public class HomePage extends TopPart {

    public static final String DETAILS_BUTTON_TEXT = "Детальніше";
    public static final String CLUB_DIRECTION_TEXT = "Оберіть напрям гуртків";

    private WebElement clubDirectionLabel;
    private HomeBannerContainer homeBannerContainer;

    public HomePage(WebDriver driver) {
        super(driver);
        initElements();
    }

    private void initElements() {
        //detailsButton = driver.findElement(By.cssSelector("a[href*='/clubs'] button.details-button"));
        clubDirectionLabel = driver.findElement(By.cssSelector("div.categories-header h2.label"));
        homeBannerContainer = new HomeBannerContainer(driver);
    }

    // Page Object

    // clubDirectionLabel
    public WebElement getClubDirectionLabel() {
        return clubDirectionLabel;
    }

    public String getClubDirectionLabelText() {
        return getClubDirectionLabel().getText();
    }

    // homeBannerContainer
    public HomeBannerContainer getHomeBannerContainer() {
        return homeBannerContainer;
    }

    // Functional

    // Business Logic

    public <T> HomePage chooseHomeBannerComponentByNumber(BannerItem<T> bannerItem) {
        getHomeBannerContainer().clickSlickDotsBottomsByNumber(bannerItem.getNumber());
        return new HomePage(driver);
    }

    public HomePage chooseHomeBannerComponentByNumber(int dotsNumber) {
        getHomeBannerContainer().clickSlickDotsBottomsByNumber(dotsNumber);
        return new HomePage(driver);
    }

    /*
    public <T extends TopPart> T chooseHomeBannerButton(BannerItem bannerItem) {
        getHomeBannerContainer().clickHomeBannerComponentDetailsButton();
        //
        Class<?> clazz = Class.forName("com.softserve.teachua.pages.ClubPage");
        Constructor<?> ctor = clazz.getConstructor(WebDriver.class);
        Object object = ctor.newInstance(new Object[] { driver });
        //
        return (T) object;
    }
    */

    /*
    // Move to Test
    public HomePage chooseHomeBannerComponentByTitle(String homeBannerTitle) {
        getHomeBannerContainer().clickSlickDotsBottomsByNumber(getHomeBannerContainer()
                .getHomeBannerComponentByTitlePosition(homeBannerTitle));
        return this;
    }
    */

}
