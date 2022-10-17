package com.softserve.edu.pages.common.home;

import com.softserve.edu.data.Locations;
import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends TopPart {

    private WebElement addClubButton;
    private WebElement initiative;

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

    // addClubButton
    private WebElement getAddClubButton() {
        return addClubButton;                                                   // get addClubButton element
    }

    private String getAddClubButtonText() {
        return getAddClubButton().getText();                                    // get addClubButton text
    }

    private void clickAddClubButton() {
        getAddClubButton().click();                                             // click addClubButton
    }

    // initiative
    private WebElement getInitiative() {
        return initiative;                                                      // get initiative element
    }

    private String getInitiativeText() {
        return getInitiative().getText();                                       // get initiative text
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

    public HomePage chooseLocation(Locations location) {
        clickLocationByPartialName(location);                                   // click on provided location
        return new HomePage(driver);                                            // return HomePage
    }

}
