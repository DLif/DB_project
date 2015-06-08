package upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;


import upload.entities.AdminDivisionUploader;
import upload.entities.BattleUploader;
import upload.entities.CapitalCitiesUploader;
import upload.entities.CityUploader;
import upload.entities.ConstructionUploader;
import upload.entities.ContinentUploader;
import upload.entities.CountryUploader;
import upload.entities.CurrencyUploader;
import upload.entities.LanguageUploader;
import upload.entities.LeaderUploader;
import upload.entities.MilitaryActionUploader;
import upload.entities.WarUploader;
import upload.relations.AdminDivisionLeadersUploader;
import upload.relations.LanguagesInCountriesUploader;
import upload.relations.MilitaryActionLocationsUploader;
import upload.relations.MilitaryParticipantsUploader;
import db_entities.Entity;
import db_parsers.ParsedData;



/**
 * 
 * @author Denis
 * 
 * Database Batch uploader
 * Uploads chunks of BATCH_SIZE batches to the DB
 * May be used with INSERT and UPDATE queries
 * 
 * Recommended BATCH_SIZE for remote DB: 15000
 * 
 * 
 * NOTE:
 * 		this class itself will NOT multi-thread different batches, rather, they will be uploaded on the same thread
 *      for multithreaded upload,  use ParallelDBUploader wrapper class
 *
 */

