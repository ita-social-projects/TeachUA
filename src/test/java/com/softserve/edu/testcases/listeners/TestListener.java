package com.softserve.edu.testcases.listeners;

import com.softserve.edu.utils.VideoRecorderUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());         // logger

    // Invoked after test class is instantiated and before execution of any testNG method
    @Override
    public void onTestStart(ITestResult iTestResult) {      // ITestResult is an interface that defines the result of the test
        // Start recording video and pass the method name as method parameter
        VideoRecorderUtility.startRecording(iTestResult.getMethod().getMethodName());
    }

    // Set up behaviour on test success
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        // Print corresponding message about successful test run
        logger.info("Test PASSED " + iTestResult.getClass().getName() + " - " + iTestResult.getMethod().getMethodName());
        // Stop screen recording and remove recorded video file for a certain method
        VideoRecorderUtility.stopRecording(false);
    }

    // Set up behaviour on test failure
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        // Print corresponding message about failed test run
        logger.info("Test FAILED " + iTestResult.getClass().getName() + " - " + iTestResult.getMethod().getMethodName());
        // Stop screen recording and save recorded video file for a certain method
        VideoRecorderUtility.stopRecording(true);
    }

    // Set up behaviour when test skipped
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        // Print corresponding message about skipped test run
        logger.info("Test SKIPPED " + iTestResult.getClass().getName() + " - " + iTestResult.getMethod().getMethodName());
        // Stop screen recording and remove recorded video file for a certain method
        VideoRecorderUtility.stopRecording(false);
    }

    // Invoked whenever a method fails but within the defined success percentage
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    // Invoked after test class is instantiated and before execution of any testNG method.
    @Override
    public void onStart(ITestContext iTestContext) { // ITestContext is a class that contains information about the test run
    }

    // Invoked after all tests of a class are executed
    @Override
    public void onFinish(ITestContext iTestContext) {
    }

}
