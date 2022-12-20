package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CenterComponent {

    // Element that represents the whole component
    private final WebElement centerLayout;

    // Parts of the components
    private WebElement title;                                                   // center title
    private WebElement detailsButton;                                           // center details button

    // Constructor
    public CenterComponent(WebElement centerLayout) {
        this.centerLayout = centerLayout;
        // Initialize elements on the page
        initElements();
    }

    private void initElements() {
        title = centerLayout.findElement(By.cssSelector(".center-title .center-name"));
        detailsButton = centerLayout.findElement(By.cssSelector(".ant-btn.ant-btn-default.outlined-button.details-button a"));
    }

    /*
     * Page Object
     */
    // title
    protected WebElement getTitle() {
        return this.title;                                                      // get title element
    }

    protected String getTitleText() {
        return getTitle().getText();                                            // get title text
    }

    // detailsButton
    protected WebElement getDetailsButton() {
        return this.detailsButton;                                              // get detailsButton element
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
