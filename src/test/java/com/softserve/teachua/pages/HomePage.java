package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public HomePage chooseHomeBannerComponentByNumber(int number) {
        getHomeBannerContainer().clickSlickDotsBottomsByNumber(number);
        return this;
    }

    public HomePage chooseHomeBannerComponentByTitle(String homeBannerTitle) {
        getHomeBannerContainer().clickSlickDotsBottomsByNumber(getHomeBannerContainer()
                .getHomeBannerComponentByTitlePosition(homeBannerTitle));
        return this;
    }

}
