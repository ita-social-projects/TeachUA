package com.softserve.edu.utils;

import com.softserve.edu.testcases.tools.browser.DriverWrapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class JsMethods {

    // Create object of the class JavascriptExecutor and initialize it
    private static JavascriptExecutor js = (JavascriptExecutor) DriverWrapper.get().driver();
    private static final String CLICK_SCRIPT = "arguments[0].click()";                  // click on element
    private static final String SCROLL_TO_ELEMENT = "arguments[0].scrollIntoView();";   // scroll to element

    // Constructor is private to not have a possibility to create object of this class
    private JsMethods() {
    }

    // Click on element
    public static void clickElement(WebElement element) {
        js.executeScript(CLICK_SCRIPT, element);
    }

    // Scroll to element
    public static void scrollToElement(WebElement element) {
        js.executeScript(SCROLL_TO_ELEMENT, element);
    }

}
