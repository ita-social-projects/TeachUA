package com.softserve.edu.pages.common.servicesinukrainian;

import com.softserve.edu.pages.TopPart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ServicesInUkrainianPage extends TopPart {

    private WebElement servicesInUkrainian;                                         // services in ukrainian text
    private WebElement ourContacts;                                                 // our contacts text
    private WebElement facebook;                                                    // facebook account
    private WebElement youtube;                                                     // youtube account
    private WebElement instagram;                                                   // instagram account
    private WebElement mail;                                                        // mail
    private WebElement helpProjectButton;                                           // help project button
    private WebElement educationServicesTitle;                                      // education services title
    private WebElement educationServicesDescription;                                // education services description
    private WebElement faqTitle;                                                    // faq title
    private WebElement initiative;                                              // page name text

    // Constructor
    public ServicesInUkrainianPage(WebDriver driver) {
        super(driver);
        initElements();                                                             // initialize elements on the page
    }

    // Check if elements present on the page
    private void initElements() {
        servicesInUkrainian = driver.findElement(By.cssSelector(".title .text"));
        ourContacts = driver.findElement(By.cssSelector(".social-media .text"));
        facebook = driver.findElement(By.cssSelector(".social-info .anticon.anticon-facebook.icon"));
        youtube = driver.findElement(By.cssSelector(".social-info .anticon.anticon-youtube.icon"));
        instagram = driver.findElement(By.cssSelector(".social-info .anticon.anticon-instagram.icon"));
        mail = driver.findElement(By.cssSelector(".social-info .anticon.anticon-mail.icon"));
        helpProjectButton = driver.findElement(By.cssSelector(".help-button .ant-btn.ant-btn-default.flooded-button.donate-button span"));
        educationServicesTitle = driver.findElement(By.cssSelector(".content-title"));
        educationServicesDescription = driver.findElement(By.cssSelector(".content-text"));
        faqTitle = driver.findElement(By.cssSelector(".faq-title"));
        initiative = driver.findElement(By.cssSelector(".city-name-box>h2.city-name"));
    }

    /*
     * Page Object
     */

    // servicesInUkrainian
    private WebElement getServicesInUkrainian() {
        return this.servicesInUkrainian;                                        // get servicesInUkrainian element
    }

    private String getServicesInUkrainianText() {
        return getServicesInUkrainian().getText();                              // get servicesInUkrainian text
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

    // educationServicesTitle
    private WebElement getEducationServicesTitle() {
        return this.educationServicesTitle;                                     // get educationServicesTitle element
    }

    private String getEducationServicesTitleText() {
        return getEducationServicesTitle().getText();                           // get educationServicesTitle text
    }

    // educationServicesDescription
    private WebElement getEducationServicesDescription() {
        return this.educationServicesDescription;                               // get educationServicesDescription element
    }

    private String getEducationServicesDescriptionText() {
        return getEducationServicesDescription().getText();                     // get educationServicesDescription text
    }

    // faqTitle
    private WebElement getFaqTitle() {
        return this.faqTitle;                                                   // get faqTitle element
    }

    private String getFaqTitleText() {
        return getFaqTitle().getText();                                         // get faqTitle text
    }

    // initiative
    private WebElement getInitiative() {
        return initiative;                                                      // get initiative element
    }

    protected String getInitiativeText() {
        return getInitiative().getText();                                       // get initiative text
    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
