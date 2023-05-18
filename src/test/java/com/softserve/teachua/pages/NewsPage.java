package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewsPage extends TopPart {
    public static final String NEWS_TEXT = "Новини";

    private WebElement newsLabel;

    public NewsPage(WebDriver driver) {
        super(driver);
        initElements();
    }

    private void initElements() {
        newsLabel = driver.findElement(By.cssSelector("h2.city-name"));
    }

    // Page Object

    // newsLabel
    public WebElement getNewsLabel() {
        return newsLabel;
    }

    public String getNewsLabelText() {
        return getNewsLabel().getText();
    }

    // Functional

    // Business Logic
}
