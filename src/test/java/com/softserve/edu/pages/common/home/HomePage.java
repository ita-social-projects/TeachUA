package com.softserve.edu.pages.common.home;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends TopPart {

    private WebElement addClubButton;                                           // add club button
    private WebElement initiative;                                              // page name text

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        addClubButton = driver.findElement(By.cssSelector("button.ant-btn.ant-btn-default.add-club-button>span"));
        initiative = driver.findElement(By.cssSelector(".city-name-box>h2.city-name"));
    }

    /*
     * Page Object
     */

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
