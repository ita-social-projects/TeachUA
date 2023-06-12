package com.example.pages;

import java.sql.*;

public class AppDB
{
    private static Connection con = null;
    private static String username = "postgres";
    private static String password = "root";
    private static String URL = "jdbc:postgresql://localhost:5432/test2";
    //private static String URL = "jdbc:postgresql://localhost:5432/contacts";

    public static void main( String[] args ) throws SQLException {
        System.out.println("Start...");
        // From Eclipse/Idea
        System.out.println("***System.getenv().MY_PASSWORD = " + System.getenv().get("MY_PASSWORD"));
        username = System.getenv().get("TEACHUA_USERNAME");
        password = System.getenv().get("TEACHUA_PASSWORD");
        URL= System.getenv().get("TEACHUA_URL");
        //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        //DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        con = DriverManager.getConnection(URL, username, password);
        if (con != null) {
            System.out.println("Connection Successful! \n");
        } else {
            System.out.println("Connection ERROR \n");
            System.exit(1);
        }
        Statement st = con.createStatement();
        //
		/* Create Database
		st.execute("CREATE DATABASE test2;");
		*/
        //
		/* Create Table
		//st.execute("USE test2;");
		//
		// MySQL
//		String query = "CREATE TABLE temp "
//			+ "( id int unsigned not null auto_increment primary key, "
//			+ "name varchar(20), "
//			+ "login varchar(20), "
//			+ "password varchar(30), "
//			+ "age int );";
		//
		// PostGreSQL
		String query = "CREATE TABLE public.temp"
		+ "( id integer NOT NULL,"
    	+ "name character varying(255),"
    	+ "login character varying(255),"
    	+ "password character varying(255),"
    	+ "age integer,"
    	+ "CONSTRAINT id_key PRIMARY KEY (id),"
    	+ "CONSTRAINT uniq UNIQUE (id));";
    	//nextval('library.authors_id_seq'::regclass)
//		String query = "CREATE TABLE temp "
//			+ "( id int not null primary key, "
//			+ "name varchar(20), "
//			+ "login varchar(20), "
//			+ "password varchar(30), "
//			+ "age int );";
		//
		st.execute(query);
		*/
        //
        /* Insert Data
        //
        // MySQL
//        String query = "INSERT INTO temp (name,login,password,age) VALUES ('Ivan4','iva4','qwerty',21);";
//		String query = "INSERT INTO temp (name,login,password,age) VALUES ('Petro4','pet3','1234562',22);";
        //
        // PostGreSQL
        //String query = "INSERT INTO temp (id,name,login,password,age) VALUES (2,'Ivan','iva','qwerty',21);";
        //String query = "INSERT INTO temp (id,name,login,password,age) VALUES (1,'Petro','pet','123456',22);";
        //String query = "INSERT INTO temp (id,name,login,password,age) VALUES (3,'Stepan3','step','123456',23);";
        String query = "INSERT INTO temp (id,name,login,password,age) VALUES (4, 'Petro2','pet2','123456',22);";
        //
        //con.setAutoCommit(false);
        st.execute(query);
        //con.commit();
        //System.out.println("transaction rollback ...");
        //Thread.sleep(2000);
        //con.rollback();
        */
        //
        // /* Update Data
        //
        //st.executeUpdate("UPDATE temp SET name='Ira' WHERE id=1;");
        //st.execute("UPDATE temp SET name='Tolik' WHERE login LIKE 'st%';");
        // */
        //
		/* Delete Data
		//
		boolean res = st.execute("DELETE FROM temp WHERE name='Tolik';");
		//boolean res = st.execute("DELETE FROM temp WHERE name='Ira';");
		System.out.println("res = " + res);
		*/
        //
        // /* Read Data
        //
        //st.execute("USE lv696;");
        //ResultSet rs = st.executeQuery("select * from temp;");
        ResultSet rs = st.executeQuery("select * from public.categories order by id;");
        //
//		ResultSet rs = null;
//		boolean res = st.execute("select * from temp;");
//		System.out.println("res = " + res);
//		if (res) {
//			rs = st.getResultSet();
//		}
        //
        int columnCount = rs.getMetaData().getColumnCount();
        // Resultset.getMetaData() get the information
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rs.getMetaData().getColumnName(i) + "\t");
            //System.out.print(rs.getMetaData().getColumnLabel(i) + "\t");
        }
        System.out.println();
        //
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
        System.out.println("before close");
        if (rs != null) {
            rs.close();
        }
        //
        //con.commit(); // Close transaction
        // */
        if (st != null) {
            st.close();
        }
        if (con != null) {
            con.close();
        }
        System.out.println("DONE");
    }
}

