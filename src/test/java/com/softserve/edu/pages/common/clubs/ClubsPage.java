package com.softserve.edu.pages.common.clubs;

import com.softserve.edu.pages.Pagination;
import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClubsPage extends TopPart {

    private WebElement clubsInCity;
    private WebElement showOnMapButton;

    // Abstract class
    private ClubsContainer clubsContainer;

    public ClubsPage(WebDriver driver) {
        super(driver);
        initElements();
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

    /*
     * Functional
     */

    private ClubsContainer createClubsContainer() {
        clubsContainer = new ClubsContainer(driver);                                // create new object of Pagination type
        return getClubsContainer();
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
