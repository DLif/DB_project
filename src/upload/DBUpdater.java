package upload;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import upload.DBUploader.DBUploaderType;
import upload.entities.AdminDivisionUploader;
import upload.entities.BattleUploader;
import upload.entities.CapitalCitiesUploader;
import upload.entities.CityUploader;
import upload.entities.ConstructionUploader;
import upload.entities.ContinentUploader;
import upload.entities.CountryUploader;
import upload.entities.CurrencyUploader;
import upload.entities.EntityUploader;
import upload.entities.LanguageUploader;
import upload.entities.LeaderUploader;
import upload.entities.MilitaryActionUploader;
import upload.entities.WarUploader;
import upload.relations.AdminDivisionLeadersUploader;
import upload.relations.LanguagesInCountriesUploader;
import upload.relations.MilitaryActionLocationsUploader;
import upload.relations.MilitaryParticipantsUploader;
import upload.relations.RelationUploader;
import db_parsers.ParsedData;

/**
 * This class is responsible to upload all data parsed to the DB
 * 
 * 
 * NOTES:
 * 	1. all information, except users table, will be DELETED before upload.
 * 	2. 
 * 
 *
 */

public class DBUpdater {
	
	/**
	 * 
	 * Helper class designed to run on a single thread
	 *
	 */
	
	
	
	
	/**
	 * connection configurations
	 */
	
	private String host;
	private String port;
	private String schema;
	private String username;
	private String password;
	private boolean serialize;
	
	
	
	/**
	 * progress information
	 */
	private progressInfo progress;
	
	private enum progressInfo
	{
		NO_BEGIN,
		DELTEING_PREV;
		
		public String toString()
		{
			switch(this)
			{
			case NO_BEGIN:
				return "Uploading process has not yet began";
			case DELTEING_PREV:
				return "Clearing existing relations";
			default:
				return null;
			}
			
			
		}
	}
	
	
	
	
	public DBUpdater(String host, String port, String schema, String username, String password, boolean serialize)
	{
		
		this.host = host;
		this.port = port;
		this.schema = schema;
		this.username = username;
		this.password = password;
		this.serialize = serialize;
		
	}
	
	/**
	 * begin 
	 */
	
	public void begin()
	{
		
		try{
			
			setStatus(progressInfo.DELTEING_PREV);
			if(!clearExistingRelations())  return;
			
			uploadAll();
			
		}
		catch(SQLException ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
	}
	
	private void setStatus(progressInfo status)
	{
		System.out.println(status);
		this.progress = status;
	}
	
	/**
	 * upload data corresponding to relations between entities
	 * e.g. participants in military actions, leaders of administrative divisions, etc.
	 * @param conn - JDBC connection object
	 */
	
	private void uploadRelations(Connection conn)
	{
		
		RelationUploader[] uploaders = {
				new AdminDivisionLeadersUploader(conn, ParsedData.leadersMap.values().iterator()),
				new LanguagesInCountriesUploader(conn, ParsedData.getCountriesSet().iterator()),
				new MilitaryActionLocationsUploader(conn, ParsedData.conflictMap.values().iterator()),
				new MilitaryParticipantsUploader(conn, ParsedData.conflictMap.values().iterator())
		};
		
		String[] messages = {
				"Uploading AdministrativeDivisionLeader ..",
				"Uploading LanguagesInCountries ..",
				"Uploading MilitaryActionLocations ..",
				"Uploading MilitaryActionParticipants .."
		
		};
		
		//Thread[] threads = new Thread[uploaders.length];
		for (int i = 0 ; i < uploaders.length; ++i){
			System.out.println(messages[i]);
			if(uploaders[i].upload())
			{
				System.out.println(" .. OK!");
			}
			else
			{
				return;
			}
		}
		
		
	}
	

	private void uploadRelations2() throws InterruptedException
	{
		
		JDBCConnector [] connectors = new JDBCConnector[4];
		for (int i = 0; i < connectors.length; ++i)
		{
			connectors[i] = new JDBCConnector();
			if (!connectors[i].openConnection(host, port, schema, username, password, false))
				return;
		}
		
		RelationUploader[] uploaders = {
				new LanguagesInCountriesUploader(connectors[1].getConnection(), ParsedData.getCountriesSet().iterator()),
				new MilitaryActionLocationsUploader(connectors[2].getConnection(), ParsedData.conflictMap.values().iterator()),
				new MilitaryParticipantsUploader(connectors[3].getConnection(), ParsedData.conflictMap.values().iterator())
		};
		
		String[] messages = {
				
				
		
		};
		// run each relation upload in a different thread
		Thread[] threads = new Thread[uploaders.length];
		for (int i = 0 ; i < uploaders.length; ++i){
			System.out.println(messages[i]);
			threads[i] = new Thread(uploaders[i]);
			threads[i].start();
		}
		
		// wait for all to finish
		for (Thread t : threads)
		{
			t.join();
		}
		
		
	}
	
	
	
	



/**
 * Upload all entities, using multiple connections and threads
 * 
 * @throws InterruptedException
 */
private void uploadEntites3() throws InterruptedException
{
	

	
	JDBCConnector [] innerConnectors = new JDBCConnector[5];
	Connection[] innerConnections = new Connection[5];
	
	JDBCConnector [] innerConnectors2 = new JDBCConnector[5];
	Connection[] innerConnections2 = new Connection[5];
	
	JDBCConnector [] innerConnectors3 = new JDBCConnector[5];
	Connection[] innerConnections3 = new Connection[5];
	
	
	for (int i = 0; i < innerConnectors.length; ++i)
	{
		innerConnectors[i] = new JDBCConnector();
		if (!innerConnectors[i].openConnection(host, port, schema, username, password, false))
			return;
		
		innerConnectors2[i] = new JDBCConnector();
		if (!innerConnectors2[i].openConnection(host, port, schema, username, password, false))
			return;
		
		innerConnectors3[i] = new JDBCConnector();
		if (!innerConnectors3[i].openConnection(host, port, schema, username, password, false))
			return;
		
		
		innerConnections[i] = innerConnectors[i].getConnection();
		innerConnections2[i] = innerConnectors2[i].getConnection();
		innerConnections3[i] = innerConnectors3[i].getConnection();
	}
	
	
	// Continents - AdminDivsions - Constructions
	Thread first = new Thread(new MultipleParallelUpload(
			new DBUploaderType[]{ 
					DBUploaderType.Continent,
					DBUploaderType.AdminDivision,
					

			},
			new String[] {
					"Uploading Continents..",
					"Uploading AdministrativeDivisions (Countries, Cities) ..",
					
			
			}, innerConnections));
	
	//first.start();
	//first.join();
	
	// Currency - language - military action - battle - war
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
			innerConnections2
	));
	
