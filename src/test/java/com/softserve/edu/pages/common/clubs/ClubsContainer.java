package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClubsContainer {

    // Message if there is no such component found
    private final String CLUB_NOT_FOUND = "There is no product that matches the search criteria.";
    // Selector for club container
    private final String CLUB_COMPONENT_CSS_SELECTOR = ".ant-card.ant-card-bordered.card";
    // Logger
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WebDriver driver;

    // List to store all club components
    private List<ClubComponent> clubComponents;

    // Constructor
    public ClubsContainer(WebDriver driver) {
        this.driver = driver;
        // Initialize elements on the page
        initElements();
    }


    private void initElements() {
        // Create object of the class ArrayList
        clubComponents = new ArrayList<>();
        // Find elements that match criteria and add them into clubComponents list
        for(WebElement element : driver.findElements(By.cssSelector(CLUB_COMPONENT_CSS_SELECTOR))) {
            clubComponents.add(new ClubComponent(element));
        }
    }

    /*
     * Page Object
     */
    // clubComponents
    private List<ClubComponent> getClubComponents() {
        // Check if clubComponent list contains some values
        if(clubComponents == null) {
            throw new RuntimeException(CLUB_NOT_FOUND);
        }
        return clubComponents;                                                  // get clubComponent list
    }

    public String getClubComponentTitle(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getTitleText();               // get clubComponent title
    }

    public void clickClubComponentTitle(String clubTitle) {
        getClubComponentByTitle(clubTitle).clickTitle();                        // click clubComponent title
    }

    public String getClubComponentCategory(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getCategoryText();            // get clubComponent category
    }

    public void clickClubComponentCategory(String clubTitle) {
        getClubComponentByTitle(clubTitle).clickCategory();                     // click clubComponent category
    }

    public String getClubComponentPartialDescription(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getPartialDescriptionText();  // get clubComponent partialDescription
    }

    public void clickClubComponentPartialDescription(String clubTitle) {
        getClubComponentByTitle(clubTitle).clickPartialDescription();           // click clubComponent partialDescription
    }

    public int getClubComponentRate(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getRateValue();               // get clubComponent rate
    }

    public int getClubComponentsCount() {
        return getClubComponents().size();                                      // get clubComponents size
    }

    public String getClubComponentLocation(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getLocationText();            // get clubComponent location
    }

    public void clickClubComponentLocation(String clubTitle) {
        getClubComponentByTitle(clubTitle).clickLocation();                     // click clubComponent location
    }

    public String getClubComponentDetailsButtonText(String clubTitle) {
        return getClubComponentByTitle(clubTitle).getDetailsButtonText();       // get clubComponent detailsButton text
    }

    public void clickClubComponentDetailsButton(String clubTitle) {
        getClubComponentByTitle(clubTitle).clickDetailsButton();                // click clubComponent detailsButton
    }

    /*
     * Functional
     */

    // Get club component by title
    public ClubComponent getClubComponentByTitle(String clubTitle) {
        ClubComponent result = null;
        for(ClubComponent component : getClubComponents()) {
            // Compare provided club title with value from club components list to find needed one
            if(component.getTitleText().equalsIgnoreCase(clubTitle)) {
                result = component;
                break;
            }
            if(!isClubComponentPresent(clubTitle)) {
                // Throw exception once club with needed title has not been found
                throw new RuntimeException("Club " + clubTitle + " has not been found");
            }
        }
        return result;
    }

    // Check is needed club is present on the page
    public boolean isClubComponentPresent(String clubTitle) {
        boolean result = false;
        for(ClubComponent component : getClubComponents()) {
            // Compare provided club title with value from club components list to find needed one
            if(component.getTitleText().contains(clubTitle)) {
                logger.info("Component with title " + clubTitle + " found on the page");
                result = true;
                break;
            }
        }
        return result;
    }

    public List<String> getClubComponentTitles() {
        List<String> clubComponentTitles = new ArrayList<>();
        for(ClubComponent component : getClubComponents()) {
            clubComponentTitles.add(component.getTitleText());                  // add clubComponentTitles to the list
        }
        return clubComponentTitles;                                             // get the list with all clubComponentTitles
    }

    /*
     * Business Logic
     */

}
