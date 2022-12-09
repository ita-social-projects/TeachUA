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
    private static final String CLUB_NOT_FOUND = "There is no club that matches the search criteria.";
    // Selector to find the whole club container
    private static final String CLUB_COMPONENT_CSS_SELECTOR = ".ant-card.ant-card-bordered.card";
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

    public int getClubComponentsCount() {
        return getClubComponents().size();                                      // get clubComponents size
    }

    /*
     * Functional
     */

    // Check is needed club is present on the page
    public boolean isClubComponentPresent(String clubTitle) {
        boolean result = false;
        try {
            //Thread.sleep(5000);
            for(ClubComponent component : getClubComponents()) {
                // Compare provided club title with value from club components list to find needed one
                if(component.getTitleText().contains(clubTitle)) {
                    logger.info("Component with partial or the same title as " + clubTitle + " found on the page");
                    result = true;
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CLUB_NOT_FOUND);
            return false;
        }
    }

    public List<String> getClubComponentTitles() {
        List<String> clubComponentTitles = new ArrayList<>();
        for(ClubComponent component : getClubComponents()) {
            // Remove all spaces at the beginning and at the end of the title and add clubComponentTitles to the list
            clubComponentTitles.add(component.getTitleText().trim());
        }
        return clubComponentTitles;                                             // get the list with all clubComponentTitles
    }

    /*
     * Business Logic
     */

}
