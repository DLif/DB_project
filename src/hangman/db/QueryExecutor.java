package hangman.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import hangman.core.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	 * the used connection is always returned to the pool
	 * 
	 * @param query
	 * @return the result set - remember to close it 
	 * @throws Exception 
	 */
	
	public static SResultSet executeQuery(String query) throws Exception
	{
		SResultSet result = null;
		Exception exception = null;
		Connection connection = getConnection();
		try
		{
			result = executeQuery(connection, query);
		}
		catch( Exception e)
		{
			exception = e;
		}
		finally
		{
			connectionPool.returnConnection(connection);
		}
		
		if(exception != null)
		{
			if(result != null) result.close();
			throw exception;
		}
		return result;
	}
	
	/**
	 * execute query wrapper with a possibility to provide a connection
	 * @param connection
	 * @param query
	 * @return the result set - remember to close it
	 * @throws SQLException
	 */
	
	public static SResultSet executeQuery(Connection connection, String query) throws SQLException
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
	
	public static boolean checkLogin(String username , String password) throws Exception{
		boolean retVal=false;
		try {


			SResultSet rs =executeQuery("SELECT * "
												+ "FROM User "
												+ "WHERE userName=\""+username +"\" AND password=\""+password+"\"");
			if (rs.next()!=false){

				retVal=true;
			}
												
		} catch (Exception e) {
			throw e;
		}
		return retVal;
		
	}
	
	public static boolean register(String username , String password) throws Exception{
		
		boolean retVal=true;
		try {
			SResultSet rs = QueryExecutor.executeQuery("SELECT * "
												+ "FROM User "
												+ "WHERE userName=\""+username +"\"");
			if (rs.next()!=false){
				retVal=false;
			}
			else {
				executeUpdate("INSERT into User(userName,highScore,highScoreDate, password)"
									+ "  VALUES( \""+username+"\",0,Null,\""+password+"\")");
			}
														

		} catch (Exception e) {
			throw e;
		}

		

		return retVal;
		
	}
	
	public static Map<Integer,String[]> getTopHighScores() throws Exception{
		HashMap<Integer, String[]> retVal=new HashMap<Integer,String[]>();
		try {


			SResultSet rs = QueryExecutor.executeQuery("SELECT userName , highScore  "
											+ "FROM User "
											+ "ORDER BY highScore DESC");

			
			for (int i=1; i<=5 && rs.next() == true ;i++) {
				retVal.put(i,new String[]{rs.getString("userName"),rs.getString("highScore")});
			}
			for(int i=retVal.size()+1;i<=5;i++)
				retVal.put(i,new String[]{"---","---"});
				
												
		} catch (Exception e) {
			throw e;
		}

		return retVal;
		
	}
	
	public static int getHighScoresByUser(String username) throws Exception{
		int retVal=-1;
		try {

			SResultSet rs = QueryExecutor.executeQuery("SELECT highScore  "
											+ "FROM User "
											+ "WHERE userName=\""+username+"\"");
			if(rs.next()!=false)
				retVal=rs.getInt("highScore");
														
		} catch (Exception e) {
			throw e;
		}
		return retVal;
		
	}
	
	public static boolean updateScore(String username , int score) throws Exception{
		boolean retVal=true;
		try {
			executeUpdate("UPDATE User "
								+ "SET highScore="+score
								+ " WHERE userName=\""+username +"\" AND highScore<"+score);

												
		} catch (Exception e) {
			throw e;
		}
		return retVal;
		
	}
	
	
	
	public static List<Clue> getClueListByType(ClueFormat type , int locationId) throws Exception{
		String query="";

		if(type.numArgs==1)query=String.format(type.sqlQ,locationId);
		else if(type.numArgs==2)query=String.format(type.sqlQ,locationId,locationId);
		List<Clue> retVal=null;
		try {

			SResultSet rs = QueryExecutor.executeQuery(query);
			
			retVal=ClueFactory.buildClueList(rs,type,locationId);

												
		} catch (Exception e) {
			throw e;
		}		
		return retVal;
	}
	
	public static List<String> getOptions(String query) throws Exception {

		List<String> retVal=new ArrayList<String>();
		try {

			SResultSet rs = QueryExecutor.executeQuery(query);
			
			while(rs.next())
				retVal.add(rs.getString("op"));

												
		} catch (Exception e) {
			throw e;
		}	
		return retVal;
	}
	


	




}