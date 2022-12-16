package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ClubComponent {

    // Element that represents the whole component
    private final WebElement clubLayout;

    // Parts of the components
    private WebElement title;                                                   // club title
    private WebElement category;                                                // club category
    private WebElement rate;                                                    // club rate
    private WebElement detailsButton;                                           // club details button

    // Constructor
    public ClubComponent(WebElement clubLayout) {
        this.clubLayout = clubLayout;
        // Initialize elements on the page
        initElements();
    }

    private void initElements() {
        title = clubLayout.findElement(By.cssSelector(".title .name"));
        category = clubLayout.findElement(By.cssSelector(".ant-tag.tag .name"));
        rate = clubLayout.findElement(By.cssSelector("ul.ant-rate.ant-rate-disabled.rating"));
        detailsButton = clubLayout.findElement(By.cssSelector(".ant-btn.ant-btn-default.outlined-button.details-button a"));
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

    // category
    protected WebElement getCategory() {
        return this.category;                                                   // get category element
    }

    // rate
    protected WebElement getRate() {
        return this.rate;                                                       // get rate element
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
