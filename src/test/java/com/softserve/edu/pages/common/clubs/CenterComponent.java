package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CenterComponent {

//    // CSS selector to find location element
//    private final String LOCATION_CSS_SELECTOR = ".oneAddress";

    // Element that represents the whole component
    private WebElement centerLayout;

    // Parts of the components
    private WebElement title;                                                   // center title
    private WebElement partialDescription;                                      // center partial description
    private WebElement location;                                                // center location
    private WebElement detailsButton;                                           // center details button

    // Constructor
    public CenterComponent(WebElement centerLayout) {
        this.centerLayout = centerLayout;
        // Initialize elements on the page
        initElements();
    }

    private void initElements() {
        title = centerLayout.findElement(By.cssSelector(".center-title .center-name"));
        partialDescription = centerLayout.findElement(By.cssSelector(".center-description-in-block"));
        detailsButton = centerLayout.findElement(By.cssSelector(".ant-btn.ant-btn-default.outlined-button.details-button a"));
    }

    /*
     * Page Object
     */
    // title
    private WebElement getTitle() {
        return this.title;                                                      // get title element
    }

    protected String getTitleText() {
        return getTitle().getText();                                            // get title text
    }

//    protected void clickTitle() {
//        getTitle().click();                                                     // click title
//    }
//
//    // partialDescription
//    private WebElement getPartialDescription() {
//        return this.partialDescription;                                         // get partialDescription element
//    }
//
//    protected String getPartialDescriptionText() {
//        return getPartialDescription().getText();                               // get partialDescription text
//    }
//
//    protected void clickPartialDescription() {
//        getPartialDescription().click();                                        // click partialDescription
//    }
//
//    // location
//    private WebElement getLocation() {
//        // Presence of location element should be checked here to prevent test failure when there is no location
//        // selected in advanced search
//        location = centerLayout.findElement(By.cssSelector(LOCATION_CSS_SELECTOR));
//        return this.location;                                                   // get location element
//    }
//
//    protected String getLocationText() {
//        return getLocation().getText();                                         // get location text
//    }
//
//    protected void clickLocation() {
//        getLocation().click();                                                  // click location
//    }
//
//    // details
//    private WebElement getDetailsButton() {
//        return this.detailsButton;                                              // get details element
//    }
//
//    protected String getDetailsButtonText() {
//        return getDetailsButton().getText();                                    // get details text
//    }
//
//    protected void clickDetailsButton() {
//        getDetailsButton().click();                                             // click details
//    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
