package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Pagination {

    // Create WebDriver instance
    private final WebDriver driver;
    private final String DISABLED_ATTRIBUTE = "disabled";                       // displayed attribute
    private final Long ONE_SECOND_DELAY = 1000L;                                // one-second delay

    private List<WebElement> elementNames;                                      // title of each component
    private WebElement nextButton;                                              // next button element
    private WebElement totalPageNumber;                                         // total number of pages

    // Constructor
    public Pagination(WebDriver driver) {
        this.driver = driver;
        initElements();                                                         // initialize elements
    }

    // Check if elements present on the page
    private void initElements() {
        elementNames = driver.findElements(By.cssSelector("div.content-clubs-list.content-clubs-block div.name"));
        nextButton = driver.findElement(By.xpath("//span[contains(@class,'right')]/../..//button[@class='ant-pagination-item-link']"));
        totalPageNumber = driver.findElement(By.xpath("//li[@title='Next Page']/preceding-sibling::li[1]//a"));
    }

    /*
     * Page Object
     */

    // elementNames
    private List<WebElement> getElementNames() {
        return this.elementNames;                                               // get elementNames element
    }

    private int getElementNamesSize() {
        return getElementNames().size();                                        // get elementNames size
    }

    private void printElementNames(List<String> names) {
        for (String element : names) {
            System.out.println(element);                                        // print elementNames
        }
    }

    // nextButton
    private WebElement getNextButton() {
        return this.nextButton;                                                 // get nextButton element
    }

    private String getNextButtonAttributeValue(String attribute) {
        return getNextButton().getAttribute(attribute);                         // get nextButton attribute
    }

    private void clickNextButton() {
        getNextButton().click();                                                // click nextButton
    }

    // totalPageNumber
    private WebElement getTotalPageNumber() {
        return this.totalPageNumber;                                            // get totalPageNumber element
    }

    protected int getTotalPageNumberValue() {
        return Integer.parseInt(getTotalPageNumber().getText());                // get totalPageNumber value
    }

    /*
     * Functional
     */

    // Overload
    private void presentationSleep() {
        presentationSleep(1);                                                   // one-second sleep
    }

    // Overload
    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);                           // set seconds sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Count number of pages
    protected int countNumberOfPages() {
        int count = 1;
        try {
            // Check number of element per page (on the first page)
            System.out.println("Amount of components on one page : " + getElementNamesSize());

            List<String> names = new ArrayList<>();
            // Adding elements to the list
            for (WebElement element : getElementNames()) {
                names.add(element.getText());                                   // add element title to final list
            }
            // Traversing through the table until the last button and adding names to the list defined above
            while(getNextButtonAttributeValue(DISABLED_ATTRIBUTE) == null) {
                clickNextButton();                                              // click next button
                count++;                                                        // increase page count
                System.out.println(count);
                presentationSleep(3);
                //TimeUnit.SECONDS.sleep(3);
                elementNames = driver.findElements(By.cssSelector("div.content-clubs-list.content-clubs-block div.name"));
                // List<WebElement> list = getElementNames();
                // elementNames = getElementNames();                            // error
                for (WebElement element : elementNames) {
                    names.add(element.getText());                               // add component title to the list
                }
            }
            printElementNames(names);                                           // printing the whole list of elements
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /*
     * Business Logic
     */

}
