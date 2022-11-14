package com.softserve.edu.pages.common.news;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewsPage extends TopPart {

    private WebElement pageName;                                                // page name text
    private WebElement clubsInCity;                                             // clubs in city text

    // Constructor
    public NewsPage(WebDriver driver) {
        super(driver);
        initElements();                                                         // initialize elements on the page
    }

    // Check if elements present on the page
    private void initElements() {
        pageName = driver.findElement(By.xpath("//div[@class='city-name-box-small-screen']//h2[@class='city-name']"));
        clubsInCity = driver.findElement(By.cssSelector(".sider-header>h2.city-name"));
    }

    /*
     * Page Object
     */

    // pageName
    private WebElement getPageName() {
        return this.pageName;                                                   // get pageName element
    }

    private String getPageNameText() {
        return getPageName().getText();                                         // get pageName text
    }

    // clubsInCity
    private WebElement getClubsInCity() {
        return this.clubsInCity;                                                // get clubsInCity element
    }

    private String getClubsInCityText() {
        return getClubsInCity().getText();                                      // get clubsInCity text
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
