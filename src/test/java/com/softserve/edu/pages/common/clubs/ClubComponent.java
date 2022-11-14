package com.softserve.edu.pages.common.clubs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ClubComponent {

//    // Selector to find full star in rate
//    private final String FULL_STAR_CSS_SELECTOR = ".ant-rate-star.ant-rate-star-full";
//    // Selector to get the last full star in rate
//    private final String RATE_VALUE_SELECTOR = ">div";
//    // Attribute to set position number of the star in rate
//    private final String ARIA_POSINSET_ATTRIBUTE = "aria-posinset";
//    // CSS selector to find location element
//    private final String LOCATION_CSS_SELECTOR = ".oneAddress";

    // Element that represents the whole component
    private WebElement clubLayout;

    // Parts of the components
    private WebElement title;                                                   // club title
    private WebElement category;                                                // club category
    private WebElement partialDescription;                                      // club partial description
    private WebElement rate;                                                    // club rate
    private WebElement location;                                                // club location
    private WebElement detailsButton;                                           // club details button

    // Constructor
    public ClubComponent(WebElement clubLayout) {
        this.clubLayout = clubLayout;
        // Initialize elements on the page
        initElements();
    }

    private void initElements() {
        title = clubLayout.findElement(By.xpath("//div[@class='title']//div[@class='name']"));
        category = clubLayout.findElement(By.cssSelector(".ant-tag.tag .name"));
        partialDescription = clubLayout.findElement(By.cssSelector(".ant-card-body .description"));
        rate = clubLayout.findElement(By.cssSelector("ul.ant-rate.ant-rate-disabled.rating"));
        detailsButton = clubLayout.findElement(By.cssSelector(".ant-btn.ant-btn-default.outlined-button.details-button a"));
    }

    /*
     * Page Object
     */
    // title
    private WebElement getTitle() {
        return this.title;                                                      // get title element
    }

    protected String getTitleText() {
        return getTitle().getText();                                            // get title text
    }

//    protected void clickTitle() {
//        getTitle().click();                                                     // click title
//    }
//
//    // category
//    private WebElement getCategory() {
//        return this.category;                                                   // get category element
//    }
//
//    protected String getCategoryText() {
//        return getCategory().getText();                                         // get category text
//    }
//
//    protected void clickCategory() {
//        getCategory().click();                                                  // click category
//    }
//
//    // partialDescription
//    private WebElement getPartialDescription() {
//        return this.partialDescription;                                         // get partialDescription element
//    }
//
//    protected String getPartialDescriptionText() {
//        return getPartialDescription().getText();                               // get partialDescription text
//    }
//
//    protected void clickPartialDescription() {
//        getPartialDescription().click();                                        // click partialDescription
//    }
//
//    // rate
//    private WebElement getRate() {
//        return this.rate;                                                       // get rate element
//    }
//
//    // location
//    private WebElement getLocation() {
//        // Presence of location element should be checked here to prevent test failure when there is no location
//        // selected in advanced search
//        location = clubLayout.findElement(By.cssSelector(LOCATION_CSS_SELECTOR));
//        return this.location;                                                   // get location element
//    }
//
//    protected String getLocationText() {
//        return getLocation().getText();                                         // get location text
//    }
//
//    protected void clickLocation() {
//        getLocation().click();                                                  // click location
//    }
//
//    // details
//    private WebElement getDetailsButton() {
//        return this.detailsButton;                                              // get details element
//    }
//
//    protected String getDetailsButtonText() {
//        return getDetailsButton().getText();                                    // get details text
//    }
//
//    protected void clickDetailsButton() {
//        getDetailsButton().click();                                             // click details
//    }
//
//    /*
//     * Functional
//     */
//    protected int getRateValue() {
//        int rate = 0;
//        // Check all the elements with full star
//        for(WebElement element : getRate().findElements(By.cssSelector(FULL_STAR_CSS_SELECTOR))) {
//            int check = Integer.parseInt(element.
//                    findElement(By.cssSelector(RATE_VALUE_SELECTOR)).getAttribute(ARIA_POSINSET_ATTRIBUTE));
//            // Check the last full star and assign that number to rate variable
//            if(check > rate) {
//                rate = check;
//            }
//        }
//        return rate;                                                            // get rate
//    }

    /*
     * Functional
     */

    /*
     * Business Logic
     */

}
