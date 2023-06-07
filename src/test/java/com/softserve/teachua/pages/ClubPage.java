package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClubPage extends TopPart {
    public static final String CLUB_LABEL_TEXT = "Гуртки у місті";

    private WebElement clubLabel;
    private WebElement showMapButton;
    public ClubPage(WebDriver driver) {
        super(driver);
        initElements();
    }

    private void initElements() {
        clubLabel = driver.findElement(By.cssSelector("h2.city-name"));
        showMapButton = driver.findElement(By.cssSelector("button.show-map-button"));
    }

    // Page Object

    // clubLabel
    public WebElement getClubLabel() {
        return clubLabel;
    }

    public String getClubLabelText() {
        return getClubLabel().getText();
    }

    // showMapButton
    public WebElement getShowMapButton() {
        return showMapButton;
    }

    public String getShowMapButtonText() {
        return getShowMapButton().getText();
    }

    public void clickShowMapButton() {
        getShowMapButton().click();
    }

    // Functional

    // Business Logic
}
