package com.softserve.edu.services.database;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.qameta.allure.Step;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.*;

public class Database {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;
    private static final ConfigPropertiesReader config = new ConfigPropertiesReader();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());           // logger
//    private final String CONFIGURATION_FILE_NAME = "hibernate.cfg.xml"; // hibernate configuration file name
//    private Session session = null;
//
//    // Configure and open session
//    private Session openSession() {
//        // Refer the hibernate.cfg.xml configuration file
//        Configuration configuration = new Configuration().configure(CONFIGURATION_FILE_NAME);
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties());
//
//        // SessionFactory will contain all the database property details which are pulled from above hibernate.cfg.xml file
//        // As application refers the database, it is required. It is a threadsafe object
//        SessionFactory factory = configuration.buildSessionFactory(builder.build());
//
//        // Get a physical connection
//        session = factory.openSession();
//
//        return session;
//    }
//
//    // Clear and close session
//    private void closeSession(Session session) {
//        if (session != null) {
//                if (session.isConnected()) {
//                    session.clear();                                    // clear connection
//                    session.close();                                    // close connection to the DB
//                }
//            }
//    }
//
//    // Get single value
//    private String getSingleValue(String query) {
//        session = openSession();                                        // connect to DB
//        Query result = session.createNativeQuery(query);                // execute query
//        String value = String.valueOf(result.getSingleResult());        // get single value
//        closeSession(session);                                          // clear and close session
//        return value;
//    }
//
//    // Get list of values
//    private List<String> getList(String query) {
//        session = openSession();                                        // connect to DB
//        Query result = session.createNativeQuery(query);                // execute query
//        List<String> resultList = result.list();                        // get result list
//        closeSession(session);                                          // clear and close session
//        return resultList;
//    }
//
//    // Get values in HashMap
//    public HashMap<String, String> getHashMap(String query) {
//        session = openSession();                                        // connect to DB
//        Query result = session.createNativeQuery(query);                // execute query
//        result.getResultStream();
//        HashMap<String, String> resultsHashMap = new HashMap<>();
//        closeSession(session);                                          // clear and close session
//        return resultsHashMap;
//    }
//
//    // Print single value
//    public void printStringValue(String value) {
//        System.out.println("\n" + value);
//    }
//
//    // Print list of values
//    public void printList(List<String> values) {
//        System.out.println();
//        // Iterate list
//        for(Object value : values){
//            System.out.println(value);
//        }
//    }
//
//    // Print HashMap
//    public void printHashMap(HashMap<String, String> values) {
//        System.out.println();
//        // Iterate HashMap
//        for (Map.Entry<String, String> entry : values.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + "; Value: " + entry.getValue());
//        }
//    }

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
            Collections.sort(result);
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
