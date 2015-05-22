package db_communication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_communication {
	
	private Connection conn; // DB connection

	/**
	 * Empty constructor
	 */
	public JDBC_communication() {
		this.conn = null;
	}

	/**
	 * 
	 * @return true if the connection was successfully set
	 */
	public boolean openConnection() {

		// loading the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver..");
			return false;
		}
		System.out.println("Driver loaded successfully");

		// creating the connection
		System.out.print("Trying to connect... ");
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3305/DbMysql03", "DbMysql03",
					"DbMysql03");
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			conn = null;
			return false;
		}
		System.out.println("Connected!");
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * close the connection
	 */
	public void closeConnection() {
		// closing the connection
		try {
			conn.close();
			System.out.println("connection closed");
		} catch (SQLException e) {
			System.out.println("Unable to close the connection - "
					+ e.getMessage());
		}

	}

	public Connection getConn() {
		return conn;
	}

}
