package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GuestDropdown {

    // Create WebDriver instance
    protected WebDriver driver;

    private WebElement register;
    private WebElement login;

    // Constructor
    public GuestDropdown(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        register = driver.findElement(By.xpath("//li[contains(@class,'ant-dropdown-menu-item ant-dropdown-menu-item-only-child') and contains(@data-menu-id,'0')]"));
        login = driver.findElement(By.xpath("//li[contains(@class,'ant-dropdown-menu-item ant-dropdown-menu-item-only-child') and contains(@data-menu-id,'1')]"));
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
