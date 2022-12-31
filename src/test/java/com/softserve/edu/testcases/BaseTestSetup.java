package com.softserve.edu.testcases;

import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.services.database.Database;
import com.softserve.edu.testcases.tools.browser.Browsers;
import com.softserve.edu.testcases.tools.browser.DriverWrapper;
import com.softserve.edu.utils.ConfigPropertiesReader;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public abstract class BaseTestSetup {

    private Browsers browser = Browsers.CHROME_WITHOUT_UI;                              // set default browser as Chrome
    protected ConfigPropertiesReader config = new ConfigPropertiesReader();             // get test data
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());           // logger
    protected Database db = new Database();                                             // database connection

    // Set driver to 'chrome without UI' if 'mode' key and 'headless' value are present in xml file
    private void checkXmlParameters(ITestContext context) {
        // Defines a test context which contains all the information for a given test run
        if(context != null) {
            // Get all parameters from current xml file as set view of all entries containing key-value pairs
            for(Map.Entry<String, String> entry : context.getCurrentXmlTest().getAllParameters().entrySet()) {
                // Check if there is mode-headless pair present in xml
                if (entry.getKey().contains("mode") && entry.getValue().contains("headless")) {
                    // If yes, run browser on headless mode
                    browser = Browsers.CHROME_WITHOUT_UI;
                    break;
                }
            }
        }
    }

    // Annotation add received image into allure report (type says allure to understand byte sequence as image, value = attachName)
    @Attachment(value = "{0}", type = "image/png")
    private byte[] saveImageAttachment(String attachName) {
        byte[] result = null;
        try{
            // Take screenshot and get output it as file
            File scrFile = ((TakesScreenshot) DriverWrapper.get().driver()).getScreenshotAs(OutputType.FILE);
            // Write taken screenshot as byte array
            result = Files.readAllBytes(scrFile.toPath());
            // Place taken screenshot into appropriate directory with the following name
            FileUtils.copyFile(scrFile, new File("./screenshots/" + attachName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Type says allure to understand byte sequence as text
    @Attachment(value = "{0}", type = "text/plain")
    private byte[] saveHtmlAttachment(String attachName) {
        return DriverWrapper.get().driver().getPageSource().getBytes();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        DriverWrapper.get().quit();                                                     // close driver
    }

    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        // Run browser in headless mode if such parameter is present in xml file
        checkXmlParameters(context);
        DriverWrapper.get().setDriverStrategy(browser);                                 // set up browser
    }

    @BeforeMethod
    public void beforeMethod() {
        DriverWrapper.get().url(config.getBaseURL());                                   // navigate to URL
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (!result.isSuccess()) {
            // Take screenshot and save it in allure report
            saveImageAttachment(result.getName() + "_image");
            // Take sourceCode and save it in allure report
            saveHtmlAttachment(result.getName() + "_sourceCode");
            // Clear cache, delete cookie, delete session
            DriverWrapper.get().deleteCookies();
        }
    }

    @Step("Load application")
    protected HomePage loadApplication() {
        logger.info("Home page opened");                                                // information about current page
        return new HomePage(DriverWrapper.get().driver());                              // load HomePage
    }

}
