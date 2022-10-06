package com.softserve.edu.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigPropertiesReader {

    //Path to the properties file
    private final String TEST_DATA_PROP_PATH = "./src/test/resources/links.properties";

    // Link to the application
    public String getBaseURL() {
        try {
            FileReader readFile = new FileReader(TEST_DATA_PROP_PATH);
            Properties prop = new Properties();
            prop.load(readFile);
            return prop.getProperty("URL");
        } catch (IOException e) {
            System.out.println("Exception");
        }
        return null;
    }

    // Link to database
    public String getDBURL() {
        try {
            FileReader readFile = new FileReader(TEST_DATA_PROP_PATH);
            Properties prop = new Properties();
            prop.load(readFile);
            return prop.getProperty("DB_URL");
        } catch (IOException e) {
            System.out.println("Exception");
        }
        return null;
    }

    // Database username
    public String getDBUsername() {
        return System.getenv().get("DB_LOGIN");
    }

    // Database password
    public String getDBPassword() {
        return System.getenv().get("DB_PASSWORD");
    }

    // Regular user username
    public String getRegularUsername() {
        return System.getenv().get("USER_LOGIN");
    }

    // Regular user password
    public String getRegularPassword() {
        return System.getenv().get("USER_PASSWORD");
    }

    // Admin user username
    public String getAdminUsername() {
        return System.getenv().get("ADMIN_LOGIN");
    }

    // Admin user password
    public String geAdminPassword() {
        return System.getenv().get("ADMIN_PASSWORD");
    }

}
