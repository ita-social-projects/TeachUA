package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class LinksImagesCheck {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());         // logger
    private final String LINK_TAG = "a";                                            // tag to find links on a page
    private final String IMAGE_TAGE = "img";                                        // tag to find images on a page
    private final String HREF_ATTRIBUTE = "href";                                   // href attribute
    private final String SRC_ATTRIBUTE = "src";                                     // src attribute
    // Script that checks if image is displayed
    private final String IMAGE_DISPLAYED =
            "return (typeof arguments[0].naturalWidth !=\"undefined\" && arguments[0].naturalWidth > 0);";
    // Message if there are no links on the page
    private final String LINKS_NOT_FOUND = "There are no links on the page";
    // Message if there are no images on the page
    private final String IMAGES_NOT_FOUND = "There are no images on the page";
    // Error message if href/src attribute is empty
    private final String EMPTY_URL = "Provided URL is empty. Please check href/src attributes in the DOM!";
    private final String REQUEST_METHOD = "HEAD";                                   // request method
    private final int HTTP_BAD_REQUEST = 400;                                       // bad request status code
    private final int CONNECTION_TIMEOUT = 5000;                                    // connection timeout


    private final WebDriver driver;                                                 // create WebDriver instance
    private List<WebElement> links;                                                 // list to store links
    private List<WebElement> images;                                                // list to store images

    // Constructor
    public LinksImagesCheck(WebDriver driver) {
        this.driver = driver;
        initElements();                                                             // initialize elements on the page
    }

    // Check if elements present on the page
    private void initElements() {
        links = new ArrayList<>();                                                  // create object of the class ArrayList
        images = new ArrayList<>();                                                 // create object of the class ArrayList
        links.addAll(driver.findElements(By.tagName(LINK_TAG)));                    // find all the links on the page
        images.addAll(driver.findElements(By.tagName(IMAGE_TAGE)));                 // find all the images on the page
    }

    /*
     * Page Object
     */

    // links
    private List<WebElement> getLinks() {
        // Check if links list contains values
        if(links == null) {
            throw new RuntimeException(LINKS_NOT_FOUND);
        }
        return links;                                                               // get links list
    }

    private int getLinksSize() {
        return getLinks().size();                                                   // links list size
    }

    // images
    private List<WebElement> getImages() {
        // Check if images list contains some values
        if(images == null) {
            throw new RuntimeException(IMAGES_NOT_FOUND);
        }
        return images;                                                              // get images list
    }

    private int getImagesSize() {
        return getImages().size();                                                  // images list size
    }

    /*
     * Functional
     */

    // Check if URL is valid
    private boolean isURLValid(String url) {
        // TODO Refactor if statements to make it looks better
        if(url == null || url.startsWith("mailto") || url.startsWith("tel")) {
            return false;
        }
        if (url.isEmpty()) {
            logger.error(EMPTY_URL);
            return false;
        }
        return true;
    }

    // Check if link is not broken
    private void verifyLink(String linkUrl) {
        if(isURLValid(linkUrl)) {
            try{
                // Create and initialize with linkUrl object url of a type URL
                URL url = new URL(linkUrl);

                // Used for indicating whether HTTP redirects (3xx) should be automatically followed
                HttpURLConnection.setFollowRedirects(false);

                // Create URL connection
                HttpURLConnection httpURLConnection = (HttpURLConnection) (url.openConnection());
                // Set timeout before establishing connection (it is important to wait before creating a connection
                // as the URL may take time to load)
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                // The request type is set to HEAD so that only Headers are returned
                //httpURLConnection.setRequestMethod(REQUEST_METHOD);
                // Create connection
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() >= HTTP_BAD_REQUEST) {
                    // Print error message if broken link was found
                    logger.error(linkUrl + " is a broken link with HTTP status code " + httpURLConnection.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Check if image is not broken
    private void validateImage(WebElement image, String imageUrl) {
        try{
             if(!((Boolean) ((JavascriptExecutor) driver).executeScript(IMAGE_DISPLAYED, image))) {
                 logger.error("Image with URL " + imageUrl + " is broken");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if all the links are not broken
    protected void checkLinks() {
        for(int i = 0; i < getLinksSize(); i++) {
            // Get href attribute value of element on the position i and verify it using verifyLinks method
            verifyLink(getLinks().get(i).getAttribute(HREF_ATTRIBUTE));
        }
    }

    // Check if image and its url are not broken
    protected void checkImages() {
        for(int i = 0; i < getImagesSize(); i++) {
            // Get image WebElement on the position i
            WebElement image = getImages().get(i);
            // Get src attribute value
            String imageUrl = image.getAttribute(SRC_ATTRIBUTE);
            // Get src attribute value of element on the position i and verify it using verifyLinks method
            verifyLink(imageUrl);                                                   // verify image link
            validateImage(image, imageUrl);                                         // verify image
        }
    }

    /*
     * Business Logic
     */

}
