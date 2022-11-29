package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ClubComponent {

    // Element that represents the whole component
    private WebElement clubLayout;

    // Parts of the components
    private WebElement title;                                                   // club title
    private WebElement category;                                                // club category
    private WebElement partialDescription;                                      // club partial description
    private WebElement rate;                                                    // club rate
    private WebElement location;                                                // club location
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
        partialDescription = clubLayout.findElement(By.cssSelector(".ant-card-body .description"));
        rate = clubLayout.findElement(By.cssSelector("ul.ant-rate.ant-rate-disabled.rating"));
        detailsButton = clubLayout.findElement(By.cssSelector(".ant-btn.ant-btn-default.outlined-button.details-button a"));
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
