package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomeBannerComponent {

    private WebElement searchDivElement;
    private WebElement searchLiElement;
    //
    private WebElement titleLabel;
    private WebElement description;
    private WebElement detailsButton;
    private String pictureUrl;

    public HomeBannerComponent(WebElement searchDivElement, WebElement searchLiElement) {
        this.searchDivElement = searchDivElement;
        this.searchLiElement = searchLiElement;
        initElements();
        //checkElements();
    }

    private void initElements() {
        // init elements
        //logo = driver.findElement(By.xpath("//div[@class='logo']/.."));
    }

    // Page Object

    // Functional

    // Business Logic
}
