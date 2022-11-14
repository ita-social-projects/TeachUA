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
        educationServicesTitle = driver.findElement(By.xpath("//div[@class='content-title']"));
        educationServicesDescription = driver.findElement(By.cssSelector(".content-text"));
        faqTitle = driver.findElement(By.cssSelector(".faq-title"));
        initiative = driver.findElement(By.cssSelector(".city-name-box>h2.city-name"));
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
