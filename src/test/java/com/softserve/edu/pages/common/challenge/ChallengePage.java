package com.softserve.edu.pages.common.challenge;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ChallengePage extends TopPart {

    private WebElement challengeName;                                           // challenge name
    private WebElement challengeDescription;                                    // challenge description
    private WebElement initiative;                                              // page name text
    private WebElement ourContacts;                                             // our contacts text
    private WebElement facebook;                                                // facebook account
    private WebElement youtube;                                                 // youtube account
    private WebElement instagram;                                               // instagram account
    private WebElement mail;                                                    // mail
    private WebElement helpProjectButton;                                       // help project button


    // Constructor
    public ChallengePage(WebDriver driver) {
        super(driver);
        initElements();                                                         // initialize elements on the page
    }

    // Check if elements present on the page
    private void initElements() {
        challengeName = driver.findElement(By.cssSelector(".title"));
        challengeDescription = driver.findElement(By.cssSelector(".challenge-description"));
        initiative = driver.findElement(By.cssSelector(".city-name-box>h2.city-name"));
        ourContacts = driver.findElement(By.cssSelector(".social-media .text"));
        facebook = driver.findElement(By.cssSelector(".social-info .anticon.anticon-facebook.icon"));
        youtube = driver.findElement(By.cssSelector(".social-info .anticon.anticon-youtube.icon"));
        instagram = driver.findElement(By.cssSelector(".social-info .anticon.anticon-instagram.icon"));
        mail = driver.findElement(By.cssSelector(".social-info .anticon.anticon-mail.icon"));
        helpProjectButton = driver.findElement(By.cssSelector(".help-button .ant-btn.ant-btn-default.flooded-button.donate-button span"));
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
