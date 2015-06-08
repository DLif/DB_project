package upload;

import java.sql.*;

/**
 * JDBC Wrapper to connect to the DB with given credentials
 * 
 */
public class JDBCConnector {
	
	/**
	 * load the JDBC driver
	 * @return true on success
	 */
	private static boolean registerJDBC()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver..");
			return false;
		}
		System.out.println("Driver loaded successfully");
		
		driverLoaded = true;
		return true;
	}
	
	private static boolean driverLoaded = false;
	
	private Connection conn; 

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
	public boolean openConnection(String host, String port, String schema, String username, String password, boolean verbose) {

		if(!driverLoaded)
		{
			if(!registerJDBC())
			{
				return false;
			}
		}
		
		// creating the connection
		
		if(verbose) System.out.print("Trying to connect... ");
		try {
			conn = DriverManager.getConnection(
					String.format("jdbc:mysql://%s:%s/%s", host, port, schema), username, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			conn = null;
			return false;
		}
		if(verbose) System.out.println("Connected!");
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

	/**
	 * Attempts to set the connection back to/from auto-commit, ignoring errors.
	 */
	public void safelySetAutoCommit(boolean auto_commit) {
		try {
			conn.setAutoCommit(auto_commit);
		} catch (Exception e) {
		}
	}

	/**
	 * Attempts to rollback, ignoring errors.
	 */

	public void safelyRollBack()
	{
		try {
			conn.rollback();
		}
		catch(Exception e)
		{
			System.out.println("Failed to rollback!");
		}
	}
	
	public Connection getConnection()
	{
		return this.conn;
	}

	

}
