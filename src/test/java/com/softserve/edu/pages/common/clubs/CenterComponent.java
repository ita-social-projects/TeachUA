package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CenterComponent {

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

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
