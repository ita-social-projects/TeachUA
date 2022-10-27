package com.softserve.edu.pages.guest.challenge;

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

    // challengeName
    private WebElement getChallengeName() {
        return this.challengeName;                                              // get challengeName element
    }

    private String getPageNameText() {
        return getChallengeName().getText();                                    // get challengeName text
    }

    // challengeDescription
    private WebElement getChallengeDescription() {
        return this.challengeDescription;                                       // get challengeDescription element
    }

    private String getClubsInCityText() {
        return getChallengeDescription().getText();                             // get challengeDescription text
    }

    // initiative
    private WebElement getInitiative() {
        return initiative;                                                      // get initiative element
    }

    protected String getInitiativeText() {
        return getInitiative().getText();                                       // get initiative text
    }

    // ourContacts
    private WebElement getOurContacts() {
        return this.ourContacts;                                                // get ourContacts element
    }

    private String getOurContactsText() {
        return getOurContacts().getText();                                      // get ourContacts text
    }

    // facebook
    private WebElement getFacebook() {
        return this.facebook;                                                   // get facebook element
    }

    private void clickFacebook() {
        getFacebook().click();                                                  // click facebook
    }

    // youtube
    private WebElement getYoutube() {
        return this.youtube;                                                    // get youtube element
    }

    private void clickYoutube() {
        getYoutube().click();                                                   // click youtube
    }

    // instagram
    private WebElement getInstagram() {
        return this.instagram;                                                  // get instagram element
    }

    private void clickInstagram() {
        getInstagram().click();                                                 // click instagram
    }

    // mail
    private WebElement getMail() {
        return this.mail;                                                       // get mail element
    }

    private void clickMail() {
        getMail().click();                                                      // click mail
    }

    // helpProjectButton
    private WebElement getHelpProjectButton() {
        return this.helpProjectButton;                                          // get helpProjectButton element
    }

    private String getHelpProjectButtonText() {
        return getHelpProjectButton().getText();                                // get helpProjectButton text
    }

    private void clickHelpProjectButton() {
        getHelpProjectButton().click();                                         // click helpProjectButton
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
