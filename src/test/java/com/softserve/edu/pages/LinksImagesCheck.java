package com.softserve.edu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinksImagesCheck {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());         // logger
    private static final String LINK_TAG = "a";                                     // tag to find links on a page
    private static final String IMAGE_TAG = "img";                                 // tag to find images on a page
    private static final String HREF_ATTRIBUTE = "href";                            // href attribute
    private static final String SRC_ATTRIBUTE = "src";                              // src attribute
    // Script that checks if image is displayed
    private static final String IMAGE_DISPLAYED =
            "return (typeof arguments[0].naturalWidth !=\"undefined\" && arguments[0].naturalWidth > 0);";
    private static final String CHALLENGE_DROPDOWN = "div[style$='944px;'], div[class*='hidden']";
    private static final String CHALLENGE_LINKS = "//li[@class='ant-menu-item ant-menu-item-only-child']//a";
    // Message if there are no links on the page
    private static final String LINKS_NOT_FOUND = "There are no links on the page";
    // Message if there are no images on the page
    private static final String IMAGES_NOT_FOUND = "There are no images on the page";
    // Error message if href/src attribute is empty
    private static final String EMPTY_URL = "Provided URL is empty. Please check href/src attributes in the DOM!";
    private static final int HTTP_BAD_REQUEST = 400;                                // bad request status code
    private static final int CONNECTION_TIMEOUT = 5000;                             // connection timeout


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
        images.addAll(driver.findElements(By.tagName(IMAGE_TAG)));                  // find all the images on the page
        if(driver.findElements(By.cssSelector(CHALLENGE_DROPDOWN)).size() != 0) {
            links.removeAll(driver.findElements(By.xpath(CHALLENGE_LINKS)));
        }
        else {
            links.clear();
            images.clear();
            links.addAll(driver.findElements(By.xpath(CHALLENGE_LINKS)));
        }
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
                HttpURLConnection httpURLConnection = (HttpURLConnection) (url.openConnection(Proxy.NO_PROXY));
                // Set timeout before establishing connection (it is important to wait before creating a connection
                // as the URL may take time to load)
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                // Create connection
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() >= HTTP_BAD_REQUEST) {
                    // Print error message if broken link was found
                    logger.error(linkUrl + " is a broken link with HTTP status code " + httpURLConnection.getResponseCode());
                }
            } catch (Exception ignored) {

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
        logger.info("There are {} links on the page", getLinksSize());
        for(int i = 0; i < getLinksSize(); i++) {
            // Get href attribute value of element on the position i and verify it using verifyLinks method
            verifyLink(getLinks().get(i).getAttribute(HREF_ATTRIBUTE));
        }
    }

    // Check if image and its url are not broken
    protected void checkImages() {
        logger.info("There are {} images on the page", getImagesSize());
        for(int i = 0; i < getImagesSize(); i++) {
            WebElement image = getImages().get(i);                                  // get image WebElement on the position i
            String imageUrl = image.getAttribute(SRC_ATTRIBUTE);                    // get src attribute value
            // Get src attribute value of element on the position i and verify it using verifyLinks method
            verifyLink(imageUrl);                                                   // verify image link
            validateImage(image, imageUrl);                                         // verify image
        }
    }

    /*
     * Business Logic
     */

}
