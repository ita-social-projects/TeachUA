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

    // register
    private WebElement getRegister() {
        return register;                                                        // get register element
    }

    private String getRegisterText() {
        return getRegister().getText();                                         // get register text
    }

    protected void clickRegister() {
        getRegister().click();                                                  // click register
    }

    // login
    private WebElement getLogin() {
        return login;                                                           // get login element
    }

    private String getLoginText() {
        return getLogin().getText();                                            // get login text
    }

    protected void clickLogin() {
        getLogin().click();                                                     // click login
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
