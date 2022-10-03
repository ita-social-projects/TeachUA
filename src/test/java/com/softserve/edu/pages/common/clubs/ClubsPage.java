package com.softserve.edu.pages.common.clubs;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClubsPage extends TopPart {

    private WebElement clubsInCity;
    private WebElement showOnMapButton;

    public ClubsPage(WebDriver driver) {
        super(driver);
        initElements();
    }

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

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
