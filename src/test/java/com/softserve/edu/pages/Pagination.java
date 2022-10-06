package com.softserve.edu.pages;

import com.softserve.edu.pages.common.clubs.ClubsContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Pagination {

    // Create WebDriver instance
    private final WebDriver driver;
    private final Long ONE_SECOND_DELAY = 1000L;                                // one-second delay

    private WebElement nextButton;                                              // next button element

    // Constructor
    public Pagination(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        nextButton = driver.findElement(By.xpath("//span[contains(@class,'right')]/../..//button[@class='ant-pagination-item-link']"));
    }

    /*
     * Page Object
     */

    // nextButton
    private WebElement getNextButton() {
        return this.nextButton;                                                 // get nextButton element
    }

    public boolean isNextButtonEnabled() {
        return getNextButton().isEnabled();                                     // check if nextButton enabled
    }

    public void clickNextButton() {
        getNextButton().click();                                                // click nextButton
    }

    /*
     * Functional
     */

    // Only for presentation
    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);                           // set seconds to sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Count number of pages
    public int countNumberOfPages() {
        int counter = 1;
        try {
            // Traversing through the table until the last button and adding names to the list defined above
            while(isNextButtonEnabled()) {
                clickNextButton();                                              // click next button
                counter++;                                                      // increase page count
                presentationSleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    /*
     * Business Logic
     */

}
