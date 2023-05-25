package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class TopPart {
    public static final String TAG_ATTRIBUTE_STYLE = "style";
    protected final String TAG_ATTRIBUTE_VALUE = "value";
    protected final String TAG_ATTRIBUTE_SRC = "src";

    protected WebDriver driver;
    //
    private WebElement logo;
    private WebElement club;
    private WebElement challenge;
    private WebElement news;
    private WebElement aboutUs;
    private WebElement ukrainianFavor;

    public TopPart(WebDriver driver) {
        this.driver = driver;
        initElements();
        //checkElements();
    }

    private void initElements() {
        // init elements
        logo = driver.findElement(By.xpath("//div[@class='logo']/.."));
        club = driver.findElement(By.cssSelector("a[href*='/clubs']"));
        challenge = driver.findElement(By.cssSelector("li#challenge_ONE span.ant-menu-title-content"));
        news = driver.findElement(By.cssSelector("a[href*='/news']"));
        aboutUs = driver.findElement(By.cssSelector("a[href*='/about']"));
        ukrainianFavor = driver.findElement(By.cssSelector("a[href*='/service']"));
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

    public void clickClub() {
        getClub().click();
    }

    // challenge
    public WebElement getChallenge() {
        return challenge;
    }

    public String getChallengeText() {
        return getChallenge().getText();
    }

    public void clickChallenge() {
        getChallenge().click();
    }

    // news
    public WebElement getNews() {
        return news;
    }

    public String getNewsText() {
        return getNews().getText();
    }

    public void clickNews() {
        getNews().click();
    }

    // aboutUs
    public WebElement getAboutUs() {
        return aboutUs;
    }

    public String getAboutUsText() {
        return getAboutUs().getText();
    }

    public void clickAboutUs() {
        getAboutUs().click();
    }

    // ukrainianFavor
    public WebElement getUkrainianFavor() {
        return ukrainianFavor;
    }

    public String getUkrainianFavorText() {
        return getUkrainianFavor().getText();
    }

    public void clickUkrainianFavor() {
        getUkrainianFavor().click();
    }

    // Functional

    // Business Logic

    public HomePage gotoHomePage() {
        clickLogo();
        return new HomePage(driver);
    }

    public ClubPage gotoClubPage() {
        clickClub();
        return new ClubPage(driver);
    }

//    public ChallengePage gotoChallengePage() {
//        clickChallenge();
//        return new ChallengePage(driver);
//    }

    public NewsPage gotoNewsPage() {
        clickNews();
        return new NewsPage(driver);
    }

    public AboutUsPage gotoAboutUsPage() {
        clickLogo();
        return new AboutUsPage(driver);
    }

    public UkrainianFavorPage gotoUkrainianFavorPage() {
        clickUkrainianFavor();
        return new UkrainianFavorPage(driver);
    }

}
