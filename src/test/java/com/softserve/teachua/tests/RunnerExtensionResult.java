package com.softserve.teachua.tests;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class RunnerExtensionResult implements AfterTestExecutionCallback  {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        TestRunner.isTestSuccessful = !context.getExecutionException().isPresent();
    }

}
