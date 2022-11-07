package com.softserve.edu.pages.common.clubs;

import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.testcases.enums.Categories;
import com.softserve.edu.pages.TopPart;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClubsPage extends TopPart {

    private WebElement clubsInCity;                                                 // clubs in city text
    private WebElement showOnMapButton;                                             // show on map button

    // Abstract classes
    private ClubsContainer clubsContainer;                                          // clubsContainer abstract class
    private AdvancedSearchPart advancedSearchPart;                                  // advancedSearchPart abstract class

    public ClubsPage(WebDriver driver) {
        super(driver);
        initElements();                                                             // initialize elements on the page
    }

    // Check if elements present on the page
    private void initElements() {
        clubsInCity = driver.findElement(By.cssSelector(".city-name-box-small-screen>h2.city-name"));
        showOnMapButton = driver.findElement(By.cssSelector("button[class*='show-map-button']>span"));
    }

    /*
     * Page Object
     */

    // clubsInCity
    private WebElement getClubsInCity() {
        return clubsInCity;                                                        // get clubsInCity element
    }

    private String getClubsInCityText() {
        return getClubsInCity().getText();                                         // get clubsInCity text
    }

    // showOnMapButton
    private WebElement getShowOnMapButton() {
        return showOnMapButton;                                                     // get showOnMapButton element
    }

    private String getShowOnMapButtonText() {
        return getShowOnMapButton().getText();                                      // get showOnMapButton text
    }

    private void clickShowOnMapButton() {
        getShowOnMapButton().click();                                               // click showOnMapButton
    }

    // clubsContainer
    private ClubsContainer getClubsContainer() {
        if (clubsContainer == null) {
            // throw RuntimeException if clubsContainer is null
            throw new RuntimeException(OPTION_NULL_MESSAGE);
        }
        return clubsContainer;
    }

    private ClubsContainer createClubsContainer() {
        clubsContainer = new ClubsContainer(driver);                                // create new object of Pagination type
        return getClubsContainer();
    }

    // advancedSearchPart
    protected AdvancedSearchPart getAdvancedSearchPart() {
        // Check if locationDropdownComponent object is created
        if(advancedSearchPart == null) {
            throw new RuntimeException(OPTION_NULL_MESSAGE);                        // throw RuntimeException
        }
        return advancedSearchPart;                                                  // return advancedSearchPart
    }

    private AdvancedSearchPart createAdvancedSearchPart() {
        // Create and initialize object of the class AdvancedSearchPart
        advancedSearchPart = new AdvancedSearchPart(driver);
        // Double-check whether advancedSearchPart object has been created
        return getAdvancedSearchPart();
    }

    /*
     * Functional
     */

    // Clink advancedSearch button to expand advance search
    @Step("Open advanced search")
    public void openAdvancedSearchPart() {
        clickAdvancedSearchButton();                                                // click advanced search button
        createAdvancedSearchPart();                                                 // create advancedSearchPart object and initialize it
    }

    // Clear dropdown content
    @Step("Clear dropdown content")
    public void clearDropdown() {
        getAdvancedSearchPart().clickClearDropdownButton();                         // click clear button inside of dropdown
    }

    // Choose club radio button to show clubs
    public void showClubs() {
        getAdvancedSearchPart().clickClubButton();                                  // click club radio button
    }

    // Choose center radio button to show centers
    public void showCenters() {
        getAdvancedSearchPart().clickCenterButton();                                // click center radio button
    }

    // Choose city (we need to provide enum location to avoid mistakes in writing city name)
    @Step("Choose city {0}")
    public void chooseCity(Locations location) {
        // Check if value is already selected in the dropdown and if so, remove it
        getAdvancedSearchPart().isClearButtonPresent(getAdvancedSearchPart().getCityDropdown());
        // Click city dropdown to open it
        getAdvancedSearchPart().clickCityDropdown();
        // Select city from dropdown by its locator and name
        getAdvancedSearchPart().selectPlace(location.toString(),
                By.xpath(getAdvancedSearchPart().LIST_CITIES_DROPDOWN_CSS_SELECTOR));
    }

    // Choose district
    @Step("Choose district {0}")
    public <T> void chooseDistrict(T district) {
        // Check if value is already selected in the dropdown and if so, remove it
        getAdvancedSearchPart().isClearButtonPresent(getAdvancedSearchPart().getDistrictDropdown());
        // Click district dropdown to open it
        getAdvancedSearchPart().clickDistrictDropdown();
        // Object type is used in parameters to accept different enum types and because it is basic class for each type
        // Select district from dropdown by its locator and name
        getAdvancedSearchPart().selectPlace(district.toString(),
                By.xpath(getAdvancedSearchPart().LIST_DISTRICTS_DROPDOWN_CSS_SELECTOR));
    }

    // Choose the nearest metro station
    @Step("Choose metro station {0}")
    public <T> void chooseMetroStation(T station) {
        // Check if value is already selected in the dropdown and if so, remove it
        getAdvancedSearchPart().isClearButtonPresent(getAdvancedSearchPart().getNearestMetroStationDropdown());
        // Click metro station dropdown to open it
        getAdvancedSearchPart().clickNearestMetroStationDropdown();
        // Generic methods don't need to be cast, and we get compilation error when do something wrong
        // Select metro station from dropdown by its locator and name
        getAdvancedSearchPart().selectPlace(station.toString(),
                By.xpath(getAdvancedSearchPart().LIST_METRO_STATION_DROPDOWN_CSS_SELECTOR));
    }

    // Set available online to search clubs that are available online
    public void searchOnlineClubs() {
        getAdvancedSearchPart().clickAvailableOnline();                             // choose online clubs option
    }

    // Choose needed categories
    public void setSearchCategories(Categories... categories) {
        getAdvancedSearchPart().chooseCategories(categories);                       // choose categories to filter
    }

    // Set child's age
    public void setChildAge(int age) {
        getAdvancedSearchPart().sendTextIntoChildAgeField(Integer.toString(age));   // set child's age
    }

    // Click center radio button
    @Step("Click center radio button")
    public void selectCenters() {
        getAdvancedSearchPart().clickCenterButton();                                // click center radio button
    }

    // Display clubs as a list
    public void displayClubsAsList() {
        getAdvancedSearchPart().clickListView();                                    // select list view
    }

    // Check if mandatory fields are displayed
    @Step("Are mandatory fields displayed?")
    public boolean areMandatoryFieldsDisplayed() {
        return getAdvancedSearchPart().areMandatoryFieldsShowed();                  // check if extra fields present
    }

    // Check if extra fields are deactivated
    @Step("Are extra fields disabled?")
    public boolean extraFieldsNotPresent() {
        return getAdvancedSearchPart().extraFieldsNotExist();                       // check if extra fields not present
    }

    @Step("Are extra fields displayed?")
    public boolean areExtraFieldsDisplayed() {
        return !getAdvancedSearchPart().extraFieldsNotExist();                      // check if extra fields present
    }

    // Sort clubs by rate
    @Step("Click sort by rate button")
    public void sortByRate() {
        getAdvancedSearchPart().clickRateSort();                                    // click sort by rate button
    }

    // Descending club sort
    @Step("Click on the '↑' icon")
    public void descendingSort() {
        getAdvancedSearchPart().clickDescendingSort();                              // click descending sort
    }

    // Ascending club sort
    public void ascendingSort() {
        getAdvancedSearchPart().clickAscendingSort();                               // click ascending sort
    }

    // Get number of clubs on all pages on Clubs page
    public int getTotalNumberOfClubs() {
        int result = 0;
        try{
            while(createPagination().isNextButtonEnabled()) {
                // Add number of clubs on the current page to the total value
                result += createClubsContainer().getClubComponentsCount();
                createPagination().clickNextButton();                               // click on next button
                presentationSleep(3);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;                                                              // get actual number of pages
    }

    // Get number of clubs on all pages on Clubs page
    public List<String> getAllClubTitles() {
        List<String> allClubTitles = new ArrayList<>();
        try{
            presentationSleep(20);
            allClubTitles.addAll(createClubsContainer().getClubComponentTitles());
            // Check if pagination is present on the page
            if(createPagination().isNextButtonPresent()) {
                while(createPagination().isNextButtonEnabled()) {
                    createPagination().clickNextButton();                           // click on next button
                    presentationSleep(3);
                    // Add all titles on the current page to the list with all titles
                    allClubTitles.addAll(createClubsContainer().getClubComponentTitles());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Sort final list
        Collections.sort(allClubTitles);
        return allClubTitles;
    }

    public int getTotalNumberOfPagesFromDatabase(String total) {
        // Number of pages needed to place all the components
        return (int)Math.ceil((Double.parseDouble(total) / (double)createClubsContainer().getClubComponentsCount()));
    }

    // Check if club is present on the page
    public boolean isClubPresentOnThePage(String title) {
        return createClubsContainer().isClubComponentPresent(title);
    }

    /*
     * Business Logic
     */

}
