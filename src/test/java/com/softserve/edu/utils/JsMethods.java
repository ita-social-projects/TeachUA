package com.softserve.edu.utils;

import com.softserve.edu.testcases.tools.browser.DriverWrapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class JsMethods {

    // Create object of the class JavascriptExecutor and initialize it
    private static JavascriptExecutor js = (JavascriptExecutor) DriverWrapper.get().driver();

    // Constructor is private to not have a possibility to create object of this class
    private JsMethods() {
    }

    // Click on element
    public static void clickElement(WebElement element) {
        js.executeScript("arguments[0].click()", element);
    }

    // Scroll to element
    public static void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

}
