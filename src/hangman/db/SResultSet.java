
package hangman.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * 
 * ResultSet wrapper
 * When closed also closes associated Statement Object
 *
 */

public class SResultSet implements AutoCloseable {

	
	/**
	 * wrapped ResultSet
	 */
	private ResultSet rs;
	
	
	/**
	 * In what context was the ResultSet generated
	 */
	private Statement stmt;
	
	public SResultSet(Statement stmt, ResultSet rs)
	{
		this.rs = rs;
		this.stmt = stmt;
	}

	/**
	 * must call to dispose
	 * may use in resource try(..) blocks
	 * 
	 * @throws Exception
	 */
	
	@Override
	public void close() throws Exception {
		
		rs.close();
		stmt.close();
	}
	
	/***
	 * wrapper methods, same semantics as result set
	 *
	 */
	
	public int getInt(int index) throws SQLException
	{
		return rs.getInt(index);
	}
	
	public String getString(int index) throws SQLException
	{
		return rs.getString(index);
	}
	
	public int getInt(String columnName) throws SQLException
	{
		return rs.getInt(columnName);
	}
	
	public String getString(String columnName) throws SQLException
	{
		return rs.getString(columnName);
	}
	
	public Object getObject(int index) throws SQLException
	{
		return rs.getObject(index);
	}
	
	public Object getObject(String columnName) throws SQLException 
	{
		return rs.getObject(columnName);
	}
	public boolean next() throws SQLException
	{
		return rs.next();
	}
	
	
}
