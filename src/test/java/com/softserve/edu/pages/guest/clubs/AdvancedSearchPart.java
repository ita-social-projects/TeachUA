package com.softserve.edu.pages.guest.clubs;

import com.softserve.edu.data.club.Categories;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AdvancedSearchPart {

    // XPath to find needed club category
    private final String LIST_CATEGORIES_XPATH =
            "//div[@id='basic_categoriesName']//span[contains(@class,'ant-checkbox')]/following-sibling::span";
    // CSS locator to find needed city from dropdown
    private final String LIST_CITIES_DROPDOWN_CSS_SELECTOR =
            "#basic_cityName_list+div .ant-select-item.ant-select-item-option>.ant-select-item-option-content";
    // CSS locator to find needed neighborhood from dropdown
    private final String LIST_NEIGHBORHOODS_DROPDOWN_CSS_SELECTOR =
            "#basic_districtName_list+div .ant-select-item.ant-select-item-option>.ant-select-item-option-content";
    // CSS locator to find metro station city from dropdown
    private final String LIST_METRO_STATION_DROPDOWN_CSS_SELECTOR =
            "#basic_stationName_list+div .ant-select-item.ant-select-item-option>.ant-select-item-option-content";
    private final String VALUE_ATTRIBUTE = "value";                                     // value attribute

    // Create driver instance
    protected WebDriver driver;

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
    private WebElement neighborhood;                                                   // neighbour text
    private WebElement neighborhoodDropdown;                                           // neighbour dropdown

    // Metro station
    private WebElement nearestMetroStation;                                             // nearestMetroStation text
    private WebElement nearestMetroStationDropdown;                                     // nearestMetroStation dropdown

    // Remote
    private WebElement remote;                                                          // remote text
    private WebElement availableOnline;                                                 // available online checkbox

    // Categories
    private WebElement categories;                                                      // categories text
//    private WebElement sportSections;                                                   // sport section checkbox
//    private WebElement dancingChoreography;                                             // dancing choreography checkbox
//    private WebElement earlyDevelopment;                                                // early development checkbox
//    private WebElement programming;                                                     // programming checkbox
//    private WebElement musicRelated;                                                    // music related checkbox
//    private WebElement theater;                                                         // theater checkbox
//    private WebElement studyChildren;                                                   // study children checkbox
//    private WebElement basics;                                                          // basics checkbox
//    private WebElement javaBasics;                                                      // java basics checkbox
//    private WebElement personalGrowth;                                                  // personal growth checkbox
//    private WebElement childTV;                                                         // child TV checkbox
//    private WebElement developmentCenter;                                               // development center checkbox
//    private WebElement other;                                                           // other checkbox

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
        cityDropdown = driver.findElement(By.cssSelector(".ant-select-selection-item"));
        neighborhood = driver.findElement(By.xpath("//input[@id='basic_districtName']/../../../../../../../../preceding-sibling::span[1]"));
        neighborhoodDropdown = driver.findElement(By.xpath("//input[@id='basic_districtName']/../following-sibling::span"));
        nearestMetroStation = driver.findElement(By.xpath("//input[@id='basic_stationName']/../../../../../../../../preceding-sibling::span[1]"));
        nearestMetroStationDropdown = driver.findElement(By.xpath("//input[@id='basic_stationName']/../following-sibling::span"));
        remote = driver.findElement(By.xpath("//div[@id='basic_isOnline']/../../../../../preceding-sibling::span[1]"));
        availableOnline = driver.findElement(By.xpath("//div[@id='basic_isOnline']//span[contains(@class,'ant-checkbox')]/following-sibling::span"));
        categories = driver.findElement(By.xpath("//div[@id='basic_categoriesName']/../../../../../preceding-sibling::span[1]"));
//        sportSections = driver.findElement(By.cssSelector(""));
//        dancingChoreography = driver.findElement(By.cssSelector(""));
//        earlyDevelopment = driver.findElement(By.cssSelector(""));
//        programming = driver.findElement(By.cssSelector(""));
//        musicRelated = driver.findElement(By.cssSelector(""));
//        theater = driver.findElement(By.cssSelector(""));
//        studyChildren = driver.findElement(By.cssSelector(""));
//        basics = driver.findElement(By.cssSelector(""));
//        javaBasics = driver.findElement(By.cssSelector(""));
//        personalGrowth = driver.findElement(By.cssSelector(""));
//        //childTV = driver.findElement();
//        developmentCenter = driver.findElement(By.cssSelector(""));
//        other = driver.findElement(By.cssSelector(""));
        childAge = driver.findElement(By.xpath("//div[@id='basic_categoriesName']/../../../../../following-sibling::span"));
        childAgeField = driver.findElement(By.cssSelector("input.ant-input-number-input"));
        sort = driver.findElement(By.cssSelector(".control-sort-label"));
        alphabetSort = driver.findElement(By.xpath("//span[@class='anticon anticon-arrow-up control-sort-arrow']/../preceding-sibling::span[2]"));
        rateSort = driver.findElement(By.xpath("//div[@class='control-sort-arrows']/preceding-sibling::span[1]"));
        ascendingSort = driver.findElement(By.cssSelector(".anticon.anticon-arrow-down.control-sort-arrow"));
        descendingSort = driver.findElement(By.cssSelector(".anticon.anticon-arrow-up.control-sort-arrow"));
        listView = driver.findElement(By.xpath("//input[@value='LIST']/../../span[contains(@class,'ant-radio-button')]"));
        blockView = driver.findElement(By.xpath("//input[@value='BLOCK']/../../span[contains(@class,'ant-radio-button')]"));
    }

    /*
     * Page Object
     */

    // clearDropdownButton
    private WebElement getClearDropdownButton() {
        return this.clearDropdownButton;                                                // get clearDropdownButton element
    }

    protected void clickClearDropdownButton() {
        getClearDropdownButton().click();                                               // click clearDropdownButton
    }

    // advancedSearch
    private WebElement getAdvancedSearch() {
        return this.advancedSearch;                                                     // get advancedSearch element
    }

    private String getAdvancedSearchText() {
        return getAdvancedSearch().getText();                                           // get advancedSearch text
    }

    // clubCenter
    private WebElement getClubCenter() {
        return this.clubCenter;                                                         // get clubCenter element
    }

    private String getClubCenterText() {
        return getClubCenter().getText();                                               // get clubCenter text
    }

    // clubButton
    private WebElement getClubButton() {
        return this.clubButton;                                                         // get clubButton
    }

    private String getClubButtonText() {
        return getClubButton().getText();                                               // get clubButton text
    }

    protected void clickClubButton() {
        getClubButton().click();                                                        // click clubButton
    }

    // centerButton
    private WebElement getCenterButton() {
        return this.centerButton;                                                       // get centerButton
    }

    private String getCenterButtonText() {
        return getCenterButton().getText();                                             // get centerButton text
    }

    protected void clickCenterButton() {
        getCenterButton().click();                                                      // click centerButton
    }

    // city
    private WebElement getCity() {
        return this.city;                                                               // get city element
    }

    private String getCityText() {
        return getCity().getText();                                                     // get city text
    }

    // cityDropdown
    private WebElement getCityDropdown() {
        return this.cityDropdown;                                                       // get cityDropdown element
    }

    private String getCityDropdownText() {
        return getCityDropdown().getText();                                             // get cityDropdown text
    }

    protected void clickCityDropdown() {
        getCityDropdown().click();                                                      // click cityDropdown
    }

    // neighborhood
    private WebElement getNeighborhood() {
        return this.neighborhood;                                                      // get neighborhood element
    }

    private String getNeighborhoodText() {
        return getNeighborhood().getText();                                            // get neighborhood text
    }

    // neighborhoodDropdown
    private WebElement getNeighborhoodDropdown() {
        return this.neighborhoodDropdown;                                              // get neighborhoodDropdown element
    }

    private String getNeighborhoodDropdownText() {
        return getNeighborhoodDropdown().getText();                                    // get neighborhoodDropdown text
    }

    protected void clickNeighborhoodDropdown() {
        getNeighborhoodDropdown().click();                                             // click cityDropdown
    }

    // nearestMetroStation
    private WebElement getNearestMetroStation() {
        return this.nearestMetroStation;                                                // get nearestMetroStation element
    }

    private String getNearestMetroStationText() {
        return getNearestMetroStation().getText();                                      // get nearestMetroStation text
    }

    // nearestMetroStationDropdown
    private WebElement getNearestMetroStationDropdown() {
        return this.nearestMetroStationDropdown;                                        // get nearestMetroStationDropdown element
    }

    private String getNearestMetroStationDropdownText() {
        return getNearestMetroStationDropdown().getText();                              // get nearestMetroStationDropdown text
    }

    protected void clickNearestMetroStationDropdown() {
        getNearestMetroStationDropdown().click();                                       // click cityDropdown
    }

    // remote
    private WebElement getRemote() {
        return this.remote;                                                             // get remote element
    }

    private String getRemoteText() {
        return getRemote().getText();                                                   // get remote text
    }

    // availableOnline
    private WebElement getAvailableOnline() {
        return this.availableOnline;                                                    // get availableOnline element
    }

    private String getAvailableOnlineText() {
        return getAvailableOnline().getText();                                          // get availableOnline text
    }

    protected void clickAvailableOnline() {
        getAvailableOnline().click();                                                   // click availableOnline checkbox
    }

    // categories
    private WebElement getCategories() {
        return this.categories;                                                         // get categories element
    }

    private String getCategoriesText() {
        return getCategories().getText();                                               // get categories text
    }

    // childAge
    private WebElement getChildAge() {
        return this.childAge;                                                           // get childAge element
    }

    private String getChildAgeText() {
        return getChildAge().getText();                                                 // get childAge text
    }

    // childAgeField
    private WebElement getChildAgeField() {
        return this.childAgeField;                                                      // get childAgeField element
    }

    private String getChildAgeFieldValue() {
        return getChildAgeField().getAttribute(VALUE_ATTRIBUTE);                        // get childAgeField value
    }

    private void clickChildAgeField() {
        getChildAgeField().click();                                                     // click childAgeField
    }

    private void clearChildAgeField() {
        getChildAgeField().clear();                                                     // clear childAgeField
    }

    private void sendChildAgeFieldText(String age) {
        getChildAgeField().sendKeys(age);                                               // send text into childAgeField
    }

    // sort
    private WebElement getSort() {
        return this.sort;                                                               // get sort element
    }

    private String getSortText() {
        return getSort().getText();                                                     // get sort text
    }

    // alphabetSort
    private WebElement getAlphabetSort() {
        return this.alphabetSort;                                                       // get alphabetSort element
    }

    private String getAlphabetSortText() {
        return getAlphabetSort().getText();                                             // get alphabetSort text
    }

    private void clickAlphabetSort() {
        getAlphabetSort().click();                                                      // click alphabetSort
    }

    // rateSort
    private WebElement getRateSort() {
        return this.rateSort;                                                           // get rateSort element
    }

    private String getRateSortText() {
        return getRateSort().getText();                                                 // get rateSort text
    }

    protected void clickRateSort() {
        getRateSort().click();                                                          // click rateSort
    }

    // ascendingSort
    private WebElement getAscendingSort() {
        return this.ascendingSort;                                                      // get ascendingSort element
    }

    protected void clickAscendingSort() {
        getAscendingSort().click();                                                     // click ascendingSort
    }

    // descendingSort
    private WebElement getDescendingSort() {
        return this.descendingSort;                                                     // get descendingSort element
    }

    protected void clickDescendingSort() {
        getDescendingSort().click();                                                    // click descendingSort
    }

    // listView
    private WebElement getListView() {
        return this.listView;                                                           // get listView
    }

    private void clickListView() {
        getListView().click();                                                          // click listView
    }

    // blockView
    private WebElement getBlockView() {
        return this.blockView;                                                          // get blockView
    }

    private void clickBlockView() {
        getBlockView().click();                                                         // click blockView
    }

    /*
     * Functional
     */

    protected void selectCity(String location) {
        clickCityDropdown();                                                            // click cityDropdown
        // Find and save all neighborhoods from dropdown into the list
        List<WebElement> cities = driver.findElements(By.cssSelector(LIST_CITIES_DROPDOWN_CSS_SELECTOR));
        for(WebElement current : cities) {
            // Check if provided city exists in dropdown
            if(current.getText().toLowerCase().contains(location.toLowerCase())) {
                // If such city was found, click on it to select
                current.click();
                break;
            }
        }
    }

    protected void selectNeighborhood(String neighborhood) {
        clickNeighborhoodDropdown();                                                    // click neighborhoodDropdown
        // Find and save all neighborhoods from dropdown into the list
        List<WebElement> neighborhoods = driver.findElements(By.cssSelector(LIST_NEIGHBORHOODS_DROPDOWN_CSS_SELECTOR));
        for(WebElement current : neighborhoods) {
            // Check if provided neighborhood exists in dropdown
            if(current.getText().toLowerCase().contains(neighborhood.toLowerCase())) {
                // If such neighborhood was found, click on it to select
                current.click();
                break;
            }
        }
    }

    protected void selectNearestMetroStation(String station) {
        clickNearestMetroStationDropdown();                                             // click nearestMetroStation
        // Find and save all metro stations from dropdown into the list
        List<WebElement> stations = driver.findElements(By.cssSelector(LIST_METRO_STATION_DROPDOWN_CSS_SELECTOR));
        for(WebElement current : stations) {
            // Check if provided metro station exists in dropdown
            if(current.getText().toLowerCase().contains(station.toLowerCase())) {
                // If such metro station was found, click on it to select
                current.click();
                break;
            }
        }
    }

    protected void chooseCategories(Categories... categories) {
        // Find and save all categories into the list
        List<WebElement> categoriesList = driver.findElements(By.cssSelector(LIST_CATEGORIES_XPATH));
        for(WebElement current : categoriesList) {
            for(Categories category : categories) {
                // Check if provided category matches one of the checkboxes
                if(current.getText().toLowerCase().contains(category.toString().toLowerCase())) {
                    // If match has been found, click on it to choose
                    current.click();
                    break;
                }
            }
        }
    }

    protected void sendTextIntoChildAgeField(String age) {
        clickChildAgeField();                                                           // click childAgeField
        clearChildAgeField();                                                           // clear childAgeField
        sendChildAgeFieldText(age);                                                     // send text into childAgeField
    }

    /*
     * Business Logic
     */

}
