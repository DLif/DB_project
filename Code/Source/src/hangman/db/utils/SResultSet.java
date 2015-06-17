
package hangman.db.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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
	
	/**
	 * In what connection context was the ResultSet generated
	 */
	
	private Connection conn;
	
	/**
	 * use this constructor in case connection should be returned to pool
	 * at the end of usage
	 * 
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	
	public SResultSet(Connection conn, Statement stmt, ResultSet rs) 
	{
		this(stmt, rs);
		this.conn = conn;
	}
	
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
		
		// return connection to pool
		if(conn != null)
		{
			ConnectionPool.getPool().returnConnection(conn);
			conn = null;
		}
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
	
	/**
	 * Return all values in columnIndex (starting from 1) in one list of type String
	 * 
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	
	public List<String> toStringList(int columnIndex) throws SQLException
	{
		List<String> retVal=new ArrayList<String>();
		while(this.next())
				retVal.add(rs.getString(columnIndex));
		return retVal;
	}
	
}
