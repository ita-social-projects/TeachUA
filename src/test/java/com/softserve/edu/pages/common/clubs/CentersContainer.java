package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CentersContainer {

    // Message if there is no such component found
    private static final String CENTER_NOT_FOUND = "There is no center that matches the search criteria.";
    // Selector to find the whole center container
    private static final String CENTER_COMPONENT_CSS_SELECTOR = ".ant-card.ant-card-bordered.card";

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
    protected List<CenterComponent> getCenterComponents() {
        // Check if centerComponent list contains some values
        if(centerComponents == null) {
            throw new RuntimeException(CENTER_NOT_FOUND);
        }
        return centerComponents;                                                    // get centerComponent list
    }

    /*
     * Functional
     */

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
