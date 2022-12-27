package com.softserve.edu.pages.common.clubs;

import com.softserve.edu.utils.JsMethods;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AdvancedSearchPart {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());             // logger
    private static final String VALUE_ATTRIBUTE = "value";                              // value attribute
    private static final String CLEAR_PLACE_LOCATOR = "./..//span[@class='anticon anticon-close-circle']";
    // Message that informs about selected value
    private static final String SELECT_MESSAGE = "The following value has been selected from dropdown: {}";
    // Locator to find needed city from dropdown
    protected static final String LIST_CITIES_DROPDOWN_CSS_SELECTOR =
            "//div[@id='basic_cityName_list']/following-sibling::div//div[@class='ant-select-item-option-content']";
    // Locator to find needed neighborhood from dropdown
    protected static final String LIST_DISTRICTS_DROPDOWN_CSS_SELECTOR =
            "//div[@id='basic_districtName_list']/following-sibling::div//div[@class='ant-select-item-option-content']";
    // Locator to find metro station city from dropdown
    protected static final String LIST_METRO_STATION_DROPDOWN_CSS_SELECTOR =
            "//div[@id='basic_stationName_list']/following-sibling::div//div[@class='ant-select-item-option-content']";
    protected static final int MINIMUM_AGE = 2;                                         // minimum child age
    protected static final int MAXIMUM_AGE = 18;                                        // maximum child age


    private final WebDriver driver;                                                     // create driver instance

    private WebElement advancedSearch;                                                  // advanced search text
    private WebElement clearDropdownButton;                                             // clear dropdown button

    // Club/center
    private WebElement clubCenter;                                                      // club/center text
    private WebElement clubButton;                                                      // club radio button
    private WebElement centerButton;                                                    // center radio button

    // City
    private WebElement city;                                                            // city text
    private WebElement cityDropdown;                                                    // city dropdown

    // Neighbour
    private WebElement district;                                                        // district text
    private WebElement districtDropdown;                                                // district dropdown

    // Metro station
    private WebElement nearestMetroStation;                                             // nearestMetroStation text
    private WebElement nearestMetroStationDropdown;                                     // nearestMetroStation dropdown

    // Remote
    private WebElement remote;                                                          // remote text
    private WebElement availableOnline;                                                 // available online checkbox

    // Categories
    private WebElement categories;

    // Child age
    private WebElement childAge;                                                        // child age text
    private WebElement childAgeField;                                                   // child age text field

    // Sort clubs
    private WebElement sort;                                                            // sort text
    private WebElement alphabetSort;                                                    // alphabet sort button
    private WebElement rateSort;                                                        // rate sort button
    private WebElement ascendingSort;                                                   // ascending sort button
    private WebElement descendingSort;                                                  // descending sort button

    // View
    private WebElement listView;                                                        // list view
    private WebElement blockView;                                                       // block view

    public AdvancedSearchPart(WebDriver driver) {
        this.driver = driver;
        initElements();                                                                 // initialize elements on the page
    }

    // Check if elements are present on the page
    private void initElements() {
        advancedSearch = driver.findElement(By.cssSelector(".club-list-label"));
        clearDropdownButton = driver.findElement(By.cssSelector(".anticon.anticon-close-circle"));
        clubCenter = driver.findElement(By.xpath("//div[@id='basic_isCenter']/../../../../../preceding-sibling::span"));
        clubButton = driver.findElement(By.cssSelector("#basic_isCenter>.ant-radio-wrapper.ant-radio-wrapper-in-form-item:nth-child(1) .ant-radio~span"));
        centerButton = driver.findElement(By.cssSelector("#basic_isCenter>.ant-radio-wrapper.ant-radio-wrapper-in-form-item:nth-child(2) .ant-radio~span"));
        city = driver.findElement(By.xpath("//span[@class='anticon anticon-close-circle']/../../../../../../../preceding-sibling::span[1]"));
        cityDropdown = driver.findElement(By.xpath("//input[@id='basic_cityName']/../.."));
        district = driver.findElement(By.xpath("//input[@id='basic_districtName']/../../../../../../../../preceding-sibling::span[1]"));
        districtDropdown = driver.findElement(By.xpath("//input[@id='basic_districtName']/../.."));
        nearestMetroStation = driver.findElement(By.xpath("//input[@id='basic_stationName']/../../../../../../../../preceding-sibling::span[1]"));
        nearestMetroStationDropdown = driver.findElement(By.xpath("//input[@id='basic_stationName']/../.."));
        remote = driver.findElement(By.xpath("//div[@id='basic_isOnline']/../../../../../preceding-sibling::span[1]"));
        availableOnline = driver.findElement(By.xpath("//div[@id='basic_isOnline']//span[contains(@class,'ant-checkbox')]/following-sibling::span"));
        categories = driver.findElement(By.xpath("//div[@id='basic_categoriesName']/../../../../../preceding-sibling::span[1]"));
        childAge = driver.findElement(By.xpath("//div[@id='basic_categoriesName']/../../../../../following-sibling::span"));
        childAgeField = driver.findElement(By.cssSelector("input.ant-input-number-input"));
        sort = driver.findElement(By.cssSelector(".control-sort-label"));
        alphabetSort = driver.findElement(By.xpath("//span[@class='anticon anticon-arrow-up control-sort-arrow']/../preceding-sibling::span[2]"));
        rateSort = driver.findElement(By.xpath("//div[@class='control-sort-arrows']/preceding-sibling::span[1]"));
        ascendingSort = driver.findElement(By.cssSelector(".anticon.anticon-arrow-down.control-sort-arrow"));
        descendingSort = driver.findElement(By.xpath("//span[contains(@class,'anticon anticon-arrow-up control-sort-arrow') and contains(@aria-label,'arrow-up')]"));
        listView = driver.findElement(By.xpath("//input[@value='LIST']/../../span[contains(@class,'ant-radio-button')]"));
        blockView = driver.findElement(By.xpath("//input[@value='BLOCK']/../../span[contains(@class,'ant-radio-button')]"));
    }

    /*
     * Page Object
     */

    // centerButton
    private WebElement getCenterButton() {
        return this.centerButton;                                                       // get centerButton
    }

    protected void clickCenterButton() {
        getCenterButton().click();                                                      // click centerButton
    }

    // city
    private WebElement getCity() {
        return this.city;                                                               // get city element
    }

    protected boolean isCityPresent() {
        return getCity().isDisplayed();                                                 // is city element present
    }

    // cityDropdown
    protected WebElement getCityDropdown() {
        return this.cityDropdown;                                                       // get cityDropdown element
    }

    protected String getCityDropdownText() {
        return getCityDropdown().getText();                                             // get cityDropdown text
    }

    protected void clickCityDropdown() {
        getCityDropdown().click();                                                      // click cityDropdown
    }

    // district
    private WebElement getDistrict() {
        return this.district;                                                           // get district element
    }

    protected boolean isDistrictPresent() {
        return getDistrict().isDisplayed();                                             // is city element present
    }

    // districtDropdown
    protected WebElement getDistrictDropdown() {
        return this.districtDropdown;                                                   // get districtDropdown element
    }

    protected void clickDistrictDropdown() {
        getDistrictDropdown().click();                                                  // click districtDropdown
    }

    // nearestMetroStation
    private WebElement getNearestMetroStation() {
        return this.nearestMetroStation;                                                // get nearestMetroStation element
    }

    protected boolean isNearestMetroStationPresent() {
        return getNearestMetroStation().isDisplayed();                                  // is city element present
    }

    // nearestMetroStationDropdown
    protected WebElement getNearestMetroStationDropdown() {
        return this.nearestMetroStationDropdown;                                        // get nearestMetroStationDropdown element
    }

    protected void clickNearestMetroStationDropdown() {
        getNearestMetroStationDropdown().click();                                       // click cityDropdown
    }

    // remote
    private WebElement getRemote() {
        return this.remote;                                                             // get remote element
    }

    protected boolean isRemotePresent() {
        try {
            return getRemote().isDisplayed();                                           // is remote element present
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    // availableOnline
    private WebElement getAvailableOnline() {
        return this.availableOnline;                                                    // get availableOnline element
    }

    protected void clickAvailableOnline() {
        getAvailableOnline().click();                                                   // click availableOnline
    }

    // categories
    private WebElement getCategories() {
        return this.categories;                                                         // get categories element
    }

    protected boolean isCategoriesPresent() {
        try {
            return getCategories().isDisplayed();                                       // is categories element present
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    // childAge
    private WebElement getChildAge() {
        return this.childAge;                                                           // get childAge element
    }

    protected boolean isChildAgePresent() {
        try {
            return getChildAge().isDisplayed();                                         // is child age element present
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    // childAgeField
    protected WebElement getChildAgeField() {
        return this.childAgeField;                                                      // get childAgeField element
    }

    protected Integer getChildAgeFieldValue() {
        return Integer.parseInt(getChildAgeField().getAttribute(VALUE_ATTRIBUTE));      // get childAgeField value
    }

    protected void clickChildAgeField() {
        getChildAgeField().click();                                                     // click childAgeField
    }

    protected void clearChildAgeField() {
        getChildAgeField().clear();                                                     // clear childAgeField
    }

    protected void sendChildAgeFieldText(String age) {
        getChildAgeField().sendKeys(age);                                               // send text into childAgeField
    }

    // alphabetSort
    private WebElement getAlphabetSort() {
        // Get alphabetSort element
        return driver.findElement(By.xpath("//span[@class='anticon anticon-arrow-up control-sort-arrow']/../preceding-sibling::span[2]"));
    }

    protected void clickAlphabetSort() {
        getAlphabetSort().click();                                                      // click alphabetSort
    }

    // ratingSort
    private WebElement getRatingSort() {
        // Get ratingSort element
        return driver.findElement(By.xpath("//div[@class='control-sort-arrows']/preceding-sibling::span[1]"));
    }

    protected void clickRatingSort() {
        getRatingSort().click();                                                        // click ratingSort
    }

    // descendingSort
    private WebElement getDescendingSort() {
        return driver.findElement(By.xpath("//span[contains(@class,'anticon anticon-arrow-up control-sort-arrow') and contains(@aria-label,'arrow-up')]"));
    }

    protected void clickDescendingSort() {
        getDescendingSort().click();                                                    // click descendingSort
    }

    // listView
    private WebElement getListView() {
        // Get listView
        return driver.findElement(By.xpath("//input[@value='LIST']/../../span[contains(@class,'ant-radio-button')]"));
    }

    protected void clickListView() {
        // TODO Investigate why the only possible way to click on element to change view to list is using JS or action class otherwise it throws exception
        JsMethods.clickElement(getListView());                                          // click listView element
    }

    /*
     * Functional
     */

    protected void selectPlace(String place, By locator) {
        try {
            // Find and save all places from dropdown into the list
            List<WebElement> places = driver.findElements(locator);
            // This delay is needed to select value from dropdown
            Thread.sleep(100);
            for(WebElement current : places) {
                // Check if provided place exists in dropdown
                if(current.getText().equalsIgnoreCase(place.toLowerCase())) {
                    // If such place was found, click on it to select
                    current.click();
                    logger.info(SELECT_MESSAGE, current.getText());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO Make the following method looks better
    protected void isClearButtonPresent(WebElement element) {
        try {
            if(element.findElements(By.xpath(CLEAR_PLACE_LOCATOR)).size() != 0) {
                element.findElement(By.xpath(CLEAR_PLACE_LOCATOR)).click();
            }
        } catch(NoSuchElementException e){
            e.printStackTrace();
        }
    }

    /*
     * Business Logic
     */

}
