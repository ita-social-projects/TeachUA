package com.softserve.teachua.tests;

import com.softserve.teachua.pages.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public abstract class TestRunner {

    private final String BASE_URL = "https://speak-ukrainian.org.ua/dev/";
    private static final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private final Long ONE_SECOND_DELAY = 1000L;
    private static WebDriver driver;

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
    public static void tearDown() {
        //TestRunner.this.presentationSleep(); // For Presentation ONLY
        if (driver != null) {
            //driver.close(); // Close browser only
            driver.quit();
        }
        System.out.println("@AfterAll executed");
    }

    @BeforeEach
    public void beforeMethod() {
        driver.get(BASE_URL);
        presentationSleep(); // For Presentation ONLY
        System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void afterMethod() {
        presentationSleep(); // For Presentation ONLY
        // logout; clear cache; delete cookie; delete session;
        // Save Screen;
        System.out.println("\t@AfterEach executed");
    }

    protected HomePage loadApplication() {
        //driver.get(BASE_URL);
        return new HomePage(driver);
        //return new HomePage(getDriver());
    }

}
