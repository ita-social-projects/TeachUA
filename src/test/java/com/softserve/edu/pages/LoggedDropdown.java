package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoggedDropdown {

    // Create WebDriver instance
    protected WebDriver driver;

    private WebElement addClub;
    private WebElement addCenter;
    private WebElement myProfile;
    private WebElement exit;

    // Constructor
    public LoggedDropdown(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        addClub = driver.findElement(By.xpath("//li[contains(@class,'ant-dropdown-menu-item ant-dropdown-menu-item-only-child') and contains(@data-menu-id,'0')]"));
        addCenter = driver.findElement(By.xpath("//li[contains(@class,'ant-dropdown-menu-item ant-dropdown-menu-item-only-child') and contains(@data-menu-id,'1')]"));
        myProfile = driver.findElement(By.cssSelector("span.ant-dropdown-menu-title-content>a[href*='page']"));
        exit = driver.findElement(By.xpath("//li[contains(@class,'ant-dropdown-menu-item ant-dropdown-menu-item-only-child') and contains(@data-menu-id,'4')]"));
    }

    /*
     * Page Object
     */

    // addClub
    private WebElement getAddClub() {
        return addClub;                                                         // get addClub element
    }

    private String getAddClubText() {
        return getAddClub().getText();                                          // get addClub text
    }

    protected void clickAddClub() {
        getAddClub().click();                                                   // click addClub
    }

    // addCenter
    private WebElement getAddCenter() {
        return addCenter;                                                       // get addCenter element
    }

    private String getAddCenterText() {
        return getAddCenter().getText();                                        // get addCenter text
    }

    protected void clickAddCenter() {
        getAddCenter().click();                                                 // click addCenter
    }

    // myProfile
    private WebElement getMyProfile() {
        return myProfile;                                                       // get myProfile element
    }

    private String getMyProfileText() {
        return getMyProfile().getText();                                        // get myProfile text
    }

    protected void clickMyProfile() {
        getMyProfile().click();                                                 // click myProfile
    }

    // exit
    private WebElement getExit() {
        return exit;                                                            // get exit element
    }

    private String getExitText() {
        return getExit().getText();                                             // get exit text
    }

    protected void clickExit() {
        getExit().click();                                                      // click exit
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
