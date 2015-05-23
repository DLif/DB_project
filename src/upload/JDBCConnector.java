package upload;

import java.sql.*;

/**
 * JDBC Wrapper to connect to the DB with given credentials
 * 
 */
public class JDBCConnector {
	
	
	public Connection conn; 

	/**
	 * Empty constructor
	 */
	public JDBCConnector() {
		this.conn = null;
	}
	
	/**
	 * 
	 * @return true if the connection was successfully set
	 */
	public boolean openConnection(String host, String port, String schema, String username, String password) {

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
					String.format("jdbc:mysql://%s:%s/%s", host, port, schema), username, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			conn = null;
			return false;
		}
		System.out.println("Connected!");
		return true;
	}

	/**
	 * close the connection
	 */
	public void closeConnection() {
		// closing the connection
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection - " + e.getMessage());
		}

	}

	


}
