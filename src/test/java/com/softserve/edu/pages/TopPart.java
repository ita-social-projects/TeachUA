package com.softserve.edu.pages;

import com.softserve.edu.pages.common.aboutus.AboutUsPage;
import com.softserve.edu.pages.common.challenge.ChallengePage;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.news.NewsPage;
import com.softserve.edu.pages.common.servicesinukrainian.ServicesInUkrainianPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TopPart {

    private static final Long ONE_SECOND_DELAY = 1000L;                                    // one-second delay
    protected WebDriver driver;                                                     // WebDriver instance
    // this.getClass() means that logger will be created from the name of the class where it is used
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());       // logger
    protected final String OPTION_NULL_MESSAGE = "Option is null";                  // error message

    /*
     * Header elements
      */
    private WebElement logo;                                                    // logo element
    private WebElement clubs;                                                   // clubs page
    private WebElement challenges;                                              // challenges page
    private WebElement news;                                                    // news page
    private WebElement aboutUs;                                                 // about us page
    private WebElement servicesInUkrainian;                                     // services in ukrainian page
    private WebElement searchTopField;                                          // search top field element
    private WebElement searchTopButton;                                         // search top button
    private WebElement advancedSearchButton;                                    // advanced search button
    private WebElement location;                                                // location element
    private WebElement myProfile;                                               // my profile element

    /*
     * Footer elements
     */
    // Social media
    private WebElement teachUALogo;                                             // teach UA logo
    private WebElement watchword;                                               // watchword element
    private WebElement facebook;                                                // facebook logo
    private WebElement youtube;                                                 // youtube logo
    private WebElement instagram;                                               // instagram logo
    private WebElement designedBy;                                              // designed by element

    // Partners
    private WebElement outPartners;                                             // our partners element
    private WebElement softServe;                                               // softserve logo
    private WebElement languageUnites;                                          // language unites logo
    private WebElement edEra;                                                   // ed era logo
    private WebElement isLanguage;                                              // is-language logo
    private WebElement countryFm;                                               // country fm logo
    private WebElement ucf;                                                     // ucf logo
    private WebElement freedom;                                                 // freedom logo

    // Donate
    private WebElement howToHelpProject;                                        // how to help project element
    private WebElement reason;                                                  // reason element
    private WebElement helpProjectButton;                                       // help project button

    // Aggregation
    private Pagination pagination;                                              // pagination class
    private LinksImagesCheck linksImagesCheck;                                  // links and images check class

    // Constructor
    public TopPart(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements are present on the page
    private void initElements() {
        logo = driver.findElement(By.cssSelector(".left-side-menu>a"));
        clubs = driver.findElement(By.xpath("//span[contains(@class,'anticon-apartment')]/parent::a"));
        challenges = driver.findElement(By.xpath("//li[@id='challenge_ONE']"));
        news = driver.findElement(By.xpath("//li[@id='challenge_ONE']//following-sibling::li[contains(@data-menu-id,'news')]"));
        aboutUs = driver.findElement(By.xpath("//span[@aria-label='container']/parent::a"));
        servicesInUkrainian = driver.findElement(By.xpath("//span[@aria-label='container']/../../..//following-sibling::li/span"));
        searchTopField = driver.findElement(By.cssSelector("input.ant-select-selection-search-input"));
        searchTopButton = driver.findElement(By.cssSelector("span.anticon.anticon-search.advanced-icon"));
        advancedSearchButton = driver.findElement(By.cssSelector("span.anticon.anticon-control.advanced-icon"));
        location = driver.findElement(By.cssSelector(".ant-dropdown-trigger.city"));
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

    // clubs
    private WebElement getClubs() {
        return this.clubs;                                                      // get clubs element
    }

    private void clickClubs() {
        getClubs().click();                                                     // click clubs
    }

    // challenges
    private WebElement getChallenges() {
        return this.challenges;                                                 // get challenges element
    }

    private void clickChallenges() {
        getChallenges().click();                                                // click challenges
    }

    // Open first challenge
    private WebElement getFirstChallenge() {
        // TODO Refactor this method (temporary solution)
        // Get firstChallenge element
        return driver.findElement(By.xpath("//li[contains(@data-menu-id,'424')]/span"));
    }

    private void clickFirstChallenge() {
        getFirstChallenge().click();                                            // click first challenge
    }

    // news
    private WebElement getNews() {
        return this.news;                                                       // get news element
    }

    private void clickNews() {
        getNews().click();                                                      // click news
    }

    // aboutUs
    private WebElement getAboutUs() {
        return this.aboutUs;                                                    // get aboutUs element
    }

    private void clickAboutUs() {
        getAboutUs().click();                                                   // click aboutUs
    }

    // servicesInUkrainian
    private WebElement getServicesInUkrainian() {
        return this.servicesInUkrainian;                                        // get servicesInUkrainian element
    }

    private void clickServicesInUkrainian() {
        getServicesInUkrainian().click();                                       // click servicesInUkrainian
    }

    // searchTopField
    private WebElement getSearchTopField() {
        return this.searchTopField;                                             // get searchTopField element
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

    public void clickEnterButton() {
        getSearchTopField().sendKeys(Keys.ENTER);                               // press enter
    }

    // advancedSearchButton
    private WebElement getAdvancedSearchButton() {
        return this.advancedSearchButton;                                       // get advancedSearchButton element
    }

    protected void clickAdvancedSearchButton() {
        getAdvancedSearchButton().click();                                      // click advancedSearchButton
    }

    // location
    private WebElement getLocation() {
        return this.location;                                                   // get location element
    }

    @Step("Get location")
    public String getLocationText() {
        return getLocation().getText();                                         // get location text
    }

    // pagination
    protected Pagination getPagination() {
        // Check if pagination object is created
        if (pagination == null) {
            throw new RuntimeException(OPTION_NULL_MESSAGE);                    // throw RuntimeException
        }
        return pagination;
    }

    protected Pagination createPagination() {
        pagination = new Pagination(driver);                                    // create new object of Pagination type
        return getPagination();
    }

    // linksCheck
    private LinksImagesCheck getLinksImagesCheck() {
        // Check if linksCheck object is created
        if (linksImagesCheck == null) {
            throw new RuntimeException(OPTION_NULL_MESSAGE);                    // throw RuntimeException
        }
        return linksImagesCheck;
    }

    protected LinksImagesCheck createLinksImagesCheck() {
        linksImagesCheck = new LinksImagesCheck(driver);                        // create new object of LinksImagesCheck type
        return getLinksImagesCheck();
    }

    /*
     * Functional
     */

    // pagination
    public int getActualNumberOfPages() {
        return createPagination().countNumberOfPages();                         // get actual number of pages
    }

    // linksImagesCheck
    public void verifyPageLinksImages() {
        createLinksImagesCheck();                                               // create linksImagesCheck object and initialize it
        getLinksImagesCheck().checkLinks();                                     // check if links are good
        getLinksImagesCheck().checkImages();                                    // check if images are good
    }

    // Only for presentation
    protected void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);                           // set seconds to sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Enter club title")
    public void sendTextIntoInputSearchField(String text) {
        logger.debug("Enter club title started");
        clickSearchTopField();                                                  // click search top field
        clearSearchTopField();                                                  // clear search top field
        sendSearchTopFieldText(text);                                           // send text into search top field
        logger.debug("Enter club title started");
    }

    @Step("Press ENTER button")
    public void searchClub(String title) {
        logger.debug("Enter and searching club by its title started");
        sendTextIntoInputSearchField(title);                                    // send text into search top field
        logger.debug("Enter and searching club by its title started");
    }

    /*
     * Business Logic
     */

    @Step("Go to Clubs page")
    public ClubsPage gotoClubsPage() {
        clickClubs();                                                           // click Clubs page
        logger.info("Clubs page opened");                                       // information about current page
        return new ClubsPage(driver);
    }

    // Temporary solution, just to be able to perform smoke tests for now
    @Step("Go to Challenge page")
    public ChallengePage openChallengePage() {
        clickChallenges();                                                      // click Challenge tab
        logger.info("Challenges dropdown opened");                              // information about current page
        verifyPageLinksImages();                                                // verify links on opened dropdown
        clickFirstChallenge();                                                  // click first challenge
        logger.info("Challenge page opened");                                   // information about current page
        return new ChallengePage(driver);
    }

    @Step("Go to News page")
    public NewsPage gotoNewsPage() {
        clickNews();                                                            // click News page
        logger.info("News page opened");                                        // information about current page
        return new NewsPage(driver);
    }

    @Step("Go to About us page")
    public AboutUsPage gotoAboutUsPage() {
        clickAboutUs();                                                         // click About us page
        logger.info("About us page opened");                                    // information about current page
        return new AboutUsPage(driver);
    }

    @Step("Go to Services in ukrainian page")
    public ServicesInUkrainianPage gotoServicesInUkrainianPage() {
        clickServicesInUkrainian();                                             // click Services in ukrainian page
        logger.info("Services in ukrainian page opened");                       // information about current page
        return new ServicesInUkrainianPage(driver);
    }

}
