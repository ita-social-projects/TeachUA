package com.softserve.edu.testcases.tools.browser;

import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class DriverWrapper {

    private static final Long IMPLICITLY_WAIT_SECONDS = 10L;    // time for implicit wait
    private static DriverWrapper instance = null;               // create instance of this class DriverWrapper
    private WebDriver driver;                                   // create WebDriver instance
    private Browsers browser = null;                            // create instance

    // Constructor is private to not have a possibility to create object of this class
    private DriverWrapper() {
    }

    // Initialize instance object
    public static DriverWrapper get() {
        if (instance == null) {                                 // check if instance of the class is initialized
            instance = new DriverWrapper();                     // initialize instance if it's null
        }
        return instance;
    }

    // STRATEGY pattern implementation
    public void setDriverStrategy(Browsers browser) {
         // Set parameter with an interface type to accept multiple classes that implements this interface
        this.browser = browser;                                 // store enum object in browser
    }

    // Get WebDriver instance
    public WebDriver driver() {
        if (driver == null) {                                   // check if driver instance already initialized
            //driver = new ChromeDriver();                      // initialize driver instance
            //part of the Strategy pattern
            if (browser == null) {                              // check if browser instance already initialized
                setDriverStrategy(Browsers.DEFAULT_BROWSER);    // set browser instance
            }
            driver = browser.runBrowser();                      // initialize driver instance with an object set in strategy
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS)); // set implicit wait
            driver.manage().window().maximize();                // make full screen browser window
        }
        return driver;
    }

    // Get URL
    public void url(String url) {
        driver().get(url);                                      // get URL
    }

    // Close driver
    public void quit() {
        if (driver() != null) {
            driver().quit();                                    // close driver
        }
    }

    // Delete cookies
    public void deleteCookies() {
        driver().manage().deleteAllCookies();                   // delete all cookies
    }

}