public abstract class DBUploader implements Runnable {

	
/**
 * current batch size to upload
 */
	
protected int actualBatchSize;

/**
 * SQL DB connection
 */
protected Connection sqlConn;


/**
 * global error flag
 */
protected boolean error;


/**
 * maximum batch size
 */
public static int BATCH_SIZE = 20000;


public DBUploader(Connection sqlConnection)
{
	this.sqlConn = sqlConnection;
	this.actualBatchSize = 0;
}

/**
 * 
 * @return true iff there are more objects to upload
 */
protected abstract boolean hasMore();

/**
 * uploads the collection of entities to the DB
 * @return true on success, false otherwise
 */

public boolean upload()
{
	while(!error && hasMore())
	{
		
		// upload a batch
		uploadBatch();
	}
	
	if(error)
	{
		return false;
	}
	return true;
}


public void run()
{
	upload();
}


/**
 * Prints the time difference from now to the input time.
 */
private void printTimeDiff(long time) {
	time = (System.currentTimeMillis() - time) / 1000;
	System.out.println("Took " + time + " seconds");
}


/**
 * Constructs a prepared statement object
 * @return
 * @throws SQLException 
 */

protected abstract PreparedStatement constructPreparedStatement() throws SQLException;


/**
 * Format current batch into given stmt
 * @param stmt
 * @throws SQLException 
 */
protected abstract void prepareBatch(PreparedStatement stmt) throws SQLException;


/**
 * perhaps do something after commit (for example: handling keys)
 * @param stmt
 */
protected abstract void postCommit(PreparedStatement stmt) throws SQLException;



/**
 * Uploads the current batch to the DB
 */
protected void uploadBatch() {
	
	long time = System.currentTimeMillis();
	try (PreparedStatement pstmt = constructPreparedStatement()) {

		sqlConn.setAutoCommit(false);
		
		prepareBatch(pstmt);
		pstmt.executeBatch();
		sqlConn.commit();
		postCommit(pstmt);

		//System.out.print("Uploaded batch: " + this.actualBatchSize + " , ");
		//printTimeDiff(time);
		
	} catch (SQLException e) {
		System.out.println("ERROR uploadBatch(): " + e.getMessage());
		error = true;
		safelyRollBack();
		
	} finally {
		safelySetAutoCommit();
	}
}



/**
 * Attempts to set the connection back to auto-commit, ignoring errors.
 */
private void safelySetAutoCommit() {
	try {
		sqlConn.setAutoCommit(true);
	} catch (Exception e) {
	}
}

/**
 * Attempts to rollback, ignoring errors.
 */

private void safelyRollBack()
{
	try {
		sqlConn.rollback();
	}
	catch(Exception e)
	{
		System.out.println("Failed to rollback!");
	}
}

/**
 * 
 * @return batch query string. e.g. INSERT INTO conflicts(..
 * 						  or UPDATE ...
 */
protected abstract String getQueryString();


/**
 * 
 * All existing DBUploader types
 *
 */
public enum DBUploaderType
{
	AdminDivision,
	Battle,
	CapitalCities,
	City,
	Construction,
	Continent,
	Country,
	Currency,
	Language,
	Leader,
	MilitaryAction,
	War,
	AdminDivisionLeaders,
	LanguagesInCountries,
	MilitaryActionLocations,
	MilitaryParticipants
	
}


/**
 * DBUploader factory method
 * 
 * @param partialEntityIterator - iterator over collection of entities to upload
 * @param connection            - connection to be used by the upload
 * @param type                  - type of uploader, see enum above
 * @return the created uploader object
 */
public static DBUploader createUploader(Iterator<? extends Entity> partialEntityIterator,
										 Connection connection,
										 DBUploaderType type)
{
	switch(type)
	{
	case AdminDivision:
		return new AdminDivisionUploader(connection, partialEntityIterator);
	case Battle:
		return new BattleUploader(connection, partialEntityIterator);
	case CapitalCities:
		return new CapitalCitiesUploader(connection, partialEntityIterator);
	case City:
		return new CityUploader(connection, partialEntityIterator);
	case Construction:
		return new ConstructionUploader(connection, partialEntityIterator);
	case Continent:
		return new ContinentUploader(connection, partialEntityIterator);
	case Country:
		return new CountryUploader(connection, partialEntityIterator);
	case Currency:
		return new CurrencyUploader(connection, partialEntityIterator);
	case Language:
		return new LanguageUploader(connection, partialEntityIterator);
	case Leader:
		return new LeaderUploader(connection, partialEntityIterator);
	case MilitaryAction:
		return new MilitaryActionUploader(connection, partialEntityIterator);
	case War:
		return new WarUploader(connection, partialEntityIterator);
	case AdminDivisionLeaders:
		return new AdminDivisionLeadersUploader(connection, partialEntityIterator);
	case LanguagesInCountries:
		return new LanguagesInCountriesUploader(connection, partialEntityIterator);
	case MilitaryActionLocations:
		return new MilitaryActionLocationsUploader(connection, partialEntityIterator);
	case MilitaryParticipants:
		return new MilitaryParticipantsUploader(connection, partialEntityIterator);

	}
	
	return null;
}

/**
 * 
 * Get collection of entities that are required by given DBUploader type (both for relations and entities uploaders)
 * 
 * @param   type - type of UPloader
 * @return  the collection of entities required by the uploader, generated by the parser
 */

public static Collection<? extends Entity> getAllEntities(DBUploaderType type)
{
	switch(type)
	{
	case AdminDivision:
		return ParsedData.locationsMap.values();
	case Battle:
		return ParsedData.getBattleSet();
	case CapitalCities:
		return ParsedData.getCountriesSet();
	case City:
		return ParsedData.getCitiesSet();
	case Construction:
		return ParsedData.constructionsMap.values();
	case Continent:
		return ParsedData.continentsMap.values();
	case Country:
		return  ParsedData.getCountriesSet();
	case Currency:
		return  ParsedData.currenciesMap.values();
	case Language:
		return  ParsedData.langugagesMap.values();
	case Leader:
		return 	ParsedData.leadersMap.values();
	case War:
		return  ParsedData.getWarsSet();
	case AdminDivisionLeaders:
		return  ParsedData.leadersMap.values();
	case LanguagesInCountries:
		return  ParsedData.getCountriesSet();
		
	case MilitaryAction:
	case MilitaryActionLocations:
	case MilitaryParticipants:
		return   ParsedData.conflictMap.values();

	}
	
	return null;
	
}



}
