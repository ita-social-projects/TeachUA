package com.softserve.edu.pages;

import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class TopPart {

    protected WebDriver driver;                                                 // WebDriver instance
    // this.getClass() means that logger will be created from the name of the class where it is used
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SCROLL_TO = "arguments[0].scrollIntoView(true);"; // scroll to element using JS
    private final Long ONE_SECOND_DELAY = 1000L;                                // one-second delay
    protected final String OPTION_NULL_MESSAGE = "Pagination is null";          // error message

    /*
     * Header elements
      */
    private WebElement logo;
    private WebElement clubs;
    private WebElement challenges;
    private WebElement news;
    private WebElement aboutUs;
    private WebElement servicesInUkrainian;
    private WebElement searchTopField;
    private WebElement searchTopButton;
    private WebElement extendedSearchButton;
    private WebElement location;
    private WebElement myProfile;

    /*
     * Footer elements
     */
    // Social media
    private WebElement teachUALogo;
    private WebElement watchword;
    private WebElement facebook;
    private WebElement youtube;
    private WebElement instagram;
    private WebElement designedBy;

    // Partners
    private WebElement outPartners;
    private WebElement softServe;
    private WebElement languageUnites;
    private WebElement edEra;
    private WebElement isLanguage;
    private WebElement countryFm;
    private WebElement ucf;
    private WebElement freedom;

    // Donate
    private WebElement howToHelpProject;
    private WebElement reason;
    private WebElement helpProjectButton;

    // Abstraction
    private Pagination pagination;

    // Constructor
    public TopPart(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        logo = driver.findElement(By.cssSelector(".left-side-menu>a"));
        clubs = driver.findElement(By.xpath("//span[contains(@class,'anticon-apartment')]/parent::a"));
        challenges = driver.findElement(By.cssSelector("#challenge_ONE>div"));
        news = driver.findElement(By.xpath("//li[@id='challenge_ONE']//following-sibling::li[contains(@data-menu-id,'news')]/span"));
        aboutUs = driver.findElement(By.xpath("//span[@aria-label='container']/parent::a"));
        servicesInUkrainian = driver.findElement(By.xpath("//span[@aria-label='container']/../../..//following-sibling::li/span"));
        searchTopField = driver.findElement(By.cssSelector("input.ant-select-selection-search-input"));
        searchTopButton = driver.findElement(By.cssSelector("span.anticon.anticon-search.advanced-icon"));
        extendedSearchButton = driver.findElement(By.cssSelector("span.anticon.anticon-control.advanced-icon"));
        location = driver.findElement(By.cssSelector("span.anticon.anticon-environment.icon"));
        myProfile = driver.findElement(By.cssSelector("span.ant-avatar.ant-avatar-lg.ant-avatar-circle"));
        teachUALogo = driver.findElement(By.cssSelector(".footer-logo"));
        watchword = driver.findElement(By.cssSelector(".description>.text"));
        facebook = driver.findElement(By.xpath("//span[@aria-label='facebook']"));
        youtube = driver.findElement(By.xpath("//span[@aria-label='youtube']"));
        instagram = driver.findElement(By.xpath("//span[@aria-label='instagram']"));
        designedBy = driver.findElement(By.cssSelector(".qubstudio"));
        outPartners = driver.findElement(By.cssSelector(".footer-partners .article"));
        softServe = driver.findElement(By.cssSelector(".logo_soft-serve"));
        languageUnites = driver.findElement(By.xpath("//img[contains(@alt,'Mova')]"));
        edEra = driver.findElement(By.xpath("//img[contains(@alt,'EDERA')]"));
        isLanguage = driver.findElement(By.xpath("//img[contains(@alt,'mova')]"));
        countryFm = driver.findElement(By.xpath("//img[contains(@alt,'FM')]"));
        ucf = driver.findElement(By.xpath("//img[contains(@alt,'ucf')]"));
        freedom = driver.findElement(By.xpath("//img[contains(@alt,'prostir')]"));
        howToHelpProject = driver.findElement(By.cssSelector(".footer-donate>.article"));
        reason = driver.findElement(By.cssSelector(".desc>span"));
        helpProjectButton = driver.findElement(By.cssSelector("button.ant-btn.ant-btn-default.flooded-button.donate-button"));
    }

    /*
     * Page Object
     */

    // logo
    private WebElement getLogo() {
        return this.logo;                                                       // get logo element
    }

    private void clickLogo() {
        getLogo().click();                                                      // click logo
    }

    // clubs
    private WebElement getClubs() {
        return this.clubs;                                                      // get clubs element
    }

    private String getClubsText() {
        return getClubs().getText();                                            // get clubs text
    }

    private void clickClubs() {
        getClubs().click();                                                     // click clubs
    }

    // challenges
    private WebElement getChallenges() {
        return this.challenges;                                                 // get challenges element
    }

    private String getChallengesText() {
        return getChallenges().getText();                                       // get challenges text
    }

    private void clickChallenges() {
        getChallenges().click();                                                // click challenges
    }

    // news
    private WebElement getNews() {
        return this.news;                                                       // get news element
    }

    private String getNewsText() {
        return getNews().getText();                                             // get news text
    }

    private void clickNews() {
        getNews().click();                                                      // click news
    }

    // aboutUs
    private WebElement getAboutUs() {
        return this.aboutUs;                                                    // get aboutUs element
    }

    private String getAboutUsText() {
        return getAboutUs().getText();                                          // get aboutUs text
    }

    private void clickAboutUs() {
        getAboutUs().click();                                                   // click aboutUs
    }

    // servicesInUkrainian
    private WebElement getServicesInUkrainian() {
        return this.servicesInUkrainian;                                        // get servicesInUkrainian element
    }

    private String getServicesInUkrainianText() {
        return getServicesInUkrainian().getText();                              // get servicesInUkrainian text
    }

    private void clickServicesInUkrainian() {
        getServicesInUkrainian().click();                                       // click servicesInUkrainian
    }

    // searchTopField
    private WebElement getSearchTopField() {
        return this.searchTopField;                                             // get searchTopField element
    }

    private String getSearchTopFieldText() {
        return getSearchTopField().getText();                                   // get searchTopField text
    }

    private void clickSearchTopField() {
        getSearchTopField().click();                                            // click searchTopField
    }

    private void clearSearchTopField() {
        getSearchTopField().clear();                                            // clear searchTopField
    }

    private void sendSearchTopFieldText(String text) {
        getSearchTopField().sendKeys(text);                                     // send text into searchTopField
    }

    // searchTopButton
    private WebElement getSearchTopButton() {
        return this.searchTopButton;                                            // get searchTopButton element
    }

    private void clickSearchTopButton() {
        getSearchTopButton().click();                                           // click searchTopButton
    }

    // extendedSearchButton
    private WebElement getExtendedSearchButton() {
        return this.extendedSearchButton;                                       // get extendedSearchButton element
    }

    private void clickExtendedSearchButton() {
        getExtendedSearchButton().click();                                      // click extendedSearchButton
    }

    // location
    private WebElement getLocation() {
        return this.location;                                                   // get location element
    }

    private String getLocationText() {
        return getLocation().getText();                                         // get location text
    }

    private void clickLocation() {
        getLocation().click();                                                  // click location
    }

    // myProfile
    private WebElement getMyProfile() {
        return this.myProfile;                                                  // get myProfile element
    }

    private void clickMyProfile() {
        getMyProfile().click();                                                 // click myProfile
    }

    // teachUALogo
    private WebElement getTeachUALogo() {
        return this.teachUALogo;                                                // get teachUALogo element
    }

    private void clickTeachUALogo() {
        getTeachUALogo().click();                                               // click teachUALogo
    }

    // watchword
    private WebElement getWatchword() {
        return this.watchword;                                                  // get location element
    }

    private String getWatchwordText() {
        return getWatchword().getText();                                        // get location text
    }

    // facebook
    private WebElement getFacebook() {
        return this.facebook;                                                   // get facebook element
    }

    private void clickFacebook() {
        getFacebook().click();                                                  // click facebook
    }

    // youtube
    private WebElement getYoutube() {
        return this.youtube;                                                    // get youtube element
    }

    private void clickYoutube() {
        getYoutube().click();                                                   // click youtube
    }

    // instagram
    private WebElement getInstagram() {
        return this.instagram;                                                  // get instagram element
    }

    private void clickInstagram() {
        getInstagram().click();                                                 // click instagram
    }

    // designedBy
    private WebElement getDesignedBy() {
        return this.designedBy;                                                 // get designedBy element
    }

    private String getDesignedByText() {
        return getDesignedBy().getText();                                       // get designedBy text
    }

    // outPartners
    private WebElement getOutPartners() {
        return this.outPartners;                                                // get outPartners element
    }

    private String getOutPartnersText() {
        return getOutPartners().getText();                                      // get outPartners text
    }

    // softServe
    private WebElement getSoftServe() {
        return this.softServe;                                                  // get softServe element
    }

    private void clickSoftServe() {
        getSoftServe().click();                                                 // click softServe
    }

    // languageUnites
    private WebElement getLanguageUnites() {
        return this.languageUnites;                                             // get languageUnites element
    }

    private void clickLanguageUnites() {
        getLanguageUnites().click();                                            // click languageUnites
    }

    // edEra
    private WebElement getEdEra() {
        return this.edEra;                                                      // get edEra element
    }

    private void clickEdEra() {
        getEdEra().click();                                                     // click edEra
    }

    // isLanguage
    private WebElement getIsLanguage() {
        return this.isLanguage;                                                 // get isLanguage element
    }

    private void clickIsLanguage() {
        getIsLanguage().click();                                                // click isLanguage
    }

    // countryFm
    private WebElement getCountryFm() {
        return this.countryFm;                                                  // get countryFm element
    }

    private void clickCountryFm() {
        getCountryFm().click();                                                 // click countryFm
    }

    // ucf
    private WebElement getUcf() {
        return this.ucf;                                                        // get ucf element
    }

    private void clickUcf() {
        getUcf().click();                                                       // click ucf
    }

    // freedom
    private WebElement getFreedom() {
        return this.freedom;                                                    // get freedom element
    }

    private void clickFreedom() {
        getFreedom().click();                                                   // click freedom
    }

    // howToHelpProject
    private WebElement getHowToHelpProject() {
        return this.howToHelpProject;                                           // get howToHelpProject element
    }

    private String getHowToHelpProjectText() {
        return getHowToHelpProject().getText();                                 // get howToHelpProject text
    }

    // reason
    private WebElement getReason() {
        return this.reason;                                                     // get reason element
    }

    private String getReasonText() {
        return getReason().getText();                                           // get reason text
    }

    // helpProjectButton
    private WebElement getHelpProjectButton() {
        return this.helpProjectButton;                                          // get helpProjectButton element
    }

    private String getHelpProjectButtonText() {
        return getHelpProjectButton().getText();                                // get helpProjectButton text
    }

    private void clickHelpProjectButton() {
        getHelpProjectButton().click();                                         // click helpProjectButton
    }

    // pagination
    private Pagination getPagination() {
        if (pagination == null) {
            // TODO Develop Custom Exception
            throw new RuntimeException(OPTION_NULL_MESSAGE);                    // throw RuntimeException
        }
        return pagination;
    }

    private Pagination createPagination() {
        pagination = new Pagination(driver);                                    // create new object of Pagination type
        return getPagination();
    }

    /*
     * Functional
     */

    // Overload
    private void presentationSleep() {
        presentationSleep(1);
    }

    // Overload
    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void scrolltoElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(SCROLL_TO, element);        // scroll to element using JS
    }

    public int getActualNumberOfPages() {
        return createPagination().countNumberOfPages();                         // get actual number of pages
    }

    public int getExpectedNumberOfPages() {
        return createPagination().getTotalPageNumberValue();                    // get actual number of pages
    }

    /*
     * Business Logic
     */

    public HomePage gotoHomePage() {
        clickLogo();                                                            // click logo
        return new HomePage(driver);
    }

    public ClubsPage gotoClubsPage() {
        clickClubs();                                                           // click Clubs page
        return new ClubsPage(driver);
    }

}
