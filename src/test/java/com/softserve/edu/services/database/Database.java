package com.softserve.edu.services.database;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class Database {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;
    private static final ConfigPropertiesReader config = new ConfigPropertiesReader();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());           // logger

    //Connect to database using JDBC
    // Establish DB connection
    private Connection openConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        try {
            // Connect to DB
            connection = DriverManager
                    .getConnection(config.getDBURL(), config.getDBUsername(), config.getDBPassword());

        } catch (SQLException e) {
            logger.error("Connection Failed");
            e.printStackTrace();
        }

        if (connection == null) {
            logger.error("Failed to make connection to database");
        }
        return connection;
    }

    // Close connection
    private void closeConnection(Connection connection, Statement statement, ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();                                     // close connection to the DB
            }
        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        }
        try {
            if (null != statement) {
                statement.close();                              // close statement
            }
        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        }
        try {
            if (null != connection) {
                if (!connection.isClosed()) {
                    connection.close();                         // close connection to the DB
                }
            }
        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        }
    }

    // Get single value
    public String getSingleValue(String query) {

        String value = null;

        try {

            // Open connection
            connection = openConnection();                      // connect to DB
            statement = connection.createStatement();           // function creates an SQL statement for execution
            statement.setMaxRows(1);                            // set maximum row number
            rs = statement.executeQuery(query);                 // executes query

            // Get String value
            while(rs.next()) {
                value = rs.getString(1);
            }

        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        } finally {
            closeConnection(connection, statement, rs);         // close connection
        }
        return value;
    }

    // Get list of values
    @Step("Get result from DB as list")
    public List<String> getList(String query) {

        List<String> result = new ArrayList<>();

        try {

            // Open connection
            connection = openConnection();                      // connect to DB
            Statement statement = connection.createStatement(); // function creates an SQL statement for execution
            rs = statement.executeQuery(query);                 // execute query
            /*
             * Returns a ResultSetMetaData object which holds
             * the description of this ResultSet object's columns
             */
            rsmd = rs.getMetaData();

            int columnsNumber = rsmd.getColumnCount();          // use rsmd to find out how many columns rs has

            // Get all values and add them to the List
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    result.add(rs.getString(i).trim());
                }
            }
        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        } finally {
            closeConnection(connection, statement, rs);         // close connection
        }
        return result;
    }

    // Get values in HashMap
    public HashMap<String, String> getHashMap(String query) {

        HashMap<String, String> resultsHashMap = new HashMap<>();

        try {

            // Open connection
            connection = openConnection();                      // close connection

            Statement statement = connection.createStatement(); // function creates an SQL statement for execution
            rs = statement.executeQuery(query);                 // executes query
            /*
             * Returns a ResultSetMetaData object which holds
             * the description of this ResultSet object's columns
             */
            rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();          // use rsmd to find out how many columns rs has

            // Get all values and add them to the HashMap
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    resultsHashMap.put(rsmd.getColumnName(i).trim(), rs.getString(i).trim());
                }
            }

        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());      // print error message
            logger.error("printTrace \n");
            e.printStackTrace();                                    // print stack trace
        } finally {
            closeConnection(connection, statement, rs);             // close connection
        }
        return resultsHashMap;
    }

    // Print single value
    public void printStringValue(String value) {
        logger.info("\n" + value);
    }

    // Print list of values
    public void printList(List<String> values) {
        logger.info("\n");
        // Iterate list
        for(Object value : values){
            logger.info(value.toString());
        }
    }

    // Print HashMap
    public void printHashMap(HashMap<String, String> values) {
        logger.info("\n");
        // Iterate HashMap
        for (Map.Entry<String, String> entry : values.entrySet()) {
            logger.info("Key: " + entry.getKey() + "; Value: " + entry.getValue());
        }
    }

}
