package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AboutUsPage extends TopPart {
    public static final String STUDY_UKRAINIAN_PARTIAL_TEXT = "Навчай українською";

    private WebElement studyUkrainianLabel;

    public AboutUsPage(WebDriver driver) {
        super(driver);
        initElements();
    }

    private void initElements() {
        studyUkrainianLabel = driver.findElement(By.cssSelector("h2.city-name"));
    }

    // Page Object

    // studyUkrainianLabel
    public WebElement getStudyUkrainianLabel() {
        return studyUkrainianLabel;
    }

    public String getStudyUkrainianLabelText() {
        return getStudyUkrainianLabel().getText();
    }

    // Functional

    // Business Logic
}
