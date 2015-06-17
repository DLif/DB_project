package hangman.db.utils;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides method for executing queries conveniently
 * This class uses the connection pool as a source for connections
 * 
 *
 */

public class QueryExecutor {

	private static ConnectionPool connectionPool; 
	private static Connection getConnection() throws Exception
	{
		if(connectionPool == null)
		{
			// get the singleton
			connectionPool = ConnectionPool.getPool();
		}
		
		return connectionPool.getConnection();
	}
	
	/**
	 * execute update wrapper
	 * The connection is taken from the global connection pool
	 * The connection is always returned to the pool (regardless to whether an exception was thrown)
	 * 
	 * @param query - the query to execute
	 * @return number of affected rows
	 * @throws Exception
	 */
	
	public static int executeUpdate(String query) throws Exception
	{
		Connection connection = getConnection();
		Exception exception  = null;
		int result = 0;
		try{
			result = executeUpdate(connection, query);
		}
		catch(Exception e)
		{
			exception = e;
		}
		finally
		{
			connectionPool.returnConnection(connection);
		}
		
		if(exception != null)
		{
			throw exception;
		}
		return result;
	}
	
	/**
	 * execute-update wrapper, with a possibility to provide a connection
	 * 
	 * @param connection
	 * @param query
	 * @return
	 * @throws SQLException 
	 */
	public static int executeUpdate(Connection connection, String query) throws SQLException
	{
		  try(Statement stmt = connection.createStatement())
		  {
		    	return stmt.executeUpdate(query);
		  }
	}
	
	/**
	 * execute query wrapper
	 * connection will be taken from global pool
	 * in case of an exception, all allocated resources WILL be closed
	 * ( connection will be returned to the pool)
	 * 
	 * NOTE That closing the SResultSet will close the assosicated ResultSet and Statement objects
	 * and will also return the associated connection to the connection pool
	 * 
	 * @param query
	 * @return the result set - remember to close it 
	 * @throws Exception 
	 */
	
	public static SResultSet executeQuery(String query) throws Exception
	{
		SResultSet result = null;

		Connection connection = getConnection();
		try
		{
			result = executeQuery(connection, query, true);
		}
		catch( Exception e)
		{
			// return connection to pool
			connectionPool.returnConnection(connection);
			throw e;
		}
	
		return result;
	}
	
	/**
	 * execute query wrapper with a possibility to provide a connection
	 * @param connection
	 * @param query
	 * @param returnConnection - should the connection be returned to the pool
	 *                           when the object is closed
	 *                           
	 * @return the result set - remember to close it
	 * @throws SQLException
	 */
	
	public static SResultSet executeQuery(Connection connection, String query, boolean returnConnection) throws SQLException
	{
		Statement stmt = null;
		ResultSet result = null;
		
		try
		{
			 stmt = connection.createStatement();
		     result =  stmt.executeQuery(query);
		}
		catch(Exception e)
		{
			// close resources and rethrow
			
			if( result != null) result.close();
			if( stmt != null)   stmt.close();
			
			throw e;
		}
		
		if(returnConnection)
			return new SResultSet(connection, stmt, result);
		
		return new SResultSet(stmt, result);
		
	}
	
	/**
	 * simple execute wrapper with resource deallocation
	 * @param connection
	 * @param query
	 * @return the boolean returned from execute() method
	 * @throws SQLException
	 */
	
	public static boolean execute(Connection connection, String query) throws SQLException
	{
		 try(Statement stmt = connection.createStatement())
		  {
		    	return stmt.execute(query);
		  }
	}
	

	
	
	
	
	


	




}