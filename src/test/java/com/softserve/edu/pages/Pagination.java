package com.softserve.edu.pages;

import com.softserve.edu.utils.JsMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Pagination {

    // Create WebDriver instance
    private final WebDriver driver;

    // Constructor
    public Pagination(WebDriver driver) {
        this.driver = driver;
    }

    /*
     * Page Object
     */

    // nextButton
    private WebElement getNextButton() {
        // Get nextButton element
        return driver.findElement(By.xpath("//span[contains(@class,'right')]/../..//button[@class='ant-pagination-item-link']"));
    }

    public boolean isNextButtonEnabled() {
        return getNextButton().isEnabled();                                     // check if nextButton enabled
    }

    public void clickNextButton() {
        getNextButton().click();                                                // click nextButton
    }

    // firstPageButton
    private WebElement getFirstPageButton() {
        return driver.findElement(By.xpath("//li[contains(@class,'ant-pagination-item ant-pagination-item-1')]//a"));
    }

    public void clickFirstPageButton() {
        getFirstPageButton().click();                                           // click first page button
    }

    /*
     * Functional
     */

    public boolean isNextButtonPresent() {
        // Check if nextButton is displayed
        return driver.findElements(By.xpath("//span[contains(@class,'right')]/../..//button[@class='ant-pagination-item-link']")).size() != 0;
    }

    /*
     * Business Logic
     */

}
