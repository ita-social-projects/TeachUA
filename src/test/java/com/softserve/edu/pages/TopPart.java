package com.softserve.edu.pages;

import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.pages.common.aboutus.AboutUsPage;
import com.softserve.edu.pages.common.challenge.ChallengePage;
import com.softserve.edu.pages.common.clubs.ClubsPage;
import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.pages.common.news.NewsPage;
import com.softserve.edu.pages.common.servicesinukrainian.ServicesInUkrainianPage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TopPart {

    private static final String SCROLL_TO = "arguments[0].scrollIntoView(true);";   // scroll to element using JS
    private final Long ONE_SECOND_DELAY = 1000L;                                    // one-second delay
    // CSS locator to find each location on dropdown list of locations
    private final String LIST_LOCATIONS_CSS_SELECTOR = ".ant-dropdown-menu-vertical>.ant-dropdown-menu-item";
    protected WebDriver driver;                                                     // WebDriver instance
    // this.getClass() means that logger will be created from the name of the class where it is used
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());       // logger
    protected final String OPTION_NULL_MESSAGE = "Option is null";                  // error message
    protected final String OPTION_NOT_FOUND_MESSAGE = "Option %s not found in %s";  // option not found error message

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

    // Abstract classes
    private Pagination pagination;                                              // pagination abstract class
    private LocationDropdownComponent locationDropdownComponent;                // location dropdown abstract class
    private LinksImagesCheck linksImagesCheck;                                  // links and images check abstract class

    // Constructor
    public TopPart(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements are present on the page
    private void initElements() {
        logo = driver.findElement(By.cssSelector(".left-side-menu>a"));
        clubs = driver.findElement(By.xpath("//span[contains(@class,'anticon-apartment')]/parent::a"));
        challenges = driver.findElement(By.cssSelector("#challenge_ONE>div"));
        news = driver.findElement(By.xpath("//li[@id='challenge_ONE']//following-sibling::li[contains(@data-menu-id,'news')]/span"));
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

    // Open first challenge
    private WebElement getFirstChallenge() {
        // Get firstChallenge element
        return driver.findElement(By.xpath("//a[contains(@href,'424')]"));
    }

    private void clickFirstChallenge() {
        getFirstChallenge().click();                                            // click first challenge
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

    public void clickEnterButton() {
        getSearchTopField().sendKeys(Keys.ENTER);                               // press enter
    }

    // searchTopButton
    private WebElement getSearchTopButton() {
        return this.searchTopButton;                                            // get searchTopButton element
    }

    private void clickSearchTopButton() {
        getSearchTopButton().click();                                           // click searchTopButton
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

    // locationDropdownComponent
    protected LocationDropdownComponent getLocationDropdownComponent() {
        // Check if locationDropdownComponent object is created
        if(locationDropdownComponent == null) {
            throw new RuntimeException(OPTION_NULL_MESSAGE);                    // throw RuntimeException
        }
        return locationDropdownComponent;                                       // return locationDropdownComponent
    }

    private LocationDropdownComponent createLocationDropdownComponent(By searchLocator) {
        // Create and initialize object of the class LocationDropdownComponent
        locationDropdownComponent = new LocationDropdownComponent(driver, searchLocator);
        return getLocationDropdownComponent();                                  // return locationDropdownComponent
    }

    private void clickLocationDropdownComponentByPartialName(String optionName) {
        // Check if needed option exists in the list
        if(!getLocationDropdownComponent().isDropdownOptionByPartialNameExist(optionName)) {
            // Throw special exception if element does not exist
            throw new RuntimeException(String.format(OPTION_NOT_FOUND_MESSAGE, optionName,
                    getLocationDropdownComponent().getListOptionsText().toString()));
        }
        // Click on option with set partial name
        getLocationDropdownComponent().clickDropdownOptionByPartialName(optionName);
        // Assign null to object to know that such element does not exist anymore
        locationDropdownComponent = null;
    }

    protected void closeLocationDropdownComponent() {
        // Click on search top field to close LocationDropdownComponent
        clickSearchTopField();
        // Assign null to object to know that such element does not exist anymore
        locationDropdownComponent = null;
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

    // location
    private void openLocationDropdownComponent() {
        // clickSearchTopField is needed because it's unknown if location dropdown already opened or not
        clickSearchTopField();                                                  // click search top field
        clickLocation();                                                        // click location
        // If we clicked on location component, we need to create it and provide css selector to find needed location
        createLocationDropdownComponent(By.cssSelector(LIST_LOCATIONS_CSS_SELECTOR));
    }

    // optionName with type Locations we need to avoid mistakes in writing location name
    protected void clickLocationByPartialName(Locations optionName) {           // void because we can be on any page
        openLocationDropdownComponent();                                        // open location dropdown component
        clickLocationDropdownComponentByPartialName(optionName.toString());     // click location by its partial name
    }

    // scrolltoElement
    protected void scrolltoElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(SCROLL_TO, element);        // scroll to element using JS
    }

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
        clickSearchTopButton();                                                 // click search top button to search club
        logger.info("Button clicked");
        logger.debug("Enter and searching club by its title started");
    }

    /*
     * Business Logic
     */

    @Step("Go to Home page")
    public HomePage gotoHomePage() {
        clickLogo();                                                            // click logo
        logger.info("Home page opened");                                        // information about current page
        return new HomePage(driver);
    }

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
        logger.info("Challenge page opened");                              // information about current page
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
