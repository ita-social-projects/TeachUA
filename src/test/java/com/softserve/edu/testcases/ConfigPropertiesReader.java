package com.softserve.edu.testcases;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigPropertiesReader {

    //Path to the properties file
    private final String TEST_DATA_PROP_PATH = "./src/test/java/com/softserve/edu/testcases/credentials.properties";

    public String getBaseURL() {
        try {
            FileReader readFile = new FileReader(TEST_DATA_PROP_PATH);
            Properties prop = new Properties();
            prop.load(readFile);
            return prop.getProperty("url");
        } catch (IOException e) {
            System.out.println("Exception");
        }
        return null;
    }

}
