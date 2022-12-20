package com.softserve.edu.pages.common.clubs;

import com.softserve.edu.testcases.enums.Categories;
import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.pages.TopPart;
import com.softserve.edu.utils.JsMethods;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ClubsPage extends TopPart {

    private WebElement clubsInCity;                                                 // clubs in city text
    private WebElement showOnMapButton;                                             // show on map button

    // Aggregation
    private ClubsContainer clubsContainer;                                          // clubsContainer abstract class
    private AdvancedSearchPart advancedSearchPart;                                  // advancedSearchPart abstract class
    private CentersContainer centersContainer;                                      // centersContainer abstract class

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

    // clubsContainer
    private ClubsContainer getClubsContainer() {
        if (clubsContainer == null) {
            // throw RuntimeException if clubsContainer is null
            throw new RuntimeException(OPTION_NULL_MESSAGE);
        }
        return clubsContainer;
    }

    private ClubsContainer createClubsContainer() {
        clubsContainer = new ClubsContainer(driver);                                // create new object of ClubsContainer type
        return getClubsContainer();
    }

    // centersContainer
    private CentersContainer getCentersContainer() {
        if (centersContainer == null) {
            // throw RuntimeException if centersContainer is null
            throw new RuntimeException(OPTION_NULL_MESSAGE);
        }
        return centersContainer;
    }

    private CentersContainer createCentersContainer() {
        centersContainer = new CentersContainer(driver);                            // create new object of CentersContainer type
        return getCentersContainer();
    }

    // advancedSearchPart
    private AdvancedSearchPart getAdvancedSearchPart() {
        // Check if locationDropdownComponent object is created
        if (advancedSearchPart == null) {
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

    // Choose city (we need to provide enum location to avoid mistakes in writing city name)
    @Step("Choose city {0}")
    public void chooseCity(Locations location) {
        // Check if value is already selected in the dropdown and if so, remove it
        getAdvancedSearchPart().isClearButtonPresent(getAdvancedSearchPart().getCityDropdown());
        // Click city dropdown to open it
        getAdvancedSearchPart().clickCityDropdown();
        // Select city from dropdown by its locator and name
        getAdvancedSearchPart().selectPlace(location.toString(), By.xpath(AdvancedSearchPart.LIST_CITIES_DROPDOWN_CSS_SELECTOR));
        //getAdvancedSearchPart().clickAlphabetSort();                                // click alphabetic sort
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
        getAdvancedSearchPart().selectPlace(district.toString(), By.xpath(AdvancedSearchPart.LIST_DISTRICTS_DROPDOWN_CSS_SELECTOR));
        //getAdvancedSearchPart().clickAlphabetSort();                                // click alphabetic sort
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
        getAdvancedSearchPart().selectPlace(station.toString(), By.xpath(AdvancedSearchPart.LIST_METRO_STATION_DROPDOWN_CSS_SELECTOR));
        //getAdvancedSearchPart().clickAlphabetSort();                                // click alphabetic sort
    }

    // Set available online to search clubs that are available online
    @Step("Select Remote option")
    public void checkRemoteOption() {
        getAdvancedSearchPart().clickAvailableOnline();                             // choose online clubs option
    }

    // Choose needed categories
    @Step("Select categories")
    public void selectCategories(Categories... categories) {
        getAdvancedSearchPart().chooseCategories(categories);                       // choose categories to filter
    }

    // Click center radio button
    @Step("Click center radio button")
    public void selectCenters() {
        getAdvancedSearchPart().clickCenterButton();                                // click center radio button
        logger.info("Centers button has been clicked");
    }

    // Display clubs as a list
    @Step("Display clubs as list")
    public void displayComponentsAsList() {
        getAdvancedSearchPart().clickListView();                                    // select list view
    }

    // Scroll to child age field
    public void scrolltoChildAgeField() {
        JsMethods.scrollToElement(getAdvancedSearchPart().getChildAgeField());      // scroll to element
    }

    // Get child age value
    private String getChildAgeValue() {
        return getAdvancedSearchPart().getChildAgeFieldValue();                     // get child age
    }

    // Check if child age is correct
    public boolean isChildAgeCorrect() {
        try {
            // Check if entering child age field is empty
            if (getChildAgeValue().isEmpty()) {
                return true;
            }
            return Integer.parseInt(getChildAgeValue()) >= AdvancedSearchPart.MINIMUM_AGE
                    && Integer.parseInt(getChildAgeValue()) <= AdvancedSearchPart.MAXIMUM_AGE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Set child age
    @Step("Child age is set to {0}")
    public void setChildAge(String age) {
        logger.info("Child age is set to {}", age);
        getAdvancedSearchPart().sendTextIntoChildAgeField(age);                     // send text into child age field
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

    // Sort clubs by rating
    @Step("Click sort by rating button")
    public void sortByRating() {
        getAdvancedSearchPart().clickRatingSort();                                  // click sort by rating button
        logger.info("Items have been sorted by rating");
    }

    // Sort clubs by title
    @Step("Click alphabetic sort button")
    public void alphabeticSort() {
        presentationSleep(3);
        getAdvancedSearchPart().clickAlphabetSort();                                // click alphabetic sort button
        logger.info("Items have been sorted alphabetically");
    }

    // Descending club sort
    @Step("Click on the '↑' icon")
    public void descendingSort() {
        createPagination().clickFirstPageButton();                                  // go to first page in pagination
        getAdvancedSearchPart().clickDescendingSort();                              // click descending sort
        logger.info("Items have been sorted in descending order");
    }

    // Get default city name
    public String getCityName() {
        logger.info("Default city is {}", getAdvancedSearchPart().getCityDropdownText());
        return getAdvancedSearchPart().getCityDropdownText();                       // get city name
    }

    // Check if all clubs fields are present after switching to list view
    public boolean areAllClubFieldsPresentInListView() {
        List<String> componentFields = new ArrayList<>();
        for (ClubComponent component : createClubsContainer().getClubComponents()) {
            if (!(component.getTitle().isDisplayed() && component.getCategory().isDisplayed()
                    && component.getRate().isDisplayed() && component.getDetailsButton().isDisplayed())) {
                componentFields.add(component.getTitleText().trim());
                logger.info("Club component with title {} has missing field(-s)", component.getTitleText());
            }
        }
        return componentFields.isEmpty();
    }

    // Check if all centers fields are present after switching to list view
    public boolean areAllCenterFieldsPresentInListView() {
        List<String> componentFields = new ArrayList<>();
        for (CenterComponent component : createCentersContainer().getCenterComponents()) {
            if (!(component.getTitle().isDisplayed() && component.getDetailsButton().isDisplayed())) {
                componentFields.add(component.getTitleText().trim());
                logger.info("Center component with title {} has missing field", component.getTitleText());
            }
        }
        return componentFields.isEmpty();
    }

    // Get number of clubs on all pages on Clubs page
    public int getTotalNumberOfClubs() {
        int result = 0;
        while (createPagination().isNextButtonEnabled()) {
            // Add number of clubs on the current page to the total value
            result += createClubsContainer().getClubComponentsCount();
            getPagination().clickNextButton();                               // click on next button
            presentationSleep(3);
        }
        return result;                                                              // get actual number of pages
    }

    // TODO Combine the following two methods into one
    // Get number of clubs on all pages on Clubs page
    public List<String> getAllClubTitles() {
        presentationSleep(4);
        List<String> componentFields = new ArrayList<>(createClubsContainer().getClubComponentTitles());
        // Check if pagination is present on the page
        if (createPagination().isNextButtonPresent()) {
            while (getPagination().isNextButtonEnabled()) {
                getPagination().clickNextButton();                              // click on next button
                // TODO Implement Strategy for searching elements
                presentationSleep(4);
                // Add all titles on the current page to the list with all titles
                componentFields.addAll(createClubsContainer().getClubComponentTitles());
            }
        }
        logger.info("Received club titles from UI: {}", componentFields);
        return componentFields;
    }

    // Get number of centers on all pages on Clubs page
    public List<String> getAllCenterTitles() {
        presentationSleep(5);
        List<String> componentFields = new ArrayList<>(createCentersContainer().getCenterComponentTitles());
        // Check if pagination is present on the page
        if (createPagination().isNextButtonPresent()) {
            while (getPagination().isNextButtonEnabled()) {
                getPagination().clickNextButton();                           // click on next button
                presentationSleep(3);
                // Add all titles on the current page to the list with all titles
                componentFields.addAll(createCentersContainer().getCenterComponentTitles());
            }
        }
        logger.info("Received centers titles from UI: {}", componentFields);
        return componentFields;
    }

    public int getTotalNumberOfPagesFromDatabase(String total) {
        // Number of pages needed to place all the components
        return (int) Math.ceil((Double.parseDouble(total) / (double) createClubsContainer().getClubComponentsCount()));
    }

    // Check if club is present on the page
    public boolean isClubPresentOnThePage(String title) {
        presentationSleep(1);
        boolean result = false;
        try {
            for(ClubComponent component : createClubsContainer().getClubComponents()) {
                // Compare provided club title with value from club components list to find needed one
                if(component.getTitleText().contains(title)) {
                    logger.info("Component with partial or the same title as " + title + " found on the page");
                    result = true;
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ClubsContainer.CLUB_NOT_FOUND);
            return false;
        }
    }

    /*
     * Business Logic
     */

}
