package hangman.db.upload;

import hangman.db.ConnectionPool;
import hangman.db.QueryExecutor;
import hangman.db.upload.AbstractBatchUploader.DBUploaderType;
import hangman.parsing.parsers.ParsedData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


/**
 * This class is responsible to upload all YAGO parsed data to the DB
 * 
 * 
 * NOTES:
 * 	1. all information, except users table, will be DELETED before upload.
 * 	2. Upload is multi-threaded (see DBUpdaeter.connectionsPerTable)
 *  3. use getProgress() and getError() to query the upload process
 * 
 *
 */

public class ParsedDataUploader {
	
	
	/**
	 * progress information
	 */
	private progressInfo progress;
	
	public progressInfo getProgress()
	{
		return progress;
	}
	
	private enum progressInfo
	{
		NO_BEGIN,
		DELTEING_PREV,
		STEP0,
		STEP1,
		STEP2,
		STEP3,
		STEP4,
		STEP5,
		STEP6,
		SERIALIZING,
		COMPLETE;
		
		public String toString()
		{
			switch(this)
			{
			case NO_BEGIN:
				return "Uploading process has not yet began";
			case DELTEING_PREV:
				return "Clearing existing relations";
			
			case STEP0:
			case STEP1:
			case STEP2:
			case STEP3:
			case STEP4:
			case STEP5:
			case STEP6:
				return "Uploading";
			case SERIALIZING:
				return "Serializing";
			case COMPLETE:
				return "Done!";
			default:
				return null;
			}
			
			
		}
		
		/**
		 * get progress percentage
		 * @return
		 */
		public double toPrecentage()
		{
			switch(this)
			{
			case NO_BEGIN:
				return 0;
			case DELTEING_PREV:
				return 0;
		
			case STEP0:
				return 0;
			case STEP1:
				return 2.0/16;
			case STEP2:
				return 7.0/16;
			case STEP3:
				return 10.0/16;
			case STEP4:
				return 13.0/16;
			case STEP5:
				return 15.0/16;
			case STEP6:
				return 1;
			case SERIALIZING:
				return 1;
			case COMPLETE:
				return 1;
			default:
				return 0;
			}
		}
	}
	
	
	/**
	 * begin YAGO update process
	 * 
	 * NOTE: 
	 * 		- all relations will be cleared before upload process (except users table)
	 * 		- in case of any error, an exception will be thrown with the error message
	 * @throws Exception 
	 */
	
	public void begin() throws Exception
	{
		
		
		setStatus(progressInfo.DELTEING_PREV);
		clearExistingRelations();
			
		setStatus(progressInfo.STEP0);
		initiateUpload();
			
	
	
	}
	
	private void setStatus(progressInfo status)
	{
		this.progress = status;
		onProgressChange(status);
		
	}
	

