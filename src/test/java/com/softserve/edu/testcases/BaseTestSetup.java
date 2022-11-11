package com.softserve.edu.testcases;

import com.softserve.edu.pages.common.home.HomePage;
import com.softserve.edu.services.database.Database;
import com.softserve.edu.testcases.tools.browser.Browsers;
import com.softserve.edu.testcases.tools.browser.DriverWrapper;
import com.softserve.edu.utils.ConfigPropertiesReader;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public abstract class BaseTestSetup {

    private final Long ONE_SECOND_DELAY = 1000L;                                        // one-second delay
    protected ConfigPropertiesReader config = new ConfigPropertiesReader();             // get test data
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());           // logger
    protected Database db = new Database();                                             // database connection

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

    // Annotation add received image into allure report (type says allure to understand byte sequence as image, value = attachName)
    @Attachment(value = "{0}", type = "image/png")
    private byte[] saveImageAttachment(String attachName) {

        byte[] result = null;
        File scrFile = ((TakesScreenshot) DriverWrapper.get().driver()).getScreenshotAs(OutputType.FILE); // take screenshot
        try{
            // Write taken screenshot as byte array
            result = Files.readAllBytes(scrFile.toPath());
            // Place taken screenshot into appropriate directory with the following name
            FileUtils.copyFile(scrFile, new File("./screenshots/" + attachName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();                                                        // throw exception
        }
        return result;
    }

    // Type says allure to understand byte sequence as text
    @Attachment(value = "{0}", type = "text/plain")
    private byte[] saveHtmlAttachment(String attachName) {
        return DriverWrapper.get().driver().getPageSource().getBytes();
    }

//    @Attachment(value = "{0}", type = "video/avi", fileExtension = ".avi")
//    public byte[] saveVideoAttachment(String attachName) {
//        File video = new File(attachName);
//        try {
//            byte[] byteArr = IOUtils.toByteArray(Files.newInputStream(Paths.get("./video/" + attachName + ".avi")));
//            byte[] decode = Base64.getDecoder().decode(byteArr);
//            FileUtils.writeByteArrayToFile(video, decode);
//            return byteArr;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new byte[0];
//        }
//    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        DriverWrapper.get().quit();                                                     // close driver
    }

    @BeforeClass
    public void beforeClass() {
        DriverWrapper.get().setDriverStrategy(Browsers.CHROME_BROWSER);                 // set browser
        DriverWrapper.get().driver();                                                   // get and initialize driver
    }

    @BeforeMethod
    public void beforeMethod() {
        DriverWrapper.get().url(config.getBaseURL());                                   // navigate to URL
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        presentationSleep(); // For Presentation ONLY
        if (!result.isSuccess()) {
            String testName = result.getName();
            logger.error("Test case error, name = " + testName + " ERROR");
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
