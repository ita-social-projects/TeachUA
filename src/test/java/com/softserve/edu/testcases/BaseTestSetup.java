package com.softserve.edu.testcases;

import com.softserve.edu.pages.common.home.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public abstract class BaseTestSetup {

    private WebDriver driver;                                                           // WebDriver instance
    private final Long IMPLICITLY_WAIT_SECONDS = 10L;                                   // time for implicit wait
    private final Long ONE_SECOND_DELAY = 1000L;                                        // one-second delay
    private final String TIME_TEMPLATE = "yyyy-MM-dd_HH-mm-ss-S";                       // time format
    protected ConfigPropertiesReader config = new ConfigPropertiesReader();             // get test data
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());           // logger
    protected SoftAssert softAssert = new SoftAssert();                                 // soft asserts

    // Overload
    protected void presentationSleep() {
        presentationSleep(1);
    }

    // Overload
    protected void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takeScreenshot(String testName) {
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).
                format(new Date());                                                     // get current date and time
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);     // take screenshot
        try {
            // Place taken screenshot into appropriate directory with the following name
            FileUtils.copyFile(scrFile, new File("./screenshots/" + currentTime + "_" + testName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();                                                        // throw exception
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();                                        // get latest ChromeDriver
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (driver != null) {
            driver.quit();                                                              // close driver
        }
    }

    @BeforeClass
    public void beforeClass() {
        // Initialize driver instance with Chrome driver
        driver = new ChromeDriver();
        // Set time for implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();                                            // maximize window
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get(config.getBaseURL());                                                // navigate to URL
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        presentationSleep(); // For Presentation ONLY
        if (!result.isSuccess()) {
            String testName = result.getName();
            logger.error("***TC error, name = " + testName + " ERROR");
            // Take Screenshot, Save sourceCode, Save to log, Prepare report, Return to previous state, logout, etc.
            takeScreenshot(testName);
            // Clear cache; delete cookie; delete session;
            driver.manage().deleteAllCookies();
        }
    }

    protected HomePage loadApplication() {
        return new HomePage(driver);                                                    // load HomePage
    }

}
