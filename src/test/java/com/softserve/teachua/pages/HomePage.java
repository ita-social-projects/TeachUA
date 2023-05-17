package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends TopPart {

    public static final String DETAILS_BUTTON_TEXT = "Детальніше";
    public static final String CLUB_DIRECTION_TEXT = "Оберіть напрям гуртків";

    private WebElement detailsButton;
    private WebElement clubDirectionLabel;

    public HomePage(WebDriver driver) {
        super(driver);
        initElements();
    }

    private void initElements() {
        detailsButton = driver.findElement(By.cssSelector("a[href*='/clubs'] button.details-button"));
        clubDirectionLabel = driver.findElement(By.cssSelector("div.categories-header h2.label"));
    }

    // Page Object

    // detailsButton
    public WebElement getDetailsButton() {
        return detailsButton;
    }

    public String getDetailsButtonText() {
        return getDetailsButton().findElement(By.cssSelector("span")).getText();
    }

    public void clickDetailsButton() {
        getDetailsButton().click();
    }

    // clubDirectionLabel

    public WebElement getClubDirectionLabel() {
        return clubDirectionLabel;
    }

    public String getClubDirectionLabelText() {
        return getClubDirectionLabel().getText();
    }


    // Functional

    // Business Logic

}
