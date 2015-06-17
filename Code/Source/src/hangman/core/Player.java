package hangman.core;

import hangman.db.utils.QueryExecutor;
import hangman.db.utils.SResultSet;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author gilifaro
 * 
 * Player related DB queries (register, login, etc..)
 * 
 */
public class Player {
	/**
	 * 
	 * Returns true iff the given user is registered (i.e. existing in the DB)
	 * 
	 * @param username - the username 
	 * @param password - the password of this user
	 * @return True iff this user exist in the DB
	 * @throws Exception
	 */
	public static boolean checkLogin(String username , String password) throws Exception{
		
		try(SResultSet rs =QueryExecutor.executeQuery("SELECT * "
												+ "FROM User "
												+ "WHERE userName=\""+username +"\" AND password=\""+password+"\"");) {

			if(rs.next()) return true;
			else          return false;
												
		} catch (Exception e) {
			throw e;
		}
		
		
	}
	/**
	 * 
	 * Method registers a new user
	 * The underlying assumption is that a user with given username does not exist in the DB
	 * 
	 * @param username
	 * @param password
	 * @return True iff its a new user and the register to the DB was successfully done
	 * @throws Exception
	 */
	public static boolean register(String username , String password) throws Exception{
	
		try(SResultSet rs = QueryExecutor.executeQuery("SELECT * "
												+ "FROM User "
												+ "WHERE userName=\""+username +"\"");) {
			
			if (rs.next())
				// user exists
				return false;
			
			
			if(QueryExecutor.executeUpdate("INSERT into User(userName, password)"
									+ "  VALUES( \""+username+"\",\""+password+"\")") > 0)
			{
				// affected more than 0 rows (i.e successful addition)
				return true;
			}
			return false;
			
		
		} catch (Exception e) {
			throw e;
		}

		
	}
	
	/**
	 * Get top 5 high-scores
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public static Map<Integer,String[]> getTopHighScores() throws Exception{
		HashMap<Integer, String[]> retVal=new HashMap<Integer,String[]>();
		
		try(SResultSet rs = QueryExecutor.executeQuery("SELECT userName , highScore  "
											+ "FROM User "
											+ "ORDER BY highScore DESC");) {

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
	
	/**
	 * get given user high-score
	 * @param username
	 * @return
	 * @throws Exception
	 */
	
	public static int getHighScoresByUser(String username) throws Exception{
		int retVal=-1;
		try(SResultSet rs = QueryExecutor.executeQuery("SELECT highScore  "
				+ "FROM User "
				+ "WHERE userName=\""+username+"\""); ) {

			if(rs.next())
				retVal=rs.getInt("highScore");
														
		} catch (Exception e) {
			throw e;
		}
		return retVal;
		
	}
	
	/**
	 * set new user high-score (if given score is higher than current high-score)
	 * @param username
	 * @param score
	 * @return true on successful update
	 * @throws Exception
	 */
	
	public static boolean updateScore(String username , int score) throws Exception{
		
		try {
			QueryExecutor.executeUpdate("UPDATE User "
								+ "SET highScore="+score
								+ " WHERE userName=\""+username +"\" AND highScore<"+score);
			
												
		} catch (Exception e) {
			throw e;
		}
		return true;
	
		
	}

}
