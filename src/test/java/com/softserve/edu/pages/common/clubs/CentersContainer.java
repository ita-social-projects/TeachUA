package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CentersContainer {

    // Message if there is no such component found
    private final String CENTER_NOT_FOUND = "There is no center that matches the search criteria.";
    // Selector to find the whole center container
    private final String CENTER_COMPONENT_CSS_SELECTOR = ".ant-card.ant-card-bordered.card";
    // Logger
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WebDriver driver;                                                     // create driver instance

    // List to store all center components
    private List<CenterComponent> centerComponents;

    // Constructor
    public CentersContainer(WebDriver driver) {
        this.driver = driver;
        // Initialize elements on the page
        initElements();
    }


    private void initElements() {
        // Create object of the class ArrayList
        centerComponents = new ArrayList<>();
        // Find elements that match criteria and add them into centerComponents list
        for(WebElement element : driver.findElements(By.cssSelector(CENTER_COMPONENT_CSS_SELECTOR))) {
            centerComponents.add(new CenterComponent(element));
        }
    }

    /*
     * Page Object
     */
    // centerComponents
    private List<CenterComponent> getCenterComponents() {
        // Check if centerComponent list contains some values
        if(centerComponents == null) {
            throw new RuntimeException(CENTER_NOT_FOUND);
        }
        return centerComponents;                                                    // get centerComponent list
    }

    public String getCenterComponentTitle(String centerTitle) {
        return getCenterComponentByTitle(centerTitle).getTitleText();               // get centerComponent title
    }

    public void clickCenterComponentTitle(String centerTitle) {
        getCenterComponentByTitle(centerTitle).clickTitle();                        // click centerComponent title
    }

    public String getCenterComponentPartialDescription(String centerTitle) {
        return getCenterComponentByTitle(centerTitle).getPartialDescriptionText();  // get centerComponent partialDescription
    }

    public void clickCenterComponentPartialDescription(String centerTitle) {
        getCenterComponentByTitle(centerTitle).clickPartialDescription();           // click centerComponent partialDescription
    }

    public int getCenterComponentsCount() {
        return getCenterComponents().size();                                        // get centerComponents size
    }

    public String getCenterComponentLocation(String centerTitle) {
        return getCenterComponentByTitle(centerTitle).getLocationText();            // get centerComponent location
    }

    public void clickCenterComponentLocation(String centerTitle) {
        getCenterComponentByTitle(centerTitle).clickLocation();                     // click centerComponent location
    }

    public String getCenterComponentDetailsButtonText(String centerTitle) {
        return getCenterComponentByTitle(centerTitle).getDetailsButtonText();       // get centerComponent detailsButton text
    }

    public void clickCenterComponentDetailsButton(String centerTitle) {
        getCenterComponentByTitle(centerTitle).clickDetailsButton();                // click centerComponent detailsButton
    }

    /*
     * Functional
     */

    // Get centerComponent by title
    public CenterComponent getCenterComponentByTitle(String centerTitle) {
        CenterComponent result = null;
        for(CenterComponent component : getCenterComponents()) {
            // Compare provided center title with value from center components list to find needed one
            if(component.getTitleText().equalsIgnoreCase(centerTitle)) {
                result = component;
                break;
            }
            if(!isCenterComponentPresent(centerTitle)) {
                // Throw exception once center with needed title has not been found
                throw new RuntimeException("Center " + centerTitle + " has not been found");
            }
        }
        return result;
    }

    // Check is needed center is present on the page
    public boolean isCenterComponentPresent(String centerTitle) {
        boolean result = false;
        try {
            Thread.sleep(7000);
            for(CenterComponent component : getCenterComponents()) {
                // Compare provided center title with value from center components list to find needed one
                if(component.getTitleText().contains(centerTitle)) {
                    logger.info("Component with partial or the same title as " + centerTitle + " found on the page");
                    result = true;
                    break;
                }
            }
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(CENTER_NOT_FOUND);
            return false;
        }
    }

    public List<String> getCenterComponentTitles() {
        List<String> centerComponentTitles = new ArrayList<>();
        for(CenterComponent component : getCenterComponents()) {
            // Remove all spaces at the beginning and at the end of the title and add centerComponentTitles to the list
            centerComponentTitles.add(component.getTitleText().trim());
        }
        return centerComponentTitles;                                               // get the list with all centerComponentTitles
    }

    /*
     * Business Logic
     */

}
