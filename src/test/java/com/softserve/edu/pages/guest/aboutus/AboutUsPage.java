package com.softserve.edu.pages.guest.aboutus;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AboutUsPage extends TopPart {

    private WebElement initiative;                                              // page name text

    // Constructor
    public AboutUsPage(WebDriver driver) {
        super(driver);
        initElements();                                                         // initialize elements
    }

    // Check if elements are present on the page
    private void initElements() {
        initiative = driver.findElement(By.cssSelector(".city-name-box>h2.city-name"));
    }

    /*
     * Page Object
     */

    // initiative
    private WebElement getInitiative() {
        return this.initiative;                                                 // get initiative element
    }

    protected String getInitiativeText() {
        return getInitiative().getText();                                       // get initiative text
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
