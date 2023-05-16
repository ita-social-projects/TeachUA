package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class TopPart {

    protected WebDriver driver;
    //
    private WebElement logo;
    private WebElement club;

    public TopPart(WebDriver driver) {
        this.driver = driver;
        initElements();
        //checkElements();
    }

    private void initElements() {
        // init elements
        logo = driver.findElement(By.xpath("//div[@class='logo']/.."));
        club = driver.findElement(By.cssSelector("a[href*='/clubs']"));
    }

    // Page Object

    // logo
    public WebElement getLogo() {
        return logo;
    }

    public void clickLogo() {
        getLogo().click();
    }

    // club
    public WebElement getClub() {
        //return driver.findElement(By.cssSelector("a[href*='/clubs']"));
        return club;
    }

    public String getClubText() {
        return getClub().getText();
    }

    public void clickvClub() {
        getClub().click();
    }

    // Functional

    // Business Logic

    public HomePage gotoHomePage() {
        clickLogo();
        return new HomePage(driver);
    }
}
