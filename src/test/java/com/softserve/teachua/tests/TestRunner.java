package com.softserve.teachua.tests;

import com.softserve.teachua.pages.home.HomePage;
import com.softserve.teachua.tools.TextUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@ExtendWith(RunnerExtensionResult.class)
public abstract class TestRunner {
    private final String GET_TEST_NAME_PATTERN = ".+\\.(\\w+)\\(.+";
    private final String TIME_TEMPLATE = "yyyy-MM-dd_HH-mm-ss-S";
    private final String BASE_URL = "https://speak-ukrainian.org.ua/dev/";
    private static final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private final Long ONE_SECOND_DELAY = 1000L;
    private static WebDriver driver;
    protected static Boolean isTestSuccessful = false;
    /*
    protected String testName;
    protected boolean isTestSuccess;
    */

    // Overload
    protected void presentationSleep() {
        presentationSleep(1);
    }

    // Overload
    protected void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void takeScreenShot(String testname) {
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("./" + currentTime + "_" + testname + "_screenshot.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void takePageSource(String testname) {
        String currentTime = new SimpleDateFormat(TIME_TEMPLATE).format(new Date());
        String pageSource = driver.getPageSource();
        byte[] strToBytes = pageSource.getBytes();
        Path path = Paths.get("./" + currentTime + "_" + testname + "_source.html");
        try {
            Files.write(path, strToBytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
        //
        //driver = new ChromeDriver();
        //
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        driver = new ChromeDriver(options);
        //
        //driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
        //
        System.out.println("@BeforeAll executed");
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("@AfterAll start");
        //TestRunner.this.presentationSleep(); // For Presentation ONLY
        if (driver != null) {
            //driver.close(); // Close browser only
            System.out.println("@AfterAll driver.quit();");
            driver.quit();
        }
        System.out.println("@AfterAll executed");
    }

    @BeforeEach
    public void beforeMethod() {
        driver.get(BASE_URL);
        presentationSleep(); // For Presentation ONLY
        /*
        isTestSuccess = false;
        */
        System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        presentationSleep(); // For Presentation ONLY
        // logout; clear cache; delete cookie; delete session;
        // Save Screen;
        /*
        if (!isTestSuccess) {
            takeScreenShot(testName);
            takePageSource(testName);
        }
        */
        //System.out.println("\t\t\t\t\t*** testInfo.getTestMethod() = " + testInfo.getTestMethod());
        //System.out.println("\t\t\t\t\t*** testInfo.getDisplayName() = " + testInfo.getDisplayName());
        if (!isTestSuccessful) {
            String testName = TextUtils.unpackFirstGroupSubText(GET_TEST_NAME_PATTERN, testInfo.getTestMethod().toString());
            //System.out.println("\t\t\t\t\t*** testName = " + testName);
            takeScreenShot(testName);
            takePageSource(testName);
        }
        System.out.println("\t@AfterEach executed");
    }

    protected HomePage loadApplication() {
        //driver.get(BASE_URL);
        return new HomePage(driver);
        //return new HomePage(getDriver());
    }

}
