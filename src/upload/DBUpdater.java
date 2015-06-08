package upload;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import upload.DBUploader.DBUploaderType;
import db_parsers.ParsedData;

/**
 * This class is responsible to upload all data parsed to the DB
 * 
 * 
 * NOTES:
 * 	1. all information, except users table, will be DELETED before upload.
 * 	2. Upload is multi-threaded (see DBUpdaeter.connectionsPerTable)
 *  3. use getProgress() and getError() to query the upload process
 * 
 *
 */

public class DBUpdater {
	
	 
	
	 /**
	  * Updater configurations
	  */
	
	/**
	 * DB connection details
	 */
	private static String host;
	private static String port;
	private static String schema;
	private static String username;
	private static String password;
	
	/**
	 * serialize after upload process is complete
	 */
	private static boolean serialize;
	
	/**
	 * how many connections should be allocated, at most, to upload a single table
	 * for example, value 5 means uploading a relation will be split, at most, between 5 connections
	 * (each connection will upload a single batch from the table)
	 */
	private static int connectionsPerTable;
	
	
	public static void configure(String host,
			String port,
			String schema,
			String username,
			String password,
			boolean serialize,
			int connectionsPerTable)
	{
		DBUpdater.host = host;
		DBUpdater.port = port;
		DBUpdater.schema = schema;
		DBUpdater.username = username;
		DBUpdater.password = password;
		DBUpdater.serialize = serialize;
		DBUpdater.connectionsPerTable = connectionsPerTable;
	}
	
	
	/**
	 * in case of an error, will hold an error message
	 */
	private String errorMsg;
	
	public String getError()
	{
		return errorMsg;
	}
	
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
		 * get progress precentage
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
 * each group will use a distinct set of connections (size: connectionsPerTable)
 * 
 * throws an exception in case of error
 * @throws Exception
 */
private void initiateUpload() throws Exception
{
	
	Connection[][] connections = new Connection[3][connectionsPerTable];
	Set<JDBCConnector> openConnectors = new HashSet<JDBCConnector>();
	
	// open all required connections
	for (int i = 0; i < connections.length; ++i)
	{	
		for(int j = 0; j < connections[i].length; ++j){
			JDBCConnector connector = new JDBCConnector();
			if (!connector.openConnection(host, port, schema, username, password, false))
			{
				// close all open connections
				for(JDBCConnector openConnector : openConnectors)
				{
					openConnector.closeConnection();
				}
	
				throw new Exception( "Failed to open connection # " + (i+1)*(j+1));
				
			}
			else
			{
				openConnectors.add(connector);
				connections[i][j] = connector.getConnection();
			}
		}
	
	}
	
	
	// First sequence: Continents -> AdminDivsions  
	Thread first = new Thread(new MultipleParallelUpload(
			new DBUploaderType[]{ 
					DBUploaderType.Continent,
					DBUploaderType.AdminDivision,
	
			},
			new String[] {
					"Uploading Continents..",
					"Uploading AdministrativeDivisions (Countries, Cities) ..",
				
			}, connections[0]
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
			connections[1]
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
			connections[0]
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
			connections[2]
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
			connections[1]
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
			connections[0]
	));
	
	// wait for all to finish
	third.join();  setStatus(progressInfo.STEP4);
	
	second.join(); setStatus(progressInfo.STEP5);
	
	first.join();  setStatus(progressInfo.STEP6);
	
	if(serialize)
	{
		 // now that all IDs are set, serialize information
		ParsedData.serializeMaps();
	}
	
	first.join();  setStatus(progressInfo.COMPLETE);
}
	
	
	/**
	 * delete existing information existing in the DB
	 * (except users table)
	 * @throws Exception - in case of error
	 */
	private void clearExistingRelations() throws Exception
	{
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
		
		JDBCConnector connector;
		
		connector = new JDBCConnector();
		
		// connecting
		if (!connector.openConnection(host, port, schema, username, password, false))
		{
			throw new Exception("Could not delete existing records: failed to connect");
		}
		
	     try(Statement stmt = connector.getConnection().createStatement())
	     {
	    	 // disable foreign key constraints for this connection only
	    	 // 
	    	 stmt.execute("SET FOREIGN_KEY_CHECKS=0");
	    	 
	    	 for(String name : relationNames)
	    	 {
	    		 stmt.executeUpdate(String.format("DELETE FROM %s", name));
	    		// System.out.println("removed " + res + " rows from " + name);
	    	 }
	   
	     }
	     catch(SQLException ex)
	     {
	    	 connector.closeConnection();
	    	 throw ex;
	     }
		
		// close connection
		connector.closeConnection();
	
	}
	
	

}
