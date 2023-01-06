package com.softserve.edu.testcases.tools.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

interface Browser {
    WebDriver getBrowser();                             // factory method
}

class FirefoxBrowser implements Browser {               // if we only create class object then methods won't run
    public WebDriver getBrowser() {                     // only when this method called, code inside will run
        WebDriverManager.firefoxdriver().setup();       // download the latest webDriver
        return new FirefoxDriver();                     // initialize driver instance
    }
}

class FirefoxWithoutUI implements Browser {
    public WebDriver getBrowser() {
        WebDriverManager.firefoxdriver().setup();       // download the latest webDriver
        FirefoxOptions options = new FirefoxOptions();  // initialize driver instance
        options.addArguments("--window-size=1920,1080");// set window size
        options.addArguments("--start-maximized");      // start maximized screen
        options.addArguments("--headless");             // run in headless mode
        options.addArguments("--no-proxy-server");      // cno proxy server
        options.addArguments("--ignore-certificate-errors");    // ignore-certificate-errors
        return new FirefoxDriver(options);              // initialize driver instance with options to run browser w/o UI
    }
}

class ChromeBrowser implements Browser {
    public WebDriver getBrowser() {
        WebDriverManager.chromedriver().setup();        // download the latest webDriver
        return new ChromeDriver();                      // initialize driver instance
    }
}

class ChromeWithoutUI implements Browser {
    public WebDriver getBrowser() {
        WebDriverManager.chromedriver().setup();        // download the latest webDriver
        ChromeOptions options = new ChromeOptions();    // initialize driver instance
        options.addArguments("--window-size=1920,1080");// set window size
        options.addArguments("--start-maximized");      // start maximized screen
        options.addArguments("--headless");             // run in headless mode
        options.addArguments("--no-proxy-server");      // cno proxy server
        options.addArguments("--ignore-certificate-errors");    // ignore-certificate-errors
        return new ChromeDriver(options);               // initialize driver instance with options to run browser w/o UI
    }
}

// Repository of class objects to run browser
public enum Browsers {
    DEFAULT_BROWSER(new ChromeBrowser()),               // ChromeBrowser class
    FIREFOX_BROWSER(new FirefoxBrowser()),              // FirefoxBrowser class
    FIREFOX_WITHOUT_UI(new FirefoxWithoutUI()),         // FirefoxWithoutUI class
    CHROME_BROWSER(new ChromeBrowser()),                // ChromeBrowser class
    CHROME_WITHOUT_UI(new ChromeWithoutUI());           // ChromeWithoutUI class

    private final Browser browser;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    Browsers(Browser browser) {
        this.browser = browser;
    }

    // From the name of the object (CHROME_BROWSER, FIREFOX_BROWSER,..) get a certain browser
    public WebDriver runBrowser() {
        return browser.getBrowser();                    // browser equals to some objects from enum list
    }
}
