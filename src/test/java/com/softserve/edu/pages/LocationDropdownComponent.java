package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class LocationDropdownComponent {

    // Message when provided location does not exist
    protected final String OPTION_NAME_NOT_FOUND = "Option Name not Found";

    // Create driver instance
    protected WebDriver driver;

    // List where all found option will be saved
    private List<WebElement> listOptions;

    // Constructor (searchLocator is needed to know how to search elements because they should be unique)
    public LocationDropdownComponent(WebDriver driver, By searchLocator) {
        //  Assign the value of the method parameter (“driver”) to the class parameter (“this.driver”)
        this.driver = driver;
        initElements(searchLocator);                                                // initialize elements
    }

    private void initElements(By searchLocator) {
        // Dropdown already opened, and we search for the needed option name
        listOptions = driver.findElements(searchLocator);
    }

    /*
     * Page Object
     */

    private List<WebElement> getListOptions() {
        return this.listOptions;                                                    // get listOptions
    }

    /*
     * Functional
     */

    protected WebElement getDropdownOptionByPartialName(String optionName) {
        WebElement result = null;
        for(WebElement current : getListOptions()) {
            if(current.getText().toLowerCase().contains(optionName.toLowerCase())) {
                // If optionName that was provided matches one of the options, then result will equal to found optionName
                result = current;
                break;
            }
        }
        if(result == null) {
            // If there is no such option in dropdown list, then throw exception with corresponding message
            throw new RuntimeException(OPTION_NAME_NOT_FOUND);
        }
        return result;                                                              // return result
    }

    protected List<String> getListOptionsText() {
        List<String> result = new ArrayList<>();                                    // create new list to store values
        for(WebElement current : getListOptions()) {
            result.add(current.getText());                                          // add option text to the list
        }
        return result;                                                              // return final list
    }

    protected boolean isDropdownOptionByPartialNameExist(String optionName) {
        boolean isFound = false;                                                    // create boolean var to store result
        for(String current : getListOptionsText()) {
            // Check if option with needed name exists in dropdown
            if(current.toLowerCase().contains(optionName.toLowerCase())) {
                // Make variable is Found true if needed option found
                isFound = true;
                break;
            }
        }
        return isFound;                                                             // return final result
    }

    protected void clickDropdownOptionByPartialName(String optionName) {
        getDropdownOptionByPartialName(optionName).click();                         // click on needed option
    }

    /*
     * Business Logic
     */

}
