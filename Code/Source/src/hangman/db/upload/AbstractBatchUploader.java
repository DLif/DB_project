package hangman.db.upload;

import hangman.db.upload.entities.AdminDivisionUploader;
import hangman.db.upload.entities.BattleUploader;
import hangman.db.upload.entities.CapitalCitiesUploader;
import hangman.db.upload.entities.CityUploader;
import hangman.db.upload.entities.ConstructionUploader;
import hangman.db.upload.entities.ContinentUploader;
import hangman.db.upload.entities.CountryUploader;
import hangman.db.upload.entities.CurrencyUploader;
import hangman.db.upload.entities.LanguageUploader;
import hangman.db.upload.entities.LeaderUploader;
import hangman.db.upload.entities.MilitaryActionUploader;
import hangman.db.upload.entities.WarUploader;
import hangman.db.upload.relations.AdminDivisionLeadersUploader;
import hangman.db.upload.relations.LanguagesInCountriesUploader;
import hangman.db.upload.relations.MilitaryActionLocationsUploader;
import hangman.db.upload.relations.MilitaryParticipantsUploader;
import hangman.parsing.entities.Entity;
import hangman.parsing.parsers.ParsedData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;




/**
 * 
 * Database Batch Uploader
 * Uploads BATCH_SIZE batches of tuples to the DB, one after another
 * 
 * Recommended BATCH_SIZE for remote DB: 20000
 * 
 * 
 * NOTE:
 * 		This uploader will NOT multi-thread different batches, rather, they will be uploaded on the same thread, one after another.
 *      for multithreaded upload,  use ParallelDBUploader wrapper class
 *
 */

public abstract class AbstractBatchUploader implements Runnable {

	
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


public AbstractBatchUploader(Connection sqlConnection)
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
	if(!upload())
	{
		// upload failed, notify uploader
		ParsedDataUploader.setError(errorString());
	}
}


protected String errorString = null;

protected String errorString()
{
	return this.errorString;
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
	
	
	try (PreparedStatement pstmt = constructPreparedStatement()) {

		sqlConn.setAutoCommit(false);
		
		prepareBatch(pstmt);
		pstmt.executeBatch();
		sqlConn.commit();
		postCommit(pstmt);

		
	} catch (SQLException e) {
		this.errorString = "Error: "  + e.getMessage();
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
		//System.out.println("Failed to rollback!");
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
public static AbstractBatchUploader createUploader(Iterator<? extends Entity> partialEntityIterator,
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
