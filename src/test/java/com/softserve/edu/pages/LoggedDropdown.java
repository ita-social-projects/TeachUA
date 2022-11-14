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

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