	/**
	 * hook this method to update progress bar
	 * @param progress
	 */
	private void onProgressChange(progressInfo progress)
	{
		switch(progress)
		{
		case NO_BEGIN:
		case DELTEING_PREV:
		case SERIALIZING:
		case COMPLETE:
			System.out.println(progress);
			return;
			
		case STEP0:
		case STEP1:
		case STEP2:
		case STEP3:
		case STEP4:
		case STEP5:
		case STEP6:
			System.out.print(progress);
			System.out.println(": " + progress.toPrecentage());
			return;
			
		default:
			return;
		}
	}
	


/**
 * Upload all entities and all relations, using multiple connections and threads
 * The upload will be divided into 3 groups, each group is responsible for uploading to certain relations
 * each group will use a distinct set of connections (size: one third of the total connection pool size)
 * 
 * throws an exception in case of error
 * @throws Exception
 */
private void initiateUpload() throws Exception
{
	ConnectionPool connectionPool = ConnectionPool.getPool();
	int connectionsPerSequence = ConnectionPool.getPoolSize() / 3;
	
	// connections for each sequence of uploads
	Connection[] firstSeqConnections = connectionPool.getConnections(connectionsPerSequence + (ConnectionPool.getPoolSize() % 3));
	Connection[] secondSeqConnections = connectionPool.getConnections(connectionsPerSequence);
	Connection[] thirdSeqConnections = connectionPool.getConnections(connectionsPerSequence);
	Exception exception = null;
	
	try
	{

		// First sequence: Continents -> AdminDivsions  
		Thread first = new Thread(new MultipleParallelUpload(
				new DBUploaderType[]{ 
						DBUploaderType.Continent,
						DBUploaderType.AdminDivision,

				},
				new String[] {
						"Uploading Continents..",
						"Uploading AdministrativeDivisions (Countries, Cities) ..",

				}, 
				firstSeqConnections
				));


		// Second sequence: Currency -> language -> military action -> battle -> war
		Thread second = new Thread(new MultipleParallelUpload(

				new DBUploaderType[]{
						DBUploaderType.Currency,
						DBUploaderType.Language,
						DBUploaderType.MilitaryAction,
						DBUploaderType.Battle,
						DBUploaderType.War

				},
				new String[]{ 
						"Uploading currencies..",
						"Uploading Languages..",
						"Uploading military actions..",
						"Uploading battles ..",
						"Uploading wars .."

				},
				secondSeqConnections
				));

		// start first two threads
		first.start(); second.start();

		// wait for first thread to finish
		first.join(); setStatus(progressInfo.STEP1);

		// Continue with first sequence
		// country -> city -> capitals
		first = new Thread(new MultipleParallelUpload(
				new DBUploaderType[] {

						DBUploaderType.Country,
						DBUploaderType.City,
						DBUploaderType.CapitalCities,


				},
				new String[]{
						"Uploading countries..",
						"Uploading cities ..",
						"Setting capital cities ..",

				},
				firstSeqConnections
				));


		// A third sequence:
		// construction -> leader -> adminDivisionLeaders
		Thread third = new Thread(new MultipleParallelUpload(
				new DBUploaderType[] {
						DBUploaderType.Construction,
						DBUploaderType.Leader,
						DBUploaderType.AdminDivisionLeaders,


				},
				new String[]{
						"Uploading constructions..",
						"Uploading leaders ..",
						"Uploading AdministrativeDivisionLeaders ..",

				},
				thirdSeqConnections
				));


		// continue with first, start third
		first.start(); third.start();

		// wait for second to finish
		second.join();  setStatus(progressInfo.STEP2);
		second = new Thread(new MultipleParallelUpload(
				new DBUploaderType[] {
						DBUploaderType.MilitaryActionLocations,
						DBUploaderType.MilitaryParticipants,


				},
				new String[]{
						"Uploading MilitaryActionLocations ..",
						"Uploading MilitaryActionParticipants .."

				},
				secondSeqConnections
				));

		second.start();

		// wait for first to finish 
		first.join(); setStatus(progressInfo.STEP3);

		// ( first uploaded countries, second uploaded languages)
		first = new Thread(new MultipleParallelUpload(
				new DBUploaderType[] {
						DBUploaderType.LanguagesInCountries

				},
				new String[]{
						"Uploading LanguagesInCountries ..",
				},
				firstSeqConnections
				));
	
		// wait for all to finish
		third.join();  setStatus(progressInfo.STEP4);
	
		second.join(); setStatus(progressInfo.STEP5);
	
		first.join();  setStatus(progressInfo.COMPLETE);
	}
	catch(Exception e)
	{
		// save exception
		exception = e;
	}
	finally
	{
		// return all connections to the pool
		connectionPool.returnConnections(firstSeqConnections);
		connectionPool.returnConnections(secondSeqConnections);
		connectionPool.returnConnections(thirdSeqConnections);
	}
	
	if(exception != null)
	{
		// exception was thrown
		throw exception;
	}
}
	
	
	/**
	 * delete existing information existing in the DB
	 * (except users table)
	 * @throws Exception - in case of error
	 */
	private void clearExistingRelations() throws Exception
	{
		
		ConnectionPool connectionPool = ConnectionPool.getPool();
		String[] relationNames = {"`AdministrativeDivision`", 
										"`AdministrativeDivisionLeader`",
										"`Battle`",
										"`City`",
										"`Construction`",
										"`Continent`",
										"`Country`",
										"`Currency`",
										"`Language`",
										"`LanguagesInCountries`",
										"`Leader`",
										"`MilitaryAction`",
										"`MilitaryActionLocations`",
										"`MilitaryActionParticipants`",
										"`War`"
		};
		
		Exception exception = null;
		Connection connection = connectionPool.getConnection();
		try
		{
			// disable foreign key constraints for this connection only
	    	// 
			QueryExecutor.execute(connection, "SET FOREIGN_KEY_CHECKS=0");
			for(String name : relationNames)
	    	{
				QueryExecutor.executeUpdate(connection, String.format("DELETE FROM %s", name));
	    	}
			
			// restore constraints
			QueryExecutor.execute(connection, "SET FOREIGN_KEY_CHECKS=1");
	    	 
	
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
	
	
	}
	
	

}
