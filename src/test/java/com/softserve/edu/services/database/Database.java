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

    // Connect to database using JDBC
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
        String result = null;
        try {
            // Open connection
            connection = openConnection();                      // connect to DB
            statement = connection.createStatement();           // function creates an SQL statement for execution
            statement.setMaxRows(1);                            // set maximum row number
            rs = statement.executeQuery(query);                 // executes query

            // Get String value
            while(rs.next()) {
                result = rs.getString(1);
            }

        } catch (SQLException e) {
            logger.error("Message = " + e.getMessage());
            logger.error("printTrace \n");
            e.printStackTrace();
        } finally {
            closeConnection(connection, statement, rs);         // close connection
        }
        logger.info("Value from DB: {}", result);
        return result;
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
        logger.info("List of values from DB: {}", result);
        return result;
    }

}