	// start first two threads
	first.start();
	second.start();
	
	
	// wait for first thread to finish
	first.join();
	
	// after first
	// leader, country, cities and capitals
	Thread third = new Thread(new MultipleParallelUpload(
			new DBUploaderType[] {

					DBUploaderType.Country,
					DBUploaderType.City,
					DBUploaderType.CapitalCities,
					DBUploaderType.LanguagesInCountries
			
			},
			new String[]{
					"Uploading countries..",
					"Uploading cities ..",
					"Setting capital cities ..",
					"Uploading LanguagesInCountries ..",
			},
			innerConnections
			));
	
	
			Thread fourth = new Thread(new MultipleParallelUpload(
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
			innerConnections3
			));
			
			
	
	third.start();
	fourth.start();
			
	// wait for second to finish
	second.join();
	Thread fifth = new Thread(new MultipleParallelUpload(
			new DBUploaderType[] {
					DBUploaderType.MilitaryActionLocations,
					DBUploaderType.MilitaryParticipants,
					
			
			},
			new String[]{
					"Uploading MilitaryActionLocations ..",
					"Uploading MilitaryActionParticipants .."
				
			},
			innerConnections2
			));
	
	fifth.start();
	
	// wait for all to finish
	third.join(); fourth.join(); fifth.join();
	
}

	
/*	
	*//**
	 * Upload all entities, using multiple connections and threads
	 * 
	 * @throws InterruptedException
	 *//*
	private void uploadEntites2() throws InterruptedException
	{
		

		JDBCConnector [] connectors = new JDBCConnector[3];
		for (int i = 0; i < connectors.length; ++i)
		{
			connectors[i] = new JDBCConnector();
			if (!connectors[i].openConnection(host, port, schema, username, password, false))
				return;
		}
		
		
		// Continents - AdminDivsions - Constructions
		Thread first = new Thread(new MultipleUpload(
				new EntityUploader[]{ 
						new ContinentUploader(connectors[0].getConnection(), ParsedData.continentsMap.values().iterator()),
						new AdminDivisionUploader(connectors[0].getConnection(),  ParsedData.locationsMap.values().iterator()),
						new ConstructionUploader(connectors[0].getConnection(), ParsedData.constructionsMap.values().iterator()),

				},
				new String[] {
						"Uploading Continents..",
						"Uploading AdministrativeDivisions (Countries, Cities) ..",
						"Uploading constructions..",
				
				}));
		
		// Currency - language - military action - battle - war
		Thread second = new Thread(new MultipleUpload(
		
				new EntityUploader[]{
						new CurrencyUploader(connectors[1].getConnection(), ParsedData.currenciesMap.values().iterator()),
						new LanguageUploader(connectors[1].getConnection(), ParsedData.langugagesMap.values().iterator()),
						new MilitaryActionUploader(connectors[1].getConnection(), ParsedData.conflictMap.values().iterator()),
						new BattleUploader(connectors[1].getConnection(), ParsedData.getBattleSet().iterator()),
						new WarUploader(connectors[1].getConnection(), ParsedData.getWarsSet().iterator())
				
				},
				new String[]{ 
						"Uploading currencies..",
						"Uploading Languages..",
						"Uploading military actions..",
						"Uploading battles ..",
						"Uploading wars .."

				}
		));
		
		// start first two threads
		first.start();
		second.start();
		
		
		// wait for first thread to finish
		first.join();
		
		// after first
		// leader, country, cities and capitals
		Thread third = new Thread(new MultipleUpload(
				new EntityUploader[] {
				new LeaderUploader(connectors[0].getConnection(), ParsedData.leadersMap.values().iterator()),
				new CountryUploader(connectors[0].getConnection(), ParsedData.getCountriesSet().iterator()),
				new CityUploader(connectors[2].getConnection(), ParsedData.getCitiesSet().iterator()),
				new CapitalCitiesUploader(connectors[2].getConnection(), ParsedData.getCountriesSet().iterator())
				
				},
				new String[]{
						"Uploading leaders ..",
						"Uploading countries..",
						"Uploading cities ..",
						"Setting capital cities ..",
				}
				));
		
		
		third.start();
		
		// wait for all to finish
		second.join(); third.join();
		
	}
	*/
	
	/**
	 * upload data corresponding to entities such as War, Country, etc.
	 * @param conn - JDBC connection object
	 */
	
	private void uploadEntities(Connection conn)
	{
		final int serializeAfterIndex = 6;
		EntityUploader[] uploaders = { 
				new ContinentUploader(conn, ParsedData.continentsMap.values().iterator()),
		 		new AdminDivisionUploader(conn,  ParsedData.locationsMap.values().iterator()),
		 		new CurrencyUploader(conn, ParsedData.currenciesMap.values().iterator()),
		 		new LanguageUploader(conn, ParsedData.langugagesMap.values().iterator()),
		 		new ConstructionUploader(conn, ParsedData.constructionsMap.values().iterator()),
		 		new MilitaryActionUploader(conn, ParsedData.conflictMap.values().iterator()),
				new LeaderUploader(conn, ParsedData.leadersMap.values().iterator()),
				new CountryUploader(conn, ParsedData.getCountriesSet().iterator()),
				new CityUploader(conn, ParsedData.getCitiesSet().iterator()),
				new CapitalCitiesUploader(conn, ParsedData.getCountriesSet().iterator()),
				new BattleUploader(conn, ParsedData.getBattleSet().iterator()),
				new WarUploader(conn, ParsedData.getWarsSet().iterator())
		};
		String[] messages = {
				"Uploading Continents..",
				"Uploading AdministrativeDivisions (Countries, Cities) ..",
				"Uploading currencies..",
				"Uploading Languages..",
				"Uploading constructions..",
				"Uploading military actions..",
				"Uploading leaders ..",
				"Uploading countries..",
				"Uploading cities ..",
				"Setting capital cities ..",
				"Uploading battles ..",
				"Uploading wars .."
			
		};
		
		for (int i = 0 ; i < uploaders.length; ++i){
			System.out.println(messages[i]);
			if(uploaders[i].upload())
			{
				System.out.println(" .. OK!");
			}
			else
			{
				return;
			}
			
			if(i == serializeAfterIndex && serialize)
			{
				// First set of uploaders set all the Entity IDs as received from the DB
				// At this point we may serialize all the information we have in memory
	 			ParsedData.serializeMaps();
			}
		}
		
	}
	
	
	private void uploadAll()
	{

		JDBCConnector connector = new JDBCConnector();
	
		// connecting
		if (!connector.openConnection(host, port, schema, username, password, true))
			return;
		
		System.out.println("Uploading entities..");
		try {
			uploadEntites3();
		//	uploadRelations2();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//uploadEntities(connector.getConnection());
		
		
		
		//System.out.println("Uploading relations between entities..");
		//uploadRelations(connector.getConnection());
		
		//System.out.println("Done!");
		// close connection
		connector.closeConnection();
	}
	
	
	/**
	 * delete existing information existing in the DB
	 * (except users table)
	 * @throws SQLException 
	 * @return true on success
	 */
	private boolean clearExistingRelations() throws SQLException
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
			return false;
		}
		
	     try(Statement stmt = connector.getConnection().createStatement())
	     {
	    	 // disable foreign key constraints for this connection only
	    	 // 
	    	 stmt.execute("SET FOREIGN_KEY_CHECKS=0");
	    	 
	    	 for(String name : relationNames)
	    	 {
	    		 int res = stmt.executeUpdate(String.format("DELETE FROM %s", name));
	    		 System.out.println("removed " + res + " rows from " + name);
	    	 }
	   
	     }
	     catch(SQLException ex)
	     {
	    	 connector.closeConnection();
	    	 throw ex;
	     }
		
		// close connection
		connector.closeConnection();
		return true;
	
	}
	
	

}
